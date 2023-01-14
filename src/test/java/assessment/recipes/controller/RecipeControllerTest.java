package assessment.recipes.controller;

import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.entity.Recipe;
import assessment.recipes.repository.RecipeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private RecipeRepository recipeRepository;

    @BeforeEach
    void setup(){
        recipeRepository.deleteAll();
    }

    @Test
    @DisplayName("When a recipe creation is requested then it is persisted")
    void recipeCreatedThenReturnRecipeIdInResponseDTO() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder()
                .recipeName("Hot-Dog")
                .ingredients("Oil, 2 Sausages, Tomato, Onion, Salt, 2 Hot dog bread, Ketchup, Mustard, Mayonnaise")
                .instructions("Pour the chopped onion after heating the requested amount of oil in a large pan and sauté until golden. " +
                        "Add the tomato paste (or tomato sauce), the salt and let it cook for 5 minutes. After that, " +
                        "add the sausages and let everything cook for another 5 minutes.\n" +
                        "Place a sausage on each bun and add sauce and extra ingredients to taste." +
                        " Afterwards, just enjoy your hot dog with the kids!\n")
                .isVegetarian(Boolean.FALSE)
                .numberServings(2).build();

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        post("/recipe/create")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(recipeDTO))).andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("response", notNullValue()))
                                .andExpect(jsonPath("message", equalTo("Recipe Created!")))
                                .andReturn()
                                .getResponse().getContentAsString(),
                        ResponseDTO.class);

        final Map<String, Long> map = mapper.convertValue(responseDTO.getResponse(),
                new TypeReference<>() {
                });

        Long recipeId = map.get("recipeID");
        assertEquals(responseDTO.getMessage(),"Recipe Created!");

        assertNotNull(
                recipeRepository
                        .findById(recipeId)
                        .orElseThrow(
                                () -> new IllegalStateException("Recipe not saved in the database")));
    }

    @Test
    @DisplayName("When a recipe creation is requested with invalid data then is not saved and then throws Exception")
    void recipeNotCreatedInvalidRecipeDTOThenReturnErrorInResponseDTO() throws Exception {
        RecipeDTO recipeDTO = RecipeDTO.builder()
                .recipeName("Hot-Dog")
                .isVegetarian(Boolean.FALSE)
                .numberServings(2).build();

        mockMvc
                .perform(
                        post("/recipe/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(recipeDTO)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(jsonPath("response", nullValue()))
                .andExpect(jsonPath("errors", notNullValue()));
    }

    @Test
    @DisplayName("When recive a delete request then delete the Recipe and is Persisted")
    void recipeDeletedByIdThenReturnMsgConfirmationInResponseDTO() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("A recipe Name")
                .ingredients("Some ingredientes")
                .instructions("A complex instruction")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        recipe = recipeRepository.save(recipe);

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        delete("/recipe/delete/{recipeId}", recipe.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("response", notNullValue()))
                                .andExpect(jsonPath("errors", empty())).andDo(print())
                                .andReturn().getResponse().getContentAsString(), ResponseDTO.class);

        assertEquals(responseDTO.getMessage(),"Recipe Deleted!");
    }

    @Test
    @DisplayName("When recive a delete request with invalid Recipe ID then return Exception")
    void recipeDeleteWithInvalidRecipeIdThenReturnException() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("A recipe Name")
                .ingredients("Some ingredientes")
                .instructions("A complex instruction")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        recipe = recipeRepository.save(recipe);

        long invalidRecipeId = 1L;

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        delete("/recipe/delete/{recipeId}", invalidRecipeId))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("response", nullValue()))
                                .andExpect(jsonPath("errors", notNullValue())).andDo(print())
                                .andReturn().getResponse().getContentAsString(), ResponseDTO.class);

        assertEquals(responseDTO.getMessage(),"Recipe not found");
    }







}
