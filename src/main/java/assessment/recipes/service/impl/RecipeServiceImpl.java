package assessment.recipes.service.impl;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.dto.querry.SearchRequest;
import assessment.recipes.dto.querry.SearchSpecification;
import assessment.recipes.entity.Recipe;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.repository.RecipeRepository;
import assessment.recipes.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;

    @Override
    public ResponseDTO createRecipe(RecipeDTO recipeDTO) {
        try {
            var recipe = RecipeDTO.recipeBuilderDTOToEntity(recipeDTO);
            recipe = recipeRepository.save(recipe);
            return new ResponseDTO(Collections.singletonMap("recipeID", recipe.getId()),
                    Collections.emptyList(),
                    "Recipe Created!");
        } catch (Exception e) {
            throw new RecipeException(e.getMessage());
        }
    }

    @Override
    public ResponseDTO deleteRecipe(String recipeId) {
        var recipe = recipeRepository.findById(Long.valueOf(recipeId)).orElse(null);
        if (recipe == null)
            throw new RecipeException("Recipe not found");

        recipeRepository.delete(recipe);
        return new ResponseDTO(Collections.emptyList(),
                Collections.emptyList(),
                "Recipe Deleted!");
    }

    @Override
    public ResponseDTO updateRecipe(RecipeDTO recipeDTO) {
        try {
            var recipeTobeUpdated = recipeRepository.findById(recipeDTO.getId()).orElse(null);
            if (recipeTobeUpdated == null)
                throw new RecipeException("Recipe not found");

            recipeTobeUpdated.setRecipeName(recipeDTO.getRecipeName());
            recipeTobeUpdated.setNumberServings(recipeDTO.getNumberServings());
            recipeTobeUpdated.setIsVegetarian(recipeDTO.getIsVegetarian());
            recipeTobeUpdated.setIngredients(recipeDTO.getIngredients());
            recipeTobeUpdated.setInstructions(recipeDTO.getInstructions());

            recipeTobeUpdated = recipeRepository.save(recipeTobeUpdated);

            return new ResponseDTO(RecipeDTO.convertEntityToDTO(recipeTobeUpdated),
                    Collections.emptyList(),
                    "Recipe Updated!");
        }  catch (Exception e) {
            throw new RecipeException(e.getMessage());
        }
    }

    @Override
    public List<RecipeDTO> getRecipeByDynamicFilter(SearchRequest searchRequest) {
        try {
            SearchSpecification<Recipe> specification = new SearchSpecification<>(searchRequest);
            var recipeList = recipeRepository.findAll(specification);
            List<RecipeDTO> returnRecipeList = new ArrayList<>();
            recipeList.forEach(recipe -> returnRecipeList.add(RecipeDTO.convertEntityToDTO(recipe)));
            return returnRecipeList;
        }  catch (Exception e) {
            throw new RecipeException(e.getMessage());
        }
    }
}
