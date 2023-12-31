package com.codeup.bitebook.controllers;
import com.codeup.bitebook.models.Recipe;
import jakarta.persistence.*;


@Entity
@Table(name="recipe-reviews")

public class RatingController  {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn (name = "recipe_id")
    private Recipe recipe;
}


