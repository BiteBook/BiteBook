package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.models.UserWithRoles;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.RecipeServiceAgain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private final RecipeServiceAgain service;
    private final UserRepository userRepository;

    @Autowired
    public SearchController(RecipeServiceAgain service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping("/recipes/search")
    public String searchByAll(Model model, @RequestParam (name="keyword") String keyword, @RequestParam (name="dietaryFilter", required = false) String dietaryFilter, Authentication authentication) {
        System.out.println(" in search");
        List<Recipe> listRecipes;
        if ("on".equals(dietaryFilter)) {
            UserWithRoles userWithRoles = (UserWithRoles) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userWithRoles.getUsername());
            listRecipes = service.listAllWithFilter(keyword, currentUser);
        } else {
            listRecipes = service.listAll(keyword);
        }
        System.out.println(listRecipes);
        model.addAttribute("recipes", listRecipes);
        model.addAttribute("keyword", keyword);

        return "recipeIndex";
    }
}
