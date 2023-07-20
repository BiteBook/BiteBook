package com.codeup.bitebook.controllers;
import com.codeup.bitebook.models.UserFavorite;
import com.codeup.bitebook.repositories.UserFavoriteRepository;
import com.codeup.bitebook.services.Authenticator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;



import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository, UserFavoriteRepository userFavoriteRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userFavoriteRepository = userFavoriteRepository; // Initialize the userFavoriteRepository field
    }
    @GetMapping("/recipes")
    public String showRecipes(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipeIndex";
    }
    @GetMapping("/recipes/{id}")
    public String showRecipeDetails(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        model.addAttribute("recipes", recipe);
        return "recipeDetails";
    }

    @GetMapping("/recipes/new")
    public String showCreateForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "createRecipe";
    }
    @PostMapping("/recipes/new")
    public String createRecipe(@ModelAttribute Recipe newRecipe) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        // Set the user to the recipe before saving
        newRecipe.setUser(currentUser);
        Recipe savedRecipe = recipeRepository.save(newRecipe);

        // Save the recipe to the user's favorite list in the "user_favorite" table
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUser(currentUser);
        userFavorite.setRecipeId(savedRecipe.getId());
        userFavorite.setRecipeName(savedRecipe.getTitle());
        userFavorite.setRecipeDescription(savedRecipe.getDescription());
        userFavoriteRepository.save(userFavorite);

        // Redirect to the profile page with the saved recipe's ID as a query parameter
        return "redirect:/profile?recipeId=" + savedRecipe.getId();
    }




    @GetMapping("/recipes/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        model.addAttribute("recipe", recipe);
        return "editRecipe";
    }

    @PostMapping("/recipes/edit/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe recipe) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User currentUser = userRepository.findByUsername(userDetails.getUsername());
//
//        if (!recipe.getUser().equals(currentUser)) {
//            return "redirect:/error";
//        }

        recipeRepository.save(recipe);
        return "redirect:/recipes/" + id;
    }

    @DeleteMapping("/recipes/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        if (!recipe.getUser().equals(currentUser)) {
            return "redirect:/error";
        }

        recipeRepository.deleteById(id);
        return "redirect:/recipes";
    }


    @GetMapping("/profile/{recipeId}")
    public String showProfile(Model model, @PathVariable Long recipeId) {
        User loggedInUser = Authenticator.getLoggedInUser();
        model.addAttribute("user", loggedInUser);

        // Find the saved recipe by its ID
        Optional<Recipe> savedRecipe = recipeRepository.findById(recipeId);
        model.addAttribute("savedRecipe", savedRecipe.orElse(null));

        return "users/profile";
    }
    @GetMapping("/favorites")
    public String showFavorites(Model model, Authentication authentication) {
        // Get the currently authenticated user's details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        // Fetch the user's favorite recipes from the "user_favorite" table
        List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(currentUser);

        // Pass the list of favorite recipes to the Thymeleaf template
        model.addAttribute("favoriteRecipes", favoriteRecipes);

        return "users/savedFavorites"; // Adjust the template name with the relative path
    }

}
