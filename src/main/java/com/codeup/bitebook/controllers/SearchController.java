package com.codeup.bitebook.controllers;


import com.codeup.bitebook.repositories.RecipeRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
    private final RecipeRepository recipeRepository;

    public SearchController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
//    @GetMapping("/recipes/all")
//    public String findByAll(Model model) {
//        model.addAttribute("recipes", recipeRepository.findAllByTitleOrDietaryOrRegionOrDifficultyOrTime("","","","",0));
//        return "recipeIndex";
//    }
    @GetMapping("/recipe/title")
    public String searchByTitle(Model model,
        @Param("keyword") String keyword) {
        model.addAttribute("recipes", recipeRepository.findByTitle(keyword));
        return "recipeIndex";
    }
    @GetMapping("/recipe/region")
    public String searchByRegion(Model model) {
        model.addAttribute("recipes", recipeRepository.findByRegion(""));
        return "recipeIndex";
    }
    @GetMapping("/recipe/dietary")
    public String searchByDietary(Model model) {
        model.addAttribute("recipes", recipeRepository.findByDietary(""));
        return "recipeIndex";
    }
    @GetMapping("/recipe/time")
    public String searchByTime(Model model) {
        model.addAttribute("recipes", recipeRepository.findByTime(""));
        return "recipeIndex";
    }
    @GetMapping("/recipe/difficulty")
    public String searchByDifficulty(Model model) {
        model.addAttribute("recipes", recipeRepository.findByDifficulty(""));
        return "recipeIndex";
    }
}
