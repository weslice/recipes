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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest implements Serializable {

    private static final long serialVersionUID = 8514625832019794838L;

    @Schema(type = "array", example = "[{\"key\":\"instructions\",\"operator\":\"LIKE\",\"field_type\":\"STRING\",\"value\":\"Oven\"}]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FilterRequest> filters;

    @Schema(type = "array", example = "[{\"key\":\"recipeName\",\"direction\":\"DESC\"}]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SortRequest> sorts;

    public List<FilterRequest> getFilters() {
        if (Objects.isNull(this.filters)) return new ArrayList<>();
        return this.filters;
    }

    public List<SortRequest> getSorts() {
        if (Objects.isNull(this.sorts)) return new ArrayList<>();
        return this.sorts;
    }

}
