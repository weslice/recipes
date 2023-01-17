package assessment.recipes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchFilterDTO implements Serializable {

    private static final long serialVersionUID = -933472689793336889L;
    private String recipeName;

    private String includedIngredients;

    private String excludedIngredients;

    private String instructions;

    private Integer numberServings;

    private Boolean isVegetarian;

}
