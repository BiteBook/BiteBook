package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user, Model model) {
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("message", "Username already exists");
            return "users/sign-up";
        }

        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userRepository.save(user);
        return "redirect:/login";
    }
}


