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

import java.io.IOException;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

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

        // Fetch the usernames of reviewers for the recipe from the database and add them to the model
        List<String> reviewers = getReviewersForRecipe(recipe);
        model.addAttribute("reviewers", reviewers);



        // Use the existing 'authentication' parameter directly to get 'currentUser'
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);

        return "recipeDetails";
    }
    private List<String> getReviewersForRecipe(Recipe recipe) {
        List<Review> reviews = reviewRepository.findByRecipe(recipe);
        List<String> reviewers = new ArrayList<>();
        for (Review review : reviews) {
            User reviewer = review.getReviewer();
            if (reviewer != null) {
                reviewers.add(reviewer.getUsername());
            }
        }
        return reviewers;
    }

    @PostMapping("/recipes/{id}")
    public String getComments (@PathVariable long id ,@ModelAttribute Review review ,@RequestParam int rating,@RequestParam String comment){
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
    public String createRecipe(@ModelAttribute Recipe recipe, @RequestParam("photo") String photoUrl, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        recipe.setUser(currentUser);
        recipe.setPhoto(photoUrl);
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
        System.out.println(photoUrl);

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

        if (!updatedRecipe.getIngredients().equals(currentRecipe.getIngredients())) {
            NutritionInfo nutritionInfo = edamamService.allNutrition(updatedRecipe.getIngredients());
            updatedRecipe.setCalories(nutritionInfo.getCalories());
            updatedRecipe.setProtein(nutritionInfo.getProtein());
            updatedRecipe.setCarbohydrates(nutritionInfo.getCarbohydrates());
            updatedRecipe.setFibre(nutritionInfo.getFibre());
            updatedRecipe.setFats(nutritionInfo.getFats());
            updatedRecipe.setSugar(nutritionInfo.getSugar());
            updatedRecipe.setSodium(nutritionInfo.getSodium());
        } else {
            // If ingredients have not changed, copy nutrition facts from current recipe
            updatedRecipe.setCalories(currentRecipe.getCalories());
            updatedRecipe.setProtein(currentRecipe.getProtein());
            updatedRecipe.setCarbohydrates(currentRecipe.getCarbohydrates());
            updatedRecipe.setFibre(currentRecipe.getFibre());
            updatedRecipe.setFats(currentRecipe.getFats());
            updatedRecipe.setSugar(currentRecipe.getSugar());
            updatedRecipe.setSodium(currentRecipe.getSodium());
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

    @GetMapping("/users/{userId}/favorites")
    public String showFavorites(@PathVariable Long userId, Model model) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(user);
            model.addAttribute("favoriteRecipes", favoriteRecipes);
            return "users/savedFavorites";
        } else {
            return "redirect:/404";
        }
    }

    @PostMapping("/users/{userId}/recipes/{id}/favorite")
    public String addToFavorites(@PathVariable Long userId, @PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Recipe recipe = recipeRepository.findById(id).orElse(null);
            if (recipe != null) {
                UserFavorite userFavorite = new UserFavorite();
                userFavorite.setUser(user);
                userFavorite.setRecipeId(recipe.getRecipeid());
                userFavorite.setRecipeName(recipe.getTitle());
                userFavorite.setRecipeDescription(recipe.getInstructions());
                userFavoriteRepository.save(userFavorite);
            }
            return "redirect:/users/" + userId + "/recipes/" + id;
        } else {
            return "redirect:/404";
        }
    }



        @GetMapping("/profile/recommendations")
        public String showRecommendations(Model model, Authentication authentication) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername());

            // Get user's past ratings from the database
            List<Review> pastRatings = reviewRepository.findByReviewer(currentUser);

            //  Calculate average ratings for each recipe
            Map<Long, Integer> averageRatings = new HashMap<>();
            Map<Long, Integer> ratingCounts = new HashMap<>();
            for (Review rating : pastRatings) {
                Long recipeId = rating.getRecipe().getRecipeid();
                int ratingValue = rating.getRating();

                if (averageRatings.containsKey(recipeId)) {
                    int currentRatingSum = averageRatings.get(recipeId);
                    int currentRatingCount = ratingCounts.get(recipeId);
                    averageRatings.put(recipeId, currentRatingSum + ratingValue);
                    ratingCounts.put(recipeId, currentRatingCount + 1);
                } else {
                    averageRatings.put(recipeId, ratingValue);
                    ratingCounts.put(recipeId, 1);
                }
            }

            // Calculate the average rating for each recipe
            Map<Long, Integer> averageRatingsResult = new HashMap<>();
            for (Long recipeId : averageRatings.keySet()) {
                int ratingSum = averageRatings.get(recipeId);
                int ratingCount = ratingCounts.get(recipeId);
                int averageRating = ratingSum / ratingCount;
                averageRatingsResult.put(recipeId, averageRating);
            }

            // Filter out recipes with average ratings below 4
            List<Recipe> recommendedRecipes = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : averageRatingsResult.entrySet()) {
                Long recipeId = entry.getKey();
                int averageRating = entry.getValue();

                if (averageRating >= 4) {
                    Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
                    if (recipe != null) {
                        recommendedRecipes.add(recipe);
                    }
                }
            }

            model.addAttribute("recommendedRecipes", recommendedRecipes);
            return "recommendations";
        }

        // Other existing methods...
    }



