package com.codeup.bitebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT p FROM Recipe p WHERE CONCAT(p.title, ' ', p.dietary, ' ', p.region, ' ', p.difficulty, ' ', p.time) LIKE %?1%")
    List<Recipe> search(String keyword);
//    List<Recipe> findAllByTitleOrDietaryOrRegionOrDifficultyOrTime(String title, String dietary, String region, String difficulty, String time);

    List<Recipe> findByTitle(String title);
    List<Recipe> findByDietary(String dietary);
    List<Recipe> findByRegion(String region);
    List<Recipe> findByTime(String time);
    List<Recipe> findByDifficulty(String difficulty);


}
