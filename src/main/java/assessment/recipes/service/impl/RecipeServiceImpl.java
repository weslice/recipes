package assessment.recipes.service.impl;

import assessment.recipes.dto.RecipeCreateDTO;
import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.dto.specificationFilters.SearchRequest;
import assessment.recipes.dto.specificationFilters.SearchSpecification;
import assessment.recipes.entity.Recipe;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.repository.RecipeRepository;
import assessment.recipes.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;

    @Override
    public ResponseDTO create(RecipeCreateDTO recipeDTO) {
        try {
            var recipe = RecipeCreateDTO.recipeBuilderDTOToEntity(recipeDTO);
            recipe = recipeRepository.save(recipe);
            return new ResponseDTO(Collections.singletonMap("recipeID", recipe.getId()),
                    Collections.emptyList(),
                    "Recipe Created!");
        } catch (Exception e) {
            throw new RecipeException("Recipe not saved in the database");
        }
    }

    @Override
    public ResponseDTO delete(String recipeId) {
        var recipe = recipeRepository.findById(Long.valueOf(recipeId)).orElse(null);

        if (recipe == null) {
            throw new RecipeException("Recipe not found");
        }

        recipeRepository.delete(recipe);
        return new ResponseDTO(Collections.emptyList(),
                Collections.emptyList(),
                "Recipe Deleted!");
    }

    @Override
    public ResponseDTO update(RecipeDTO recipeDTO) {
        try {
            var recipeTobeUpdated = recipeRepository.findById(recipeDTO.getId()).orElse(null);

            if (recipeTobeUpdated == null) {
                throw new RecipeException("Recipe not found");
            }

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
            return recipeRepository
                    .findAll(specification)
                    .stream()
                    .map(RecipeDTO::convertEntityToDTO).collect(Collectors.toList());
        }  catch (Exception e) {
            throw new RecipeException(e.getMessage());
        }
    }
}
