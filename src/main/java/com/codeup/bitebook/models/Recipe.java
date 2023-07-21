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
    @Column
    private Long recipeid;

    @Column
    private String title;
    @Column(columnDefinition = "TEXT")
    private String ingredients;
    @Column(columnDefinition = "TEXT")
    private String instructions;
    @Column
    private String tools;
    @Column
    private String photo;
    @Column
    private String difficulty;
    @Column
    private String time;
    @Column
    private String region;
    @Column
    private String dietary;
    @Column
    private Double calories;
    @Column
    private Double protein;
    @Column
    private Double fibre;
    @Column
    private Double carbohydrates;
    @Column
    private Double fats;
    @Column
    private Double sugar;
    @Column
    private Double sodium;
    @OneToMany(mappedBy = "recipe")
    private List<MealPlanner> mealPlanners;
    @ManyToOne
    private User user;



    @Override
    public String toString() {
        return title;
    }

    public Long getId() {
        return recipeid;
    }

    public String getDescription() {
        return instructions;
    }

}
