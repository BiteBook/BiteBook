package com.codeup.bitebook.repositories;


import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.Review;
import com.codeup.bitebook.models.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRecipe(Recipe recipe);
}

