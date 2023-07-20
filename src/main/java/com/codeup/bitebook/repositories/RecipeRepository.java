package com.codeup.bitebook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT p FROM Recipe p WHERE "
            + "CONCAT (p.title, p.difficulty, p.region, p.dietary)"
            + " LIKE %?1%")
    List<Recipe> findAll(String keyword);
}
