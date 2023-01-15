package assessment.recipes.service.impl;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.entity.Recipe;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    private RecipeServiceImpl recipeServiceImpl;

    @Mock
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.recipeServiceImpl = new RecipeServiceImpl(recipeRepository);
    }

    private RecipeDTO createRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setRecipeName("Lemon Pie");
        recipeDTO.setIngredients("1 cup sugar, " +
                "3 tablespoons all-purpose flour, " +
                "salt, 2 cups water, " +
                "lemon juice " +
                "3 eggs, 1 tablespoon butter");
        recipeDTO.setInstructions("In a medium saucepan, combine sugar, cornstarch, flour and salt." +
                " Gradually stir in water." +
                " Cook and stir over medium heat until thickened and bubbly ...");
        recipeDTO.setNumberServings(4);
        recipeDTO.setIsVegetarian(Boolean.TRUE);
        return recipeDTO;
    }

    @Test
    void shouldCreateRecipe() {
        var recipeDTO = createRecipeDTO();
        Recipe recipeSave = new Recipe();
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeSave);
        var returnResponse = recipeServiceImpl.createRecipe(recipeDTO);
        assertEquals(returnResponse.getMessage(),"Recipe Created!");
        verify(recipeRepository, times(1)).save(Mockito.any(Recipe.class));
    }

    @Test
    void shouldNotCreateRecipeAndThenThrowRecipeException() {
        var recipeDTO = new RecipeDTO();
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenThrow(RecipeException.class);
        assertThrows(RecipeException.class,
                () -> {
                    recipeServiceImpl.createRecipe(recipeDTO);
                }
        );
    }

    @Test
    void shouldDeleteRecipe() {
        var recipeId = "1";
        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        var returnResponse = recipeServiceImpl.deleteRecipe(recipeId);
        Mockito.verify(recipeRepository, times(1)).delete(recipe);
        assertEquals(returnResponse.getMessage(),"Recipe Deleted!");
    }

    @Test
    void shouldNotDeleteRecipeAndThrowsException() {
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        var msg = assertThrows(RecipeException.class,
                () -> recipeServiceImpl.deleteRecipe("1")
        ).getMessage();
        assertEquals(msg,"Recipe not found");
        Mockito.verify(recipeRepository, times(0)).delete(any(Recipe.class));
    }

    @Test
    void shouldUpdateRecipeAndReturnRecipeResponseDTOWithTheUpdatedValues() {
        Recipe recipe = new Recipe();
        var recipeDTO = new RecipeDTO();
        recipeDTO.setId(1L);
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        Recipe recipeSave = new Recipe();
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeSave);
        var returnResponse = recipeServiceImpl.updateRecipe(recipeDTO);
        assertEquals(returnResponse.getMessage(),"Recipe Updated!");
        verify(recipeRepository, times(1)).save(Mockito.any(Recipe.class));
    }

    @Test
    void shouldGiveRecipeExeptionWhenTryingToUpdateRecipeWithInvalidRecipeId() {
        var recipeDTO = new RecipeDTO();
        recipeDTO.setId(1L);
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        var msg = assertThrows(RecipeException.class,
                () -> recipeServiceImpl.updateRecipe(recipeDTO)
        ).getMessage();
        assertEquals(msg,"Recipe not found");
        Mockito.verify(recipeRepository, times(0)).delete(any(Recipe.class));
    }

    @Test
    void shouldGiveRecipeExeptionWhenTryingToUpdateRecipeWithInvalidValues() {
        var recipeDTO = new RecipeDTO();
        Recipe recipe = new Recipe();
        recipeDTO.setId(1L);
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        Mockito.when(recipeRepository.save(recipe)).thenThrow(RecipeException.class);

        assertThrows(RecipeException.class,
                () -> recipeServiceImpl.updateRecipe(recipeDTO)
        ).getMessage();
        Mockito.verify(recipeRepository, times(0)).delete(any(Recipe.class));
    }

}