//package com.codeup.bitebook.controllers;
//
//import com.codeup.bitebook.models.Recipe;
//import com.codeup.bitebook.models.RecipePage;
//import com.codeup.bitebook.models.RecipeSearchCriteria;
//import com.codeup.bitebook.services.RecipeService;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/search")
//public class RecipeSearchController {
//
//    private final RecipeService recipeService;
//
//    public RecipeSearchController(RecipeService recipeService) {
//        this.recipeService = recipeService;
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<Recipe>> getRecipes(RecipePage recipePage,
//                                                   RecipeSearchCriteria recipeSearchCriteria){
//        return new ResponseEntity<>(recipeService.getRecipes(recipePage, recipeSearchCriteria),
//        HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Recipe> addRecipe(Recipe recipe){
//        return new ResponseEntity<>(recipeService.addRecipe(recipe), HttpStatus.OK);
//    }
//}