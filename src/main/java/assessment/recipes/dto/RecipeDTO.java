package assessment.recipes.dto;

import assessment.recipes.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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

    @Schema( type = "int", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema( type = "string", example = "Lemon Pie", requiredMode = Schema.RequiredMode.REQUIRED)
    private String recipeName;

    @Schema( type = "string", example = "4 lemons, flour, sugar, etc", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ingredients;

    @Schema( type = "string", example = "squeeze the lemon juice, oven, etc", requiredMode = Schema.RequiredMode.REQUIRED)
    private String instructions;

    @Schema( type = "int", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numberServings;

    @Schema( type = "boolean", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isVegetarian;


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
