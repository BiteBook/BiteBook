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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;


@Controller
public class UserController {

    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private MealPlannerRepository mealPlannerRepository;


    private RecipeRepository recipeRepository;

    private  UserFavoriteRepository userFavoriteRepository;

    @Autowired
    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, RecipeRepository recipeRepository,UserFavoriteRepository userFavoriteRepository,MealPlannerRepository mealPlannerRepository) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recipeRepository = recipeRepository;
        this.userFavoriteRepository = userFavoriteRepository;
        this.mealPlannerRepository = mealPlannerRepository;

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
    public String saveUser(@ModelAttribute User user){
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

}