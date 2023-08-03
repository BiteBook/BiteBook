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

    @Column
    private String title;
    @Column(columnDefinition = "TEXT")
    private String ingredients;
    @Column(columnDefinition = "TEXT")
    private String instructions;
    @Column
    private String tools;
    @Column(length = 1000)
    private String photo;

    @Column
    private String difficulty;
    @Column
    private Integer hours;
    @Column
    private Integer minutes;
    @Column
    private Integer time;

    @Column
    private String region;

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
    @OneToMany(mappedBy = "recipe")
    private List<Review> reviews;
    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "recipe_diet_styles",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "diet_style_id")
    )
    private List<DietStyle> dietStyles;

    @ManyToMany
    @JoinTable(
            name = "recipe_allergens",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> allergens;


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

    public void setHours(Integer hours) {
        this.hours = hours;
        calculateTime();
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
        calculateTime();
    }

    private void calculateTime() {
        if (this.hours != null && this.minutes != null) {
            this.time = this.hours * 60 + this.minutes;
        }
    }


    public void setTime(int time) {
        this.time = time;
    }
}

