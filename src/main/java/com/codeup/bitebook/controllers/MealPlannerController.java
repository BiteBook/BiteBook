package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.MealPlanner;
import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.MealPlannerRepository;
import com.codeup.bitebook.repositories.RecipeRepository;
import com.codeup.bitebook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;

@Controller
public class MealPlannerController {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final MealPlannerRepository mealPlannerRepository;

    @Autowired
    public MealPlannerController(UserRepository userRepository, RecipeRepository recipeRepository, MealPlannerRepository mealPlannerRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.mealPlannerRepository = mealPlannerRepository;
    }

    @PostMapping("/mealplanner/add")
    public String addRecipeToMealPlanner(@RequestParam Long recipeId, @RequestParam LocalDate date, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + recipeId));

        MealPlanner mealPlanner = new MealPlanner();
        mealPlanner.setUser(user);
        mealPlanner.setRecipe(recipe);
        mealPlanner.setDate(date);
        mealPlannerRepository.save(mealPlanner);

        return "redirect:/profile?username=" + user.getUsername();

    }
}
