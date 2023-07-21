package com.codeup.bitebook.controllers;
import com.codeup.bitebook.models.EdamamService;
import com.codeup.bitebook.models.NutritionInfo;
import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.models.UserFavorite;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserFavoriteRepository;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final EdamamService edamamService;
    private final UserFavoriteRepository userFavoriteRepository;
    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository, EdamamService edamamService, UserFavoriteRepository userFavoriteRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.edamamService = edamamService;
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
        NutritionInfo nutritionInfo = edamamService.getNutritionInfo(recipe.getIngredients());
        recipe.setCalories(nutritionInfo.getCalories());
        recipe.setProtein(nutritionInfo.getProtein());
        recipe.setCarbohydrates(nutritionInfo.getCarbohydrates());
        recipe.setFibre(nutritionInfo.getFibre());
        recipe.setFats(nutritionInfo.getFats());
        recipe.setSugar(nutritionInfo.getSugar());
        recipe.setSodium(nutritionInfo.getSodium());
        recipeRepository.save(recipe);
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
        recipeRepository.save(recipe);
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User currentUser = userRepository.findByUsername(userDetails.getUsername());
//
//        if (!recipe.getUser().equals(currentUser)) {
//            return "redirect:/error";
//        }
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