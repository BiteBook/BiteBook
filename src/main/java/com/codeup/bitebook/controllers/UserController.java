package com.codeup.bitebook.controllers;
import com.codeup.bitebook.models.*;
import com.codeup.bitebook.repositories.*;
import com.codeup.bitebook.services.Authenticator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class UserController {

    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private MealPlannerRepository mealPlannerRepository;
    private RecipeRepository recipeRepository;
    private UserFavoriteRepository userFavoriteRepository;
    private PostRepository postDao;

    @Autowired
    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, RecipeRepository recipeRepository,UserFavoriteRepository userFavoriteRepository,MealPlannerRepository mealPlannerRepository, PostRepository postDao, DietStyleRepository dietStyleRepository, AllergenRepository allergenRepository) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recipeRepository = recipeRepository;
        this.userFavoriteRepository = userFavoriteRepository;
        this.mealPlannerRepository = mealPlannerRepository;
        this.postDao = postDao;
        this.dietStyleRepository = dietStyleRepository;
        this.allergenRepository = allergenRepository;
    }

    @Autowired
    private DietStyleRepository dietStyleRepository;

    @Autowired
    private AllergenRepository allergenRepository;


    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        User loggedInUser = Authenticator.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("user", new User());
        List<String> allAllergies = Arrays.asList("Peanuts", "Tree nuts", "Milk", "Egg", "Wheat", "Soy", "Fish", "Shellfish", "Other");
        model.addAttribute("allAllergies", allAllergies);

        // Add all diet styles and allergens to the model
        model.addAttribute("allDietStyles", dietStyleRepository.findAll());
        model.addAttribute("allAllergens", allergenRepository.findAll());
        model.addAttribute("title", "Sign Up");

        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model){
        if (userDao.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        }
        if (bindingResult.hasErrors()) {
            // Add all diet styles and allergens to the model again
            model.addAttribute("allDietStyles", dietStyleRepository.findAll());
            model.addAttribute("allAllergens", allergenRepository.findAll());
            return "users/sign-up";
        }
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        // Convert the IDs submitted by the form into Allergen and DietStyle entities
        List<Allergen> allergens = allergenRepository.findAllById(user.getAllergyList().stream().map(Allergen::getId).collect(Collectors.toList()));
        user.setAllergyList(allergens);

        List<DietStyle> dietStyles = dietStyleRepository.findAllById(user.getDietaryPreferences().stream().map(DietStyle::getId).collect(Collectors.toList()));
        user.setDietaryPreferences(dietStyles);
        model.addAttribute("title", "Save User");

        userDao.save(user);
        return "redirect:/login";
    }


    @GetMapping("/profile")
    public String showProfile(@RequestParam(required = false) String username, Principal principal, Model model) {
        User user;
        if (username == null || username.isEmpty()) {
            if (principal == null) {
                return "redirect:/login"; // Redirect to login if no username and no logged-in user
            }
            user = userDao.findByUsername(principal.getName());
        } else {
            user = userDao.findByUsername(username);
        }

        // Fetch meal planners for the user whose profile is being viewed
        List<MealPlanner> mealPlanners = mealPlannerRepository.findByUser(user);
        List<SimpleMealPlanner> simpleMealPlanners = mealPlanners.stream()
                .map(SimpleMealPlanner::new)
                .collect(Collectors.toList());
        model.addAttribute("mealPlanners", simpleMealPlanners);

        model.addAttribute("user", user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserWithRoles currentUser = (UserWithRoles) authentication.getPrincipal();




        model.addAttribute("currentUser", currentUser);
        List<Post> userPosts = postDao.findByCreatorOrderByCreatedDateDesc(user);
        if (userPosts.size() > 3) {
            userPosts = userPosts.subList(0, 3);
        }
        model.addAttribute("userPosts", userPosts);

//        List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(user);
//        model.addAttribute("favoriteRecipes", favoriteRecipes);

        List<String> allAllergies = Arrays.asList("Peanuts", "Tree nuts", "Milk", "Egg", "Wheat", "Soy", "Fish", "Shellfish", "Other");
        model.addAttribute("allAllergies", allAllergies);

        // Add all diet styles and allergens to the model
        model.addAttribute("allDietStyles", dietStyleRepository.findAll());
        model.addAttribute("allAllergens", allergenRepository.findAll());
        model.addAttribute("title", "Profile");

        model.addAttribute("selectedPage", "profile");
        return "users/profile";
    }





    @PostMapping("/profile/edit")
    public String editPreferences(@ModelAttribute User user, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());

        if (user.getDietaryPreferences() != null && !user.getDietaryPreferences().isEmpty()) {
            loggedInUser.setDietaryPreferences(user.getDietaryPreferences());
        }
        if (user.getAllergyList() != null && !user.getAllergyList().isEmpty()) {
            loggedInUser.setAllergyList(user.getAllergyList());
        }
        if (user.getOtherAllergies() != null && !user.getOtherAllergies().isEmpty()) {
            loggedInUser.setOtherAllergies(user.getOtherAllergies());
        }

        userDao.save(loggedInUser);

        return "redirect:/profile";
    }

    @GetMapping("/users/{userId}/profile/personal-recipes")
    public String showPersonalRecipes(@PathVariable Long userId, Model model) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Recipe> personalRecipes = recipeRepository.findByUser(user);
            model.addAttribute("title", "Personal Recipes");
            model.addAttribute("personalRecipes", personalRecipes);
            return "users/personalRecipes";
        } else {
            return "redirect:/404";
        }
    }


    @GetMapping("/users/{userId}/posts")
    public String showUserPosts(@PathVariable long userId, Model model) {
        if (userId <= 0) {
            return "redirect:/404";
        }
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Post> posts = postDao.findByCreatorOrderByCreatedDateDesc(user);
            model.addAttribute("posts", posts);
            model.addAttribute("title", "User Posts");
            return "users/userPosts";
        } else {
            return "redirect:/404";
        }
    }

    @PostMapping("/profile/edit-username")
    public String editUsername(@ModelAttribute User user, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());

        if (user.getUsername() != null && !user.getUsername().equals(loggedInUser.getUsername()) && userDao.findByUsername(user.getUsername()) != null) {
            return "redirect:/profile?error=username";
        }

        loggedInUser.setUsername(user.getUsername());
        userDao.save(loggedInUser);

        loggedInUser = userDao.findByUsername(user.getUsername());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());

        if (!passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
            return "redirect:/profile?error=password";
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            String hash = passwordEncoder.encode(newPassword);
            loggedInUser.setPassword(hash);
            userDao.save(loggedInUser);
        }

        return "redirect:/profile";
    }

    @GetMapping("/users/{userId}")
    public String showUserProfile(@PathVariable Long userId, Model model, Principal principal) {
        if (userId == null || userId <= 0) {
            return "redirect:/404";
        }
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            List<Post> userPosts = postDao.findByCreatorOrderByCreatedDateDesc(user);
            if (userPosts.size() > 3) {
                userPosts = userPosts.subList(0, 3);
            }
            model.addAttribute("userPosts", userPosts);
            if (principal != null) {
                User loggedInUser = userDao.findByUsername(principal.getName());
                model.addAttribute("currentUser", loggedInUser);
            }
            model.addAttribute("title", "User Profile");
            return "users/profile";
        } else {
            return "redirect:/404";
        }
    }

    @GetMapping("/api/username")
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal != null ? principal.getName() : "";
    }


}
