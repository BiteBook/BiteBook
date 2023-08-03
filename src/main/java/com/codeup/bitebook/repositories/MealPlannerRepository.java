package com.codeup.bitebook.repositories;

import com.codeup.bitebook.models.MealPlanner;
import com.codeup.bitebook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealPlannerRepository extends JpaRepository<MealPlanner, Long> {
    List<MealPlanner> findByUser(User user);
}
