package assessment.recipes.service;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.exception.RecipeException;

import java.util.Collections;
import java.util.List;

public interface RecipeService {

     ResponseDTO createRecipe(RecipeDTO recipeDTO);

     ResponseDTO deleteRecipe(String recipeId);

     ResponseDTO updateRecipe(RecipeDTO recipeDTO);

     List<RecipeDTO> getRecipeByDynamicFilter();

}
