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
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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
    private  UserFavoriteRepository userFavoriteRepository;
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
    public String showProfile(Model model, @RequestParam(name = "recipeId", required = false) Long recipeId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User loggedInUser = userDao.findByUsername(principal.getName());
        model.addAttribute("user", loggedInUser);

        List<Post> userPosts = postDao.findByCreatorOrderByCreatedDateDesc(loggedInUser);
        if (userPosts.size() > 3) {
            userPosts = userPosts.subList(0, 3);
        }
        model.addAttribute("userPosts", userPosts);

        List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(loggedInUser);
        model.addAttribute("favoriteRecipes", favoriteRecipes);

        if (recipeId != null) {
            Recipe savedRecipe = recipeRepository.findById(recipeId).orElse(null);
            model.addAttribute("savedRecipe", savedRecipe);
        }

        List<MealPlanner> mealPlanners = mealPlannerRepository.findByUser(loggedInUser);
        model.addAttribute("mealPlanners", mealPlanners);

        List<String> allAllergies = Arrays.asList("Peanuts", "Tree nuts", "Milk", "Egg", "Wheat", "Soy", "Fish", "Shellfish", "Other");
        model.addAttribute("allAllergies", allAllergies);


        return "users/profile";
    }
    @PostMapping("/profile/edit")
    public String editPreferences(@ModelAttribute User user, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());

        loggedInUser.setDietaryPreferences(user.getDietaryPreferences());
        loggedInUser.setAllergyList(user.getAllergyList());
        loggedInUser.setOtherAllergies(user.getOtherAllergies());

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
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Post> posts = postDao.findByCreatorOrderByCreatedDateDesc(user);
            model.addAttribute("posts", posts);
            return "users/userPosts";
        } else {
//             redirect to a 404 page
            return "redirect:/404";
        }
    }

    @PostMapping("/profile/edit-username")
    public String editUsername(@ModelAttribute User user, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());

        // Check if the new username is already taken
        if (userDao.findByUsername(user.getUsername()) != null) {
            // If taken, redirect back to the profile page with an error message
            return "redirect:/profile?error=username";
        }

        loggedInUser.setUsername(user.getUsername());
        userDao.save(loggedInUser);

        // Fetch the updated user from the database
        loggedInUser = userDao.findByUsername(user.getUsername());

        // Create a new authentication token
        Authentication newAuth = new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities());

        // Update the security context with the new authentication token
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/profile";
    }
    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());

        // Check if the current password is correct
        if (!passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
            // If the current password is incorrect, redirect back to the profile page with an error message
            return "redirect:/profile?error=password";
        }

        // Encode the new password and save it
        String hash = passwordEncoder.encode(newPassword);
        loggedInUser.setPassword(hash);
        userDao.save(loggedInUser);

        return "redirect:/profile";
    }


}