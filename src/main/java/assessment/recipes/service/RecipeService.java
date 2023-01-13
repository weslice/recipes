package assessment.recipes.service;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class RecipeService {

    private RecipeRepository recipeRepository;

    public ResponseDTO createRecipe(RecipeDTO recipeDTO) {
        try {
            var recipe = RecipeDTO.recipeBuilderDTOToEntity(recipeDTO);
            recipe = recipeRepository.save(recipe);
            return new ResponseDTO(Collections.singletonMap("recipeID", recipe.getId()),
                    Collections.emptyList(),
                    "Recipe Created!");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
