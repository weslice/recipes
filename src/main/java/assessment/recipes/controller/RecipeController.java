package assessment.recipes.controller;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        } catch (RecipeException e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/delete/{recipeId}")
    public ResponseEntity<ResponseDTO> deleteRecipe(@PathVariable("recipeId") String recipeId) {
        try {
            return new ResponseEntity<>(recipeService.deleteRecipe(recipeId), HttpStatus.OK);
        } catch (RecipeException e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }




}
