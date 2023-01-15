package assessment.recipes.dto;

import assessment.recipes.entity.Recipe;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDTO implements Serializable {

    private Long id;
    private String recipeName;

    private String ingredients;

    private String instructions;

    private Integer numberServings;

    private Boolean isVegetarian;


    public static Recipe recipeBuilderDTOToEntity(RecipeDTO recipeDTO) {
        return Recipe.builder()
                .recipeName(recipeDTO.getRecipeName())
                .ingredients(recipeDTO.getIngredients())
                .instructions(recipeDTO.getInstructions())
                .numberServings(recipeDTO.getNumberServings())
                .isVegetarian(recipeDTO.getIsVegetarian()).build();
    }

    public static RecipeDTO convertEntityToDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .ingredients(recipe.getIngredients())
                .instructions(recipe.getInstructions())
                .numberServings(recipe.getNumberServings())
                .isVegetarian(recipe.getIsVegetarian()).build();
    }

}
