package assessment.recipes.service;

import assessment.recipes.dto.RecipeCreateDTO;
import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.dto.specificationFilters.SearchRequest;

import java.util.List;

public interface RecipeService {

     ResponseDTO createRecipe(RecipeCreateDTO recipeDTO);

     ResponseDTO deleteRecipe(String recipeId);

     ResponseDTO updateRecipe(RecipeDTO recipeDTO);

    List<RecipeDTO> getRecipeByDynamicFilter(SearchRequest searchRequest);
}
