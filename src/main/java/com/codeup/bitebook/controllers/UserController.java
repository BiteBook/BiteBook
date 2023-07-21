package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.MealPlanner;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.MealPlannerRepository;
import com.codeup.bitebook.repositories.UserRepository;


import com.codeup.bitebook.services.Authenticator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private MealPlannerRepository mealPlannerRepository;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, MealPlannerRepository mealPlannerRepository) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.mealPlannerRepository = mealPlannerRepository;
    }

    @GetMapping("/sign-up")

    public String showSignupForm(Model model){
        User loggedInUser = Authenticator.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);


        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "redirect:/login";
    }
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userDao.findByUsername(principal.getName());
        List<MealPlanner> mealPlanners = mealPlannerRepository.findByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("mealPlanners", mealPlanners);
        return "users/profile";
    }

}

