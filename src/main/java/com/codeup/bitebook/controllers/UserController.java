package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.UserRepository;


import com.codeup.bitebook.services.Authenticator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;

        this.passwordEncoder = passwordEncoder;
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
}
