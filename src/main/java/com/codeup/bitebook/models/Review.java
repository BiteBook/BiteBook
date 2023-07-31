package com.codeup.bitebook.models;

import jakarta.persistence.*;


@Entity
@Table(name="recipe-reviews")
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn (name = "recipeid")
    private Recipe recipe;

    @ManyToOne // Many reviews can be associated with one user
    @JoinColumn(name = "user_id") // This is the foreign key column in the recipe_reviews table
    private User user; // The user who wrote the review

    // getters and setters

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
