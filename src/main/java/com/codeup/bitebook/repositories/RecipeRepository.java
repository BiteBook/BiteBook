package com.codeup.bitebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;
import org.springframework.stereotype.Repository;

@Repository

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
