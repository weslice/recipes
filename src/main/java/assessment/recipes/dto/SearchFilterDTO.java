package assessment.recipes.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFilterDTO {

    private String recipeName;

    private String includedIngredients;
    private String excludedIngredients;

    private String instructions;

    private Integer numberServings;

    private Boolean isVegetarian;

}
