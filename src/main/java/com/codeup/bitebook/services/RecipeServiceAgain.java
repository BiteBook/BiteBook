package com.codeup.bitebook.services;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceAgain {
    @Autowired
    private RecipeRepository repo;

    public List<Recipe> listAll(String keyword, List<Long> dietStyleIds, List<Long> allergenIds) {
        if (keyword != null) {
            if (dietStyleIds != null && allergenIds != null) {
                return repo.search("%" + keyword + "%", dietStyleIds, allergenIds);
            } else {
                return repo.searchWithoutFilter("%" + keyword + "%");
            }
        }
        return repo.findAll();
    }
    public List<Recipe> listAllWithFilter(String keyword, List<Long> dietStyleIds, List<Long> allergenIds, User currentUser) {
        // Fetch all recipes that match the keyword
        List<Recipe> recipes = listAll(keyword, null, null);

        // Filter out the recipes that don't match the user's dietary needs
        recipes = recipes.stream()
                .filter(recipe -> recipe.getDietStyles().stream().anyMatch(dietStyle -> dietStyleIds.contains(dietStyle.getId())))
                .filter(recipe -> recipe.getAllergens().stream().noneMatch(allergen -> allergenIds.contains(allergen.getId())))
                .collect(Collectors.toList());

        return recipes;
    }



    private boolean matchesUserDietaryNeeds(Recipe recipe, User user) {
        // Check if the recipe matches the user's dietary and allergy needs
        // This will depend on how you're storing the dietary and allergy information
        // For example, you might have a method on the User object that checks if a recipe matches the user's dietary needs
        return user.matchesDietaryNeeds(recipe);
    }

}
