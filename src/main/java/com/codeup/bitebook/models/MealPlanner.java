package com.codeup.bitebook.models;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "mealplanner")
@Getter @Setter
public class MealPlanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerid;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipeid")
    private Recipe recipe;

    private LocalDate date;
}