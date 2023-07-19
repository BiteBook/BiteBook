package com.codeup.bitebook.services;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.RecipePage;
import com.codeup.bitebook.models.RecipeSearchCriteria;
import com.codeup.bitebook.repositories.RecipeCriteriaRepository;
import com.codeup.bitebook.repositories.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCriteriaRepository recipeCriteriaRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeCriteriaRepository recipeCriteriaRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeCriteriaRepository = recipeCriteriaRepository;
    }

    public Page<Recipe> getRecipes(RecipePage recipePage,
                                   RecipeSearchCriteria recipeSearchCriteria){
        return recipeCriteriaRepository.findAllWithFilters(recipePage, recipeSearchCriteria);
    }
    public Recipe addRecipe(Recipe recipe){
        return recipeRepository.save(recipe);
    }
}
