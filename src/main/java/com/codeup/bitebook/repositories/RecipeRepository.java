package com.codeup.bitebook.repositories;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT p FROM Recipe p " +
            "JOIN p.dietStyles ds " +
            "JOIN p.allergens a " +
            "WHERE (p.title LIKE :keyword " +
            "OR p.difficulty LIKE :keyword " +
            "OR p.time LIKE :keyword " +
            "OR p.region LIKE :keyword) " +
            "AND ds.id IN :dietStyleIds " +
            "AND a.id IN :allergenIds")
    List<Recipe> search(@Param("keyword") String keyword, @Param("dietStyleIds") List<Long> dietStyleIds, @Param("allergenIds") List<Long> allergenIds);
    List<Recipe> findByUser(User user);
    @Query(value = "SELECT * FROM recipes WHERE time <= 15 ORDER BY RAND()", nativeQuery = true)
    List<Recipe> findRandomRecipesReadyIn15Minutes(Pageable pageable);
    @Query("SELECT p FROM Recipe p " +
            "WHERE (p.title LIKE :keyword " +
            "OR p.difficulty LIKE :keyword " +
            "OR p.time LIKE :keyword " +
            "OR p.region LIKE :keyword)")
    List<Recipe> searchWithoutFilter(@Param("keyword") String keyword);


}

