package com.codeup.bitebook.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String landingPage() {
        return "<h1>This is the landing page for BiteBook!</h1>";
    }
}

