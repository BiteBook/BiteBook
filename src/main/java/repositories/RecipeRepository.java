package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
