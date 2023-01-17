package assessment.recipes.dto.specificationFilters;

import assessment.recipes.enumerator.FieldType;
import assessment.recipes.enumerator.Operator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterRequest implements Serializable {

    private static final long serialVersionUID = -3783122025820579098L;

    @Schema( type = "string", example = "ingredients", requiredMode = Schema.RequiredMode.REQUIRED)
    private String key;

    @Schema( type = "string", example = "LIKE", requiredMode = Schema.RequiredMode.REQUIRED)
    private Operator operator;

    @Schema( type = "string", example = "STRING", requiredMode = Schema.RequiredMode.REQUIRED)
    private FieldType fieldType;

    @Schema( type = "string", example = "flour", requiredMode = Schema.RequiredMode.REQUIRED)
    private transient Object value;

    @Schema( type = "string", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private transient Object valueTo;

    @Schema( type = "array", example = "[\"5.13\", \"5.8\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private transient List<Object> values;

}
