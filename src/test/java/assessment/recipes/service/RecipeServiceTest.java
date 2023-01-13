package assessment.recipes.service;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.entity.Recipe;
import assessment.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private  RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.recipeService = new RecipeService(recipeRepository);
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
        var returnResponse = recipeService.createRecipe(recipeDTO);
        assertNotNull(returnResponse.getMessage(), () -> "Recipe Created!");
        verify(recipeRepository, times(1)).save(Mockito.any(Recipe.class));
    }

}