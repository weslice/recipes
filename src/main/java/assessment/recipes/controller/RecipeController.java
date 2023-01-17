package assessment.recipes.controller;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.dto.querry.SearchRequest;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.service.impl.RecipeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
public class RecipeController {

    private RecipeServiceImpl recipeServiceImpl;

    @PostMapping(value = "/create")
    public ResponseEntity<ResponseDTO> createRecipe(@RequestBody RecipeDTO recipe) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.createRecipe(recipe), HttpStatus.CREATED);
        } catch (RecipeException e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/delete/{recipeId}")
    public ResponseEntity<ResponseDTO> deleteRecipe(@PathVariable("recipeId") String recipeId) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.deleteRecipe(recipeId), HttpStatus.OK);
        } catch (RecipeException e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ResponseDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.updateRecipe(recipeDTO), HttpStatus.OK);
        } catch (RecipeException re) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(re.getMessage()), re.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/filter")
    public ResponseEntity<?> getRecipesByFilters(@RequestBody SearchRequest searchRequest) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.getRecipeByDynamicFilter(searchRequest), HttpStatus.OK);
        } catch (RecipeException re) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(re.getMessage()), re.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
