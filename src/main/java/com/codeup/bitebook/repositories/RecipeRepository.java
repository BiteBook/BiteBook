package com.codeup.bitebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByTitleOrDietaryOrRegionOrDifficultyOrTime(String title, String dietary, String region, String difficulty, String time);
    List<Recipe> findByTitle(String title);
    List<Recipe> findByDietary(String dietary);
    List<Recipe> findByRegion(String region);
    List<Recipe> findByTime(String time);
    List<Recipe> findByDifficulty(String difficulty);

}
