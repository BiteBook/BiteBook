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



    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "recipe_description", columnDefinition = "TEXT")
    private String recipeDescription;

    @ManyToOne // Add this annotation for the Recipe association
    @JoinColumn(name = "recipe_id") // Assuming this column links to the id of the Recipe entity
    private Recipe recipe;

    // Add any other relevant fields and methods as needed
}
