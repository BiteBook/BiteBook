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
        public String createRecipe(@RequestParam Long recipeId, Authentication authentication) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername());

            Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

            if (recipe != null) {
                // Save the recipe to the user's favorite list in the "user_favorite" table
                UserFavorite userFavorite = new UserFavorite();
                userFavorite.setUser(currentUser);
                userFavorite.setRecipeId(recipe.getId());
                userFavorite.setRecipeName(recipe.getTitle());
                userFavorite.setRecipeDescription(recipe.getDescription());
                userFavoriteRepository.save(userFavorite);
            }

            // Redirect to the profile page with the saved recipe's ID as a query parameter
            return "redirect:/profile?recipeId=" + recipeId;
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

//        @GetMapping("/profile/{recipeId}")
//        public String showProfile(Model model, @PathVariable Long recipeId) {
//            User loggedInUser = Authenticator.getLoggedInUser();
//            model.addAttribute("user", loggedInUser);
//
//            Optional<Recipe> savedRecipe = recipeRepository.findById(recipeId);
//            model.addAttribute("savedRecipe", savedRecipe.orElse(null));
//
//            return "users/profile";
//        }

        @GetMapping("/favorites")
        public String showFavorites(Model model, Authentication authentication) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername());

            List<UserFavorite> favoriteRecipes = userFavoriteRepository.findByUser(currentUser);

            model.addAttribute("favoriteRecipes", favoriteRecipes);
//            System.out.println(recipe);
//            UserFavorite userFavorite = new UserFavorite();
//            userFavorite.setUser(currentUser);
//            userFavorite.setRecipeId(recipe.getId());
//            userFavorite.setRecipeName(recipe.getTitle());
//            userFavorite.setRecipeDescription(recipe.getDescription());
//            userFavoriteRepository.save(userFavorite);
//            return "redirect:/profile?recipeId=" + recipe.getId();
            return "users/savedFavorites";
        }

    }
