package com.codeup.bitebook.controllers;
import com.codeup.bitebook.models.*;
import com.codeup.bitebook.repositories.*;
import com.codeup.bitebook.services.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.security.Principal;
import java.util.List;
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

    @Autowired
    private UserRepository userDao;
    @Autowired
    private DietStyleRepository dietStyleRepository;

    @Autowired
    private AllergenRepository allergenRepository;

    @GetMapping("/recipes")
    public String showRecipes(Model model) {
        model.addAttribute("title", "Recipes");
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipeIndex";
    }

    @GetMapping("/recipes/{id}")
    public String showRecipeDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        model.addAttribute("recipes", recipe);
        model.addAttribute("review", new Review());
        model.addAttribute("title", "Recipe Details");

        // Fetch the comments and ratings for the recipe from the database
        List<Review> comments = reviewRepository.findByRecipe(recipe);
        List<Review> ratings = reviewRepository.findByRecipe(recipe);
        List<Review> reviews = reviewRepository.findByRecipe(recipe);

        // Add the comments and ratings to the model without redefining variables
        model.addAttribute("comments", comments);
        model.addAttribute("reviews", ratings);
        model.addAttribute("reviews", reviews);
        model.addAttribute("allDietStyles", dietStyleRepository.findAll());
        model.addAttribute("allAllergens", allergenRepository.findAll());
        model.addAttribute("recipeDietStyles", recipe.getDietStyles());
        model.addAttribute("recipeAllergens", recipe.getAllergens());


        // Fetch the usernames of reviewers for the recipe from the database and add them to the model
        List<String> users = getUsersForRecipe(recipe);
        model.addAttribute("users", users);

        // Check if the user is logged in before trying to get the UserDetails and User objects
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        } else {
            model.addAttribute("currentUser", null);
        }

        return "recipeDetails";
    }


    private List<String> getUsersForRecipe(Recipe recipe) {
        List<Review> reviews = reviewRepository.findByRecipe(recipe);
        List<String> users = new ArrayList<>();
        for (Review review : reviews) {
            User user = review.getUser();
            if (user != null) {
                users.add(user.getUsername());
            }
        }
        return users;
    }



    @PostMapping("/recipes/{id}")
    public String getComments(@PathVariable long id, @ModelAttribute Review review, @RequestParam int rating, @RequestParam String comment, Principal principal) {
        System.out.println("rating " + rating);
        review.setRating(rating);
        review.setComment(comment);
        System.out.println("comment " + comment);
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        review.setRecipe(recipe);
        User user = userDao.findByUsername(principal.getName());
        review.setUser(user);
        reviewRepository.save(review);

        return "redirect:/recipes/" + id;
    }


    @GetMapping("/recipes/new")
    public String showCreateRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("title", "Create Recipe");
        model.addAttribute("allDietStyles", dietStyleRepository.findAll());
        model.addAttribute("allAllergens", allergenRepository.findAll());
        return "createRecipe";
    }

    @PostMapping("/recipes/new")
    public String createRecipe(@ModelAttribute Recipe recipe, @RequestParam("photo") String photoUrl, @RequestParam(required = false) List<Long> dietStyleIds, @RequestParam List<Long> allergenIds, Authentication authentication)
    {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        recipe.setUser(currentUser);
        recipe.setPhoto(photoUrl);
        recipe.setDietStyles(dietStyleRepository.findAllById(dietStyleIds));
        recipe.setAllergens(allergenRepository.findAllById(allergenIds));
        recipe.setTime(recipe.getHours() * 60 + recipe.getMinutes());
        NutritionInfo nutritionInfo = edamamService.allNutrition(recipe.getIngredients());
        recipe.setCalories(nutritionInfo.getCalories());
        recipe.setProtein(nutritionInfo.getProtein());
        recipe.setCarbohydrates(nutritionInfo.getCarbohydrates());
        recipe.setFibre(nutritionInfo.getFibre());
        recipe.setFats(nutritionInfo.getFats());
        recipe.setSugar(nutritionInfo.getSugar());
        recipe.setSodium(nutritionInfo.getSodium());
        recipe.setDietStyles(dietStyleRepository.findAllById(dietStyleIds));

        if (allergenIds == null || allergenIds.isEmpty()) {
            // Handle the error, e.g., return an error message or throw an exception
        } else {
            for (Long allergenId : allergenIds) {
                Optional<Allergen> allergen = allergenRepository.findById(allergenId);
                if (!allergen.isPresent()) {
                    // Handle the error, e.g., return an error message or throw an exception
                }
            }
        }
        if (dietStyleIds != null) {
            recipe.setDietStyles(dietStyleRepository.findAllById(dietStyleIds));
        }

        recipeRepository.save(recipe);
        recipeRepository.flush();

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
        model.addAttribute("title", "Edit Recipe");
        model.addAttribute("recipe", recipe);
        model.addAttribute("allDietStyles", dietStyleRepository.findAll());
        model.addAttribute("allAllergens", allergenRepository.findAll());
        return "editRecipe";
    }


    @PutMapping("/recipes/edit/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe updatedRecipe, @RequestParam List<Long> dietStyleIds, @RequestParam List<Long> allergenIds, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        int hours = (updatedRecipe.getHours() != null) ? updatedRecipe.getHours() : 0;
        int minutes = (updatedRecipe.getMinutes() != null) ? updatedRecipe.getMinutes() : 0;

        updatedRecipe.setTime(hours * 60 + minutes);

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
        updatedRecipe.setDietStyles(dietStyleRepository.findAllById(dietStyleIds));
        updatedRecipe.setAllergens(allergenRepository.findAllById(allergenIds));

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
            model.addAttribute("title", "Favorites");
            model.addAttribute("favoriteRecipes", favoriteRecipes);
            return "users/savedFavorites";
        } else {
            return "redirect:/404";
        }
    }

    @PostMapping("/recipes/{id}/favorite")
    public String addToFavorites(@PathVariable Long id, Principal principal) {
        User loggedInUser = userDao.findByUsername(principal.getName());
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe != null) {
            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(loggedInUser);
            userFavorite.setCreator(recipe.getUser());
            userFavorite.setRecipeId(recipe.getRecipeid());
            userFavorite.setRecipeName(recipe.getTitle());
            userFavorite.setRecipeDescription(recipe.getInstructions());
            userFavoriteRepository.save(userFavorite);
        }
        return "redirect:/recipes/" + id;
    }
    @PostMapping("/reviews")
    public String submitReview(@RequestParam String comment, @RequestParam int rating, @AuthenticationPrincipal User user) {
        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        review.setUser(user);
        reviewRepository.save(review);
        return "redirect:/reviews";
    }
}
