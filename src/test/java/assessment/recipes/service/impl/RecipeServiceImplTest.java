package assessment.recipes.service.impl;

import assessment.recipes.dto.RecipeCreateDTO;
import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.specificationFilters.FilterRequest;
import assessment.recipes.dto.specificationFilters.SearchRequest;
import assessment.recipes.dto.specificationFilters.SearchSpecification;
import assessment.recipes.entity.Recipe;
import assessment.recipes.enumerator.FieldType;
import assessment.recipes.enumerator.Operator;
import assessment.recipes.exception.RecipeException;
import assessment.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


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

    private SearchRequest buildSearchRequest() {
        var filterRequest = new FilterRequest();
        filterRequest.setOperator(Operator.LIKE);
        filterRequest.setKey("instructions");
        filterRequest.setFieldType(FieldType.STRING);
        filterRequest.setValue("Oven");

        List<FilterRequest> filterRequestsList = new ArrayList<>();
        filterRequestsList.add(filterRequest);

        var searchRequest = new SearchRequest();
        searchRequest.setFilters(filterRequestsList);
        searchRequest.setSorts(new ArrayList<>());

        return searchRequest;
    }

    private Recipe buildRecipe() {
        return Recipe.builder()
                .id(1L)
                .numberServings(2)
                .isVegetarian(true)
                .recipeName("Recipe name")
                .instructions("Simple instru")
                .ingredients("Eggs, Soy")
                .build();
    }

    private RecipeCreateDTO createRecipeDTO() {
        RecipeCreateDTO recipeDTO = new RecipeCreateDTO();
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
    @DisplayName("Should create a recipe")
    void shouldCreateRecipe() {
        var recipeDTO = createRecipeDTO();
        Recipe recipeSave = new Recipe();
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeSave);
        var returnResponse = recipeServiceImpl.createRecipe(recipeDTO);
        assertEquals(returnResponse.getMessage(),"Recipe Created!");
        verify(recipeRepository, times(1)).save(Mockito.any(Recipe.class));
    }

    @Test
    @DisplayName("Should not create recipe and then throw RecipeException")
    void shouldNotCreateRecipeAndThenThrowRecipeException() {
        var recipeDTO = new RecipeCreateDTO();
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenThrow(RecipeException.class);
        assertThrows(RecipeException.class,
                () -> recipeServiceImpl.createRecipe(recipeDTO)
        );
    }

    @Test
    @DisplayName("Should delete a recipe by given recipe id")
    void shouldDeleteRecipe() {
        var recipeId = "1";
        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        var returnResponse = recipeServiceImpl.deleteRecipe(recipeId);
        Mockito.verify(recipeRepository, times(1)).delete(recipe);
        assertEquals(returnResponse.getMessage(),"Recipe Deleted!");
    }

    @Test
    @DisplayName("Should not delete a recipe invalid recipe id then throws exception")
    void shouldNotDeleteRecipeAndThrowsException() {
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        var msg = assertThrows(RecipeException.class,
                () -> recipeServiceImpl.deleteRecipe("1")
        ).getMessage();
        assertEquals(msg,"Recipe not found");
        Mockito.verify(recipeRepository, times(0)).delete(any(Recipe.class));
    }

    @Test
    @DisplayName("Should update a recipe by given id and then return RecipeResponseDTO with the updated values")
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
    @DisplayName("Should give recipe exception when a recipe update is requested with invalid recipe id")
    void shouldGiveRecipeExceptionWhenTryingToUpdateRecipeWithInvalidRecipeId() {
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
    @DisplayName("Should give recipe exception when a recipe update is requested with invalid recipe values")
    void shouldGiveRecipeExceptionWhenTryingToUpdateRecipeWithInvalidValues() {
        var recipeDTO = new RecipeDTO();
        Recipe recipe = new Recipe();
        recipeDTO.setId(1L);
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        Mockito.when(recipeRepository.save(recipe)).thenThrow(RecipeException.class);

        assertThrows(RecipeException.class,
                () -> recipeServiceImpl.updateRecipe(recipeDTO)
        );
        Mockito.verify(recipeRepository, times(0)).delete(any(Recipe.class));
    }

    @Test
    @DisplayName("Should get recipes by a dynamic filter")
    void shouldGetRecipesByFilterAndReturnListOfRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(buildRecipe());
        var searchRequest = buildSearchRequest();
        SearchSpecification<Recipe> specification = new SearchSpecification<>(searchRequest);
        lenient().when(recipeRepository.findAll(specification)).thenReturn(recipeList);
        recipeServiceImpl.getRecipeByDynamicFilter(searchRequest);
        verify(recipeRepository, times(1)).findAll((Specification<Recipe>) any());
    }

    @Test
    @DisplayName("Should throw recipe exception when try to get recipe by invalid filter")
    void shouldThrowExceptionWhenTryingToGetRecipesByInvalidFilter() {
        var searchRequest = buildSearchRequest();
        lenient().when(recipeRepository.findAll((Specification<Recipe>) any())).thenThrow(RecipeException.class);
        assertThrows(RecipeException.class,
                () -> recipeServiceImpl.getRecipeByDynamicFilter(searchRequest)
        );
    }

}