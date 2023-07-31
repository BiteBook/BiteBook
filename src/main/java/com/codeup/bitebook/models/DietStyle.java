package com.codeup.bitebook.models;


import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "diet_styles")
@Data
@Getter @Setter
public class DietStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "dietStyles")
    private List<Recipe> recipes;
}