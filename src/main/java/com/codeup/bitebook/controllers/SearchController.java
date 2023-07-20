package com.codeup.bitebook.controllers;


import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.services.RecipeServiceAgain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    public SearchController(RecipeRepository recipeRepository) {
    }

    @Autowired
    private RecipeServiceAgain service;

    @GetMapping("/recipes/search")
    public String searchByAll(Model model, @RequestParam (name="keyword") String keyword) {
        System.out.println(" in search");
        List<Recipe> listRecipes = service.listAll(keyword);
        System.out.println(listRecipes);
        model.addAttribute("recipes", listRecipes);
        model.addAttribute("keyword", keyword);

        return "recipeIndex";
    }

}
