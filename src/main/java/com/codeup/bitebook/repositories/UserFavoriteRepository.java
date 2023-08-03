package com.codeup.bitebook.repositories;

import com.codeup.bitebook.models.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    List<UserFavorite> findByUser(com.codeup.bitebook.models.User user);
    @Query("SELECT uf.recipeId, COUNT(uf) FROM UserFavorite uf GROUP BY uf.recipeId ORDER BY COUNT(uf) DESC")
    List<Object[]> findTop10MostSavedRecipes(Pageable pageable);


}


