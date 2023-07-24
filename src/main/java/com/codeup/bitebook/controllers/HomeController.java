package com.codeup.bitebook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "home";
    }

    @GetMapping("/developers")
    public String showDevelopers() {
        return "developers";
    }


    @GetMapping("/recipeIndex")
    public String showRecipeIndex() {
        return "recipeindex";
    }


}

