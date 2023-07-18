package com.codeup.bitebook.repositories;

import com.codeup.bitebook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}