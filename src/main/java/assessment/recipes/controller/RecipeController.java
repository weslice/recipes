package assessment.recipes.controller;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @PostMapping(value = "/create")
    public ResponseEntity<ResponseDTO> createRecipe(@RequestBody RecipeDTO recipe) {
        try {
            return new ResponseEntity<>(recipeService.createRecipe(recipe), HttpStatus.CREATED);
        } catch (Exception e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


}
