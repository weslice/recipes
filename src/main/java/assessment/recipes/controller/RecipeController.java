package assessment.recipes.controller;

import assessment.recipes.dto.RecipeCreateDTO;
import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.dto.specificationFilters.SearchRequest;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.service.impl.RecipeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Insert a recipe in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe Created!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Recipe not saved in the database",
                    content = @Content)})
    @PostMapping(value = "/create")
    public ResponseEntity<ResponseDTO> createRecipe(@RequestBody RecipeCreateDTO recipe) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.createRecipe(recipe), HttpStatus.CREATED);
        } catch (RecipeException e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a recipe from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe Deleted!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Recipe not found",
                    content = @Content)})
    @DeleteMapping(value = "/delete/{recipeId}")
    public ResponseEntity<ResponseDTO> deleteRecipe(@Parameter(
            description = "ID of recipe that needs to be deleted",
            schema = @Schema(
                    type = "integer",
                    format = "int64",
                    description = "ID of recipe that needs to be deleted",
                    defaultValue = "1, 2, 3, ..."),
            required = true)
                                                     @PathVariable("recipeId") String recipeId) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.deleteRecipe(recipeId), HttpStatus.OK);
        } catch (RecipeException e) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(e.getMessage()), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update a recipe in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe Updated!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Recipe not found",
                    content = @Content)})
    @PatchMapping(value = "/update")
    public ResponseEntity<ResponseDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            return new ResponseEntity<>(recipeServiceImpl.updateRecipe(recipeDTO), HttpStatus.OK);
        } catch (RecipeException re) {
            var errorResponse = new ResponseDTO(null, Collections.singletonList(re.getMessage()), re.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get a recipe in the database from filter request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400",
                    content = @Content)})
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
