package com.codeup.bitebook.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "recipes")
@Getter @Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeid;

    private String title;
    private String ingredients;
    private String instructions;
    private String tools;
    private String photo;
    private String difficulty;
    private Integer time;
    private String region;
    private String dietary;

    @ManyToOne
    private User user;
}
