package assessment.recipes.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@ToString
public class ResponseDTO implements Serializable {

    private static final long serialVersionUID = 6905214817937047932L;

    private Object response;
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PUBLIC)
    private List<String> errors;
    private String message;

    public ResponseDTO() {
        errors = new ArrayList<>();
    }

}
