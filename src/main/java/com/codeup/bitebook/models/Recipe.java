package com.codeup.bitebook.models;

import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "recipes")
@Data
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
    private Double calories;
    private Double protein;
    private Double fibre;
    @OneToMany(mappedBy = "recipe")
    private List<MealPlanner> mealPlanners;
    @ManyToOne
    private User user;
}
