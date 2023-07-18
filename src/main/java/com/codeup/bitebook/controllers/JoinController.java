package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class JoinController {


    @GetMapping("/join")
    public String showJoinForm(Model model) {
        ArrayList<Item> shoppingCart = new ArrayList<>();
        shoppingCart.add(new Item("thing"));
        shoppingCart.add(new Item("thing2"));
        shoppingCart.add(new Item("thing3"));
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("page_has_error", true);
        return "join";
    }


    @PostMapping("/join")
    public String joinCohort(@RequestParam(name = "cohort") String cohort, Model model) {
        model.addAttribute("cohort", "Welcome to " + cohort + "!");
        return "join";
    }

}
