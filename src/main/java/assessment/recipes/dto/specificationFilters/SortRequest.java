package assessment.recipes.dto.specificationFilters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SortRequest implements Serializable {

    private static final long serialVersionUID = 3194362295851723069L;

    @Schema( type = "string", example = "recipeName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String key;

    @Schema( type = "string", example = "DESC or ASC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private SortDirection direction;

}
