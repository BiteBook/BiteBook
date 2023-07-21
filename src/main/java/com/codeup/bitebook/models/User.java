package com.codeup.bitebook.models;



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

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
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

}