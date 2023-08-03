package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.models.UserWithRoles;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.RecipeServiceAgain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.codeup.bitebook.models.DietStyle;
import com.codeup.bitebook.models.Allergen;
import java.util.List;
import java.util.stream.Collectors;

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
    public String searchByAll(Model model, @RequestParam(name="keyword") String keyword, @RequestParam(name="dietaryFilter", required = false) String dietaryFilter, Authentication authentication) {
        List<Recipe> listRecipes;
        if ("on".equals(dietaryFilter)) {
            UserWithRoles userWithRoles = (UserWithRoles) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userWithRoles.getUsername());
            List<Long> dietStyleIds = currentUser.getDietaryPreferences().stream().map(DietStyle::getId).collect(Collectors.toList());
            List<Long> allergenIds = currentUser.getAllergyList().stream().map(Allergen::getId).collect(Collectors.toList());
            listRecipes = service.listAllWithFilter(keyword, dietStyleIds, allergenIds, currentUser);
        } else {
            listRecipes = service.listAll(keyword, null, null);
        }
        model.addAttribute("recipes", listRecipes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("title", "Search Recipes");

        return "recipeIndex";
    }
}
