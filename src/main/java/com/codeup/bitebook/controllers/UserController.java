package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.MealPlanner;
import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.models.UserFavorite;
import com.codeup.bitebook.repositories.MealPlannerRepository;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserFavoriteRepository;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.Authenticator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.codeup.bitebook.models.Post;
import com.codeup.bitebook.repositories.PostRepository;
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
    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, RecipeRepository recipeRepository,UserFavoriteRepository userFavoriteRepository,MealPlannerRepository mealPlannerRepository, PostRepository postDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recipeRepository = recipeRepository;
        this.userFavoriteRepository = userFavoriteRepository;
        this.mealPlannerRepository = mealPlannerRepository;
        this.postDao = postDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        User loggedInUser = Authenticator.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("user", new User());
        List<String> allAllergies = Arrays.asList("Peanuts", "Tree nuts", "Milk", "Egg", "Wheat", "Soy", "Fish", "Shellfish", "Other");
        model.addAttribute("allAllergies", allAllergies);
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model){
        if (userDao.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        }
        if (bindingResult.hasErrors()) {
            return "users/sign-up";
        }
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User loggedInUser = userDao.findByUsername(principal.getName());
        model.addAttribute("user", loggedInUser);
        model.addAttribute("currentUser", loggedInUser);

        List<Post> userPosts = postDao.findByCreatorOrderByCreatedDateDesc(loggedInUser);
        if (userPosts.size() > 3) {
            userPosts = userPosts.subList(0, 3);
        }
        model.addAttribute("userPosts", userPosts);

        List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(loggedInUser);
        model.addAttribute("favoriteRecipes", favoriteRecipes);

        List<MealPlanner> mealPlanners = mealPlannerRepository.findByUser(loggedInUser);
        model.addAttribute("mealPlanners", mealPlanners);

        List<String> allAllergies = Arrays.asList("Peanuts", "Tree nuts", "Milk", "Egg", "Wheat", "Soy", "Fish", "Shellfish", "Other");
        model.addAttribute("allAllergies", allAllergies);
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

    @GetMapping("/profile/personal-recipes")
    public String showPersonalRecipes(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User loggedInUser = userDao.findByUsername(principal.getName());
        List<Recipe> personalRecipes = recipeRepository.findByUser(loggedInUser);
        model.addAttribute("personalRecipes", personalRecipes);

        return "users/personalRecipes";
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
            if (principal != null) {
                User loggedInUser = userDao.findByUsername(principal.getName());
                model.addAttribute("loggedInUser", loggedInUser);
            }
            return "users/profile";
        } else {
            return "redirect:/404";
        }
    }
}
