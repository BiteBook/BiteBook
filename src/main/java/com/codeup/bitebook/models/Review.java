package com.codeup.bitebook.models;

import jakarta.persistence.*;


@Entity
@Table(name="recipe-reviews")
public class Review {


        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(nullable = false)
        private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Column(nullable = false)
        private String rating;

        @ManyToOne
        @JoinColumn (name = "recipeid")
        private Recipe recipe;
    }

