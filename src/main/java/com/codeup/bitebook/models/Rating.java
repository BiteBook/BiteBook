package com.codeup.bitebook.models;

import jakarta.persistence.*;


@Entity
@Table(name="recipe_ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int value; // The numerical value representing the rating (e.g., 1, 2, 3, 4, or 5)

}
