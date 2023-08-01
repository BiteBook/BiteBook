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
        model.addAttribute("title", "Home");
        List<Recipe> readyIn15Recipes = recipeRepository.findRandomRecipesReadyIn15Minutes(PageRequest.of(0, 10));
        if (readyIn15Recipes.isEmpty()) {
        }
        model.addAttribute("readyIn15Recipes", readyIn15Recipes);

        return "home";
    }


    @GetMapping("/developers")
    public String showDevelopers(Model model) {
        model.addAttribute("title", "Developers");
        return "developers";
    }

    @GetMapping("/recipeIndex")
    public String showRecipeIndex(Model model) {
        model.addAttribute("title", "Recipe Index");
        return "recipeindex";
    }
}
