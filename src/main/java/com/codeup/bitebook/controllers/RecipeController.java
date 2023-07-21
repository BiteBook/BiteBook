package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.UserFavorite;
import com.codeup.bitebook.repositories.UserFavoriteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserRepository;

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository, UserFavoriteRepository userFavoriteRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userFavoriteRepository = userFavoriteRepository;
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
    public String createRecipe(@ModelAttribute Recipe recipe) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User currentUser = userRepository.findByUsername(userDetails.getUsername());
//        recipe.setUser(currentUser);
//        NutritionInfo nutritionInfo = edamamCall.getNutritionInfo(recipe.getIngredients());
//        recipe.setCalories(nutritionInfo.getCalories());
//        recipeRepository.save(recipe);
        return "redirect:/recipes/" + recipe.getRecipeid();
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

    @PostMapping("/recipes/{recipeId}/favorite")
    public String addToFavorites(@PathVariable Long recipeId, Authentication authentication) {
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername());

            Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

            if (recipe != null) {
                UserFavorite userFavorite = new UserFavorite();
                userFavorite.setUser(currentUser);
                userFavorite.setRecipeId(recipe.getId());
                userFavorite.setRecipeName(recipe.getTitle());
                userFavorite.setRecipeDescription(recipe.getDescription());
                userFavoriteRepository.save(userFavorite);

            }

            return "redirect:/recipes/" + recipeId;
        } else {
            // Redirect to login page if the user is not authenticated
            return "redirect:/login";
        }
    }

}