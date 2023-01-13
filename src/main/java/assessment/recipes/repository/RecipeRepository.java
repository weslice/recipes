package assessment.recipes.repository;

import assessment.recipes.entity.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipes, Long> {
}
