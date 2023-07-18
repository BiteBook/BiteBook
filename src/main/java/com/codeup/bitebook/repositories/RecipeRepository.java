package com.codeup.bitebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
