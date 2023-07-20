package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.RecipeRepository;
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

@Controller
public class UserController {

    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    private RecipeRepository recipeRepository;

    @Autowired // Add this annotation
    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, RecipeRepository recipeRepository) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recipeRepository = recipeRepository; // Initialize the recipeRepository field
    }

    @GetMapping("/sign-up")

    public String showSignupForm(Model model){
        User loggedInUser = Authenticator.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);


        model.addAttribute("user", new User());
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
    public String showProfile(Model model, @RequestParam(name = "recipeId", required = false) Long recipeId) {
        User loggedInUser = Authenticator.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        // Check if a recipe ID is provided in the query parameter
        if (recipeId != null) {
            Recipe savedRecipe = recipeRepository.findById(recipeId).orElse(null);
            model.addAttribute("savedRecipe", savedRecipe);
        }

        return "users/profile";
    }


}

