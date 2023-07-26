package com.codeup.bitebook.controllers;
import com.codeup.bitebook.models.*;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.ReviewRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final EdamamService edamamService;
    private final UserFavoriteRepository userFavoriteRepository;
    private final ReviewRepository reviewRepository;
    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository, EdamamService edamamService, UserFavoriteRepository userFavoriteRepository, ReviewRepository reviewRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.edamamService = edamamService;
        this.userFavoriteRepository = userFavoriteRepository;
        this.reviewRepository = reviewRepository;
    }
    @GetMapping("/recipes")
    public String showRecipes(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipeIndex";
    }
    @GetMapping("/recipes/{id}")
    public String showRecipeDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        model.addAttribute("recipes", recipe);
        model.addAttribute("review", new Review());

        // Fetch the comments and ratings for the recipe from the database
        List<Review> comments = reviewRepository.findByRecipe(recipe);
        List<Review> ratings = reviewRepository.findByRecipe(recipe);

        // Add the comments and ratings to the model without redefining variables
        model.addAttribute("comments", comments);
        model.addAttribute("reviews", ratings);

        // Use the existing 'authentication' parameter directly to get 'currentUser'
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);

        return "recipeDetails";
    }
    @PostMapping("/recipes/{id}")
    public String getComments (@PathVariable long id ,@ModelAttribute Review review ,@RequestParam Integer rating,@RequestParam String comment){
        System.out.println("rating " + rating );
        review.setRating(rating);
        review.setComment(comment);
        System.out.println("comment " + comment);
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        review.setRecipe(recipe);
        reviewRepository.save(review);


        return "redirect:/recipes/" + id;



    }





        



    @GetMapping("/recipes/new")
    public String showCreateForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "createRecipe";
    }
    @PostMapping("/recipes/new")
    public String createRecipe(@ModelAttribute Recipe recipe, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        recipe.setUser(currentUser);
        System.out.println(recipe.getIngredients());
        NutritionInfo nutritionInfo = edamamService.allNutrition(recipe.getIngredients());
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
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        if (!recipe.getUser().equals(currentUser)) {
            return "redirect:/error";
        }

        model.addAttribute("recipe", recipe);
        return "editRecipe";
    }

    @PutMapping("/recipes/edit/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe updatedRecipe, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        Recipe currentRecipe = recipeRepository.findById(id).orElseThrow();

        if (!currentRecipe.getUser().equals(currentUser)) {
            return "redirect:/error";
        }

        updatedRecipe.setUser(currentUser);
        updatedRecipe.setRecipeid(id);
        recipeRepository.save(updatedRecipe);
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

    @GetMapping("/favorites")
    public String showFavorites(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(currentUser);

        model.addAttribute("favoriteRecipes", favoriteRecipes);
        return "users/savedFavorites";
    }

    @PostMapping("/recipes/{id}/favorite")
    public String addToFavorites(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        Recipe recipe = recipeRepository.findById(id).orElse(null);

        if (recipe != null) {
            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(currentUser);
            userFavorite.setRecipeName(recipe.getTitle());
            userFavorite.setRecipeDescription(recipe.getInstructions());
            userFavoriteRepository.save(userFavorite);
        }

        return "redirect:/recipes/" + id;
    }

    @GetMapping("/profile/recommendations")
    public String showRecommendations(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        // Get user's past ratings from the database
        List<Review> pastRatings = reviewRepository.findByReviewer(currentUser);

        // Get user's saved recipes from the database
        List<UserFavorite> savedRecipes = userFavoriteRepository.findByUser(currentUser);

        //  Calculate average ratings for each recipe
        Map<Recipe, Integer> averageRatings = new HashMap<>();
        for (Review rating : pastRatings) {
            Recipe recipe = rating.getRecipe();
            int  ratingValue = rating.getRating();

            if (averageRatings.containsKey(recipe)) {
                int currentRating = averageRatings.get(recipe);
                int updatedRating = (currentRating + ratingValue) / 2;
                averageRatings.put(recipe, updatedRating);
            } else {
                averageRatings.put(recipe, ratingValue);
            }
        }

//   Filter out recipes with ratings below 4.0 and recipes that are already saved
        List<Recipe> recommendedRecipes = new ArrayList<>();
        for (Map.Entry<Recipe, Integer> entry : averageRatings.entrySet()) {
            Recipe recipe = entry.getKey();
            int averageRating = entry.getValue();

            if (averageRating >= 4.0 && !savedRecipes.contains(recipe)) {
                recommendedRecipes.add(recipe);
            }
        }


        model.addAttribute("recommendedRecipes", recommendedRecipes);
        return "recommendation";
    }



}