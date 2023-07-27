package com.codeup.bitebook.models;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

        @ManyToOne
        @JoinColumn (name = "reviewerId")
        private User reviewer;


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

        public void  setRating(int rating) {
            this.rating = rating;
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
        }




    }