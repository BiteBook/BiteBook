package com.codeup.bitebook.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "recipe_description")
    private String recipeDescription;

    // Add any other relevant fields and methods as needed
}
