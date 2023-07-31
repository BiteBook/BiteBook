package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;



@Controller
public class HomeController {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Recipe> trendingRecipes = userFavoriteRepository.findTop10MostSavedRecipes(PageRequest.of(0, 10))
                .stream()
                .peek(objects -> System.out.println("objects[0]: " + objects[0])) // Add this line
                .map(objects -> objects[0] != null ? recipeRepository.findById((Long) objects[0]).orElse(null) : null)
                .collect(Collectors.toList());
        model.addAttribute("trendingRecipes", trendingRecipes);

        List<Recipe> readyIn15Recipes = recipeRepository.findRandomRecipesReadyIn15Minutes(PageRequest.of(0, 10));
        if (readyIn15Recipes.isEmpty()) {
            // Handle the case where there are no recipes that are ready in 15 minutes
            // For example, you could add a default recipe or show a message to the user
        }
        model.addAttribute("readyIn15Recipes", readyIn15Recipes);

        return "home";
    }


    @GetMapping("/developers")
    public String showDevelopers() {
        return "developers";
    }

    @GetMapping("/recipeIndex")
    public String showRecipeIndex() {
        return "recipeindex";
    }
}
