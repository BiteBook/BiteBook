package com.codeup.bitebook.repositories;

import com.codeup.bitebook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.bitebook.models.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT p FROM Recipe p WHERE p.title LIKE %:keyword%"
            + " OR p.difficulty LIKE %:keyword%"
            + " OR p.time LIKE %:keyword%"
            + " OR p.region LIKE %:keyword%"
            + "OR p.dietary LIKE %:keyword%")
    List<Recipe> search(@Param("keyword") String keyword);

    List<Recipe> findByUser(User user);



}


