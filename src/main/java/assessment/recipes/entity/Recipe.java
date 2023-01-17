package assessment.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recipe", indexes = @Index(columnList = "id"))
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    @Column(name = "ingredients", nullable = false, columnDefinition="TEXT")
    private String ingredients;

    @Column(name = "instructions", nullable = false, columnDefinition="TEXT")
    private String instructions;

    @Column(name = "number_serving", nullable = false)
    private Integer numberServings;

    @Column(name = "vegetarian", nullable = false)
    private Boolean isVegetarian;

}
