package com.codeup.bitebook.services;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceAgain {
    @Autowired
    private RecipeRepository repo;

    public List<Recipe> listAll(String keyword) {
        if (keyword != null) {
            return repo.search("%" + keyword + "%");
        }
        return repo.findAll();
    }
    public List<Recipe> listAllWithFilter(String keyword, User user) {
        List<Recipe> allRecipes = listAll(keyword);
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (matchesUserDietaryNeeds(recipe, user)) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }

    private boolean matchesUserDietaryNeeds(Recipe recipe, User user) {
        // Check if the recipe matches the user's dietary and allergy needs
        // This will depend on how you're storing the dietary and allergy information
        // For example, you might have a method on the User object that checks if a recipe matches the user's dietary needs
        return user.matchesDietaryNeeds(recipe);
    }

}
