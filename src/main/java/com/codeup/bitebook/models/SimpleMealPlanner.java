package com.codeup.bitebook.models;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class SimpleMealPlanner {
    private LocalDate date;
    private String recipeTitle;
    private Long recipeId;

    public SimpleMealPlanner(MealPlanner mealPlanner) {
        this.date = mealPlanner.getDate();
        this.recipeTitle = mealPlanner.getRecipe().getTitle();
        this.recipeId = mealPlanner.getRecipe().getId();
    }
    public String getDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

}
