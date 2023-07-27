package com.codeup.bitebook.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_favorite")
@Getter
@Setter
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "recipe_description", columnDefinition = "TEXT")
    private String recipeDescription;

    @ManyToOne
    @JoinColumn(name = "recipeid")
    private Recipe recipe;


}

