package assessment.recipes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recipes", indexes = @Index(columnList = "id"))
public class Recipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    @Column(name = "ingredients", nullable = false)
    private String ingredients;

    @Column(name = "instructions", nullable = false)
    private String instructions;

    @Column(name = "number_serving", nullable = false)
    private Integer numberServings;

    @Column(name = "vegetarian", nullable = false)
    private Boolean isVegetarian;


}
