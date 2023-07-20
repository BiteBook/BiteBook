package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Recipe;
import java.util.List;

import com.codeup.bitebook.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class SearchController {

    @Autowired
    private RecipeService service;
    @RequestMapping("/recipesearch")
    public String viewRecipes(Model model,
                              @Param("keyword") String keyword){
        List<Recipe> listRecipes = service.listAll(keyword);
        model.addAttribute("listRecipes", listRecipes);
        model.addAttribute("keyword", keyword);

        return "/recipesearch";
    }

//    @RequestMapping("/newrecipe")
//    public String showNewRecipe(Model model){
//        Recipe recipe = new Recipe();
//        model.addAttribute("recipe", recipe);
//
//        return "new_recipe";
//    }
}
