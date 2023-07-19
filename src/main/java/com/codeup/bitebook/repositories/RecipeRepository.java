package com.codeup.bitebook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE r.title LIKE %?1%"
    + " OR r.difficulty LIKE %?1%"
//    + " OR r.time LIKE %?1%"
    )
    List<Recipe> findAll(String keyword);
}
