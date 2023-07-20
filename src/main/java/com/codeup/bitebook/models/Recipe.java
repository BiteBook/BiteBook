package com.codeup.bitebook.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "recipes")
@Getter @Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long recipeid;

    private String title;
    private String ingredients;
    private String instructions;
    private String tools;
    private String photo;
    private String difficulty;
    private String time;
    private String region;
    private String dietary;
    private String calories;
    private String protein;
    private String fibre;
    @OneToMany(mappedBy = "recipe")
    private List<MealPlanner> mealPlanners;
    @ManyToOne
    private User user;
    @Override
    public String toString() {
        return title;
    }
}
