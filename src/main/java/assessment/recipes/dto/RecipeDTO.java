package assessment.recipes.dto;

import assessment.recipes.entity.Recipe;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecipeDTO implements Serializable {
    private static final long serialVersionUID = 5848407641740953840L;
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
