package com.codeup.bitebook.controllers;


import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.services.RecipeServiceAgain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SearchController {
    private final RecipeRepository recipeRepository;

    public SearchController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    @GetMapping("/recipes/all")
    public String findByAll(Model model) {
        model.addAttribute("recipes", recipeRepository.findAllByTitleOrDietaryOrRegionOrDifficultyOrTime("","","","",""));
        return "recipeIndex";
    }
    @GetMapping("/recipes/title")
    public String searchByTitle(Model model) {
        model.addAttribute("recipes", recipeRepository.findByTitle("Ham"));
        return "recipeIndex";
    }
    @GetMapping("/recipes/region")
    public String searchByRegion(Model model) {
        model.addAttribute("recipes", recipeRepository.findByRegion(""));
        return "recipeIndex";
    }
    @GetMapping("/recipes/dietary")
    public String searchByDietary(Model model) {
        model.addAttribute("recipes", recipeRepository.findByDietary(""));
        return "recipeIndex";
    }
    @GetMapping("/recipes/time")
    public String searchByTime(Model model) {
        model.addAttribute("recipes", recipeRepository.findByTime(""));
        return "recipeIndex";
    }
    @GetMapping("/recipes/difficulty")
    public String searchByDifficulty(Model model) {
        model.addAttribute("recipes", recipeRepository.findByDifficulty(""));
        return "recipeIndex";
    }

    @Autowired
    private RecipeServiceAgain service;

    @RequestMapping("/recipes/searchall")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<Recipe> listRecipes = service.listAll(keyword);
        model.addAttribute("listRecipes", listRecipes);
        model.addAttribute("keyword", keyword);

        return "recipeIndex";
    }

}
