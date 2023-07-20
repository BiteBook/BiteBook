package com.codeup.bitebook.controllers;
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

    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
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

}
