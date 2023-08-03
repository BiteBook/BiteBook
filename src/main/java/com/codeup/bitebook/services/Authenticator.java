package com.codeup.bitebook.services;

import com.codeup.bitebook.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Authenticator {
    public static User getLoggedInUser() {
        User loggedInUser = new User();
        Object thing = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if(thing instanceof String || ((User) thing).getId() == 0) {
            loggedInUser.setUsername("Not logged in");
            return loggedInUser;
        }
        loggedInUser = (User) thing;
        return loggedInUser;
    }
}