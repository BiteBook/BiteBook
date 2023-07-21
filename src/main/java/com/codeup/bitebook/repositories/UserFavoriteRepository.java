package com.codeup.bitebook.repositories;

import com.codeup.bitebook.models.User;
import com.codeup.bitebook.models.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    List<UserFavorite> findByUser(com.codeup.bitebook.models.User user);
}

