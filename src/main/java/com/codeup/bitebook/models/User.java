package com.codeup.bitebook.models;
import jakarta.validation.constraints.*;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @ElementCollection
    private List<String> dietaryPreferences;
    @ElementCollection
    private List<String> allergyList;
    @Column
    private String otherAllergies;


    @OneToMany(mappedBy = "user")
    private List<MealPlanner> mealPlanners;



    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        username = copy.username;
        password = copy.password;
        dietaryPreferences = copy.dietaryPreferences;
        allergyList = copy.allergyList;
    }


    @Override
    public String toString() {
        return "User id " + id + " username: " + username;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean matchesDietaryNeeds(Recipe recipe) {
        // Check if the recipe's diet style matches the user's dietary preferences
        if (dietaryPreferences != null && !dietaryPreferences.contains(recipe.getDietStyle())) {
            return false;
        }

        // Check if the recipe contains any allergens that the user is allergic to
        if (allergyList != null) {
            for (String allergen : allergyList) {
                if (recipe.getAllergens() != null && recipe.getAllergens().contains(allergen)) {
                    return false;
                }
            }
        }

        // If the recipe passed all checks, it matches the user's dietary needs
        return true;
    }

}