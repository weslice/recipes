package assessment.recipes.controller;

import assessment.recipes.dto.RecipeCreateDTO;
import assessment.recipes.dto.RecipeDTO;
import assessment.recipes.dto.ResponseDTO;
import assessment.recipes.dto.specificationFilters.FilterRequest;
import assessment.recipes.dto.specificationFilters.SearchRequest;
import assessment.recipes.entity.Recipe;
import assessment.recipes.enumerator.FieldType;
import assessment.recipes.enumerator.Operator;
import assessment.recipes.repository.RecipeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private RecipeRepository recipeRepository;

    @LocalServerPort
    private int port;

    String baseUrl = "http://localhost:";
    @BeforeEach
    void setup() {
        baseUrl = baseUrl + port;
        log.info("application started with base URL:{} and port:{}", baseUrl, port);
        recipeRepository.deleteAll();
    }

    private SearchRequest buildSearchRequest() {
        var filterRequest = new FilterRequest();
        filterRequest.setOperator(Operator.LIKE);
        filterRequest.setKey("ingredients");
        filterRequest.setFieldType(FieldType.STRING);
        filterRequest.setValue("Banana");

        List<FilterRequest> filterRequestsList = new ArrayList<>();
        filterRequestsList.add(filterRequest);

        filterRequest.setOperator(Operator.EQUAL);
        filterRequest.setKey("isVegetarian");
        filterRequest.setFieldType(FieldType.BOOLEAN);
        filterRequest.setValue(true);
        filterRequestsList.add(filterRequest);

        var searchRequest = new SearchRequest();
        searchRequest.setFilters(filterRequestsList);
        searchRequest.setSorts(new ArrayList<>());

        return searchRequest;
    }
    private SearchRequest buildInvalidSearchRequest() {
        var filterRequest = new FilterRequest();
        filterRequest.setOperator(Operator.LIKE);
        filterRequest.setKey("ingredientsx");
        filterRequest.setFieldType(FieldType.STRING);
        filterRequest.setValue("milk");

        List<FilterRequest> filterRequestsList = new ArrayList<>();
        filterRequestsList.add(filterRequest);

        var searchRequest = new SearchRequest();
        searchRequest.setFilters(filterRequestsList);
        searchRequest.setSorts(new ArrayList<>());

        return searchRequest;
    }


    @Test
    @DisplayName("When a recipe creation is requested then it is persisted")
    void recipeCreatedThenReturnRecipeIdInResponseDTO() throws Exception {
        RecipeCreateDTO recipeDTO = RecipeCreateDTO.builder()
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
        RecipeCreateDTO recipeDTO = RecipeCreateDTO.builder()
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
    @DisplayName("When receive a delete request then delete the Recipe and is Persisted")
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
    @DisplayName("When receive a delete request with invalid Recipe ID then return Exception")
    void recipeDeleteWithInvalidRecipeIdThenReturnException() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("A recipe Name")
                .ingredients("Some ingredients")
                .instructions("A complex instruction")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        recipeRepository.save(recipe);

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

    @Test
    @DisplayName("When receive an PUT request to update recipe then it is persisted")
    void recipePutRequestUpdateRecipeThenPersist() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("A recipe Name")
                .ingredients("Some ingredients")
                .instructions("A complex instruction")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        recipe = recipeRepository.save(recipe);

        RecipeDTO recipeNewValues = RecipeDTO.builder()
                .id(recipe.getId())
                .recipeName("Another recipe name")
                .isVegetarian(Boolean.FALSE)
                .instructions("Update the complex instruction")
                .numberServings(6)
                .ingredients("Add more ingredients")
                .build();

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        patch("/recipe/update")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(recipeNewValues))).andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("response", notNullValue()))
                                .andExpect(jsonPath("message", equalTo("Recipe Updated!")))
                                .andReturn()
                                .getResponse().getContentAsString(),
                        ResponseDTO.class);

        var recipeDtoUpdated = mapper.convertValue(responseDTO.getResponse(), RecipeDTO.class);

        assertEquals(recipeDtoUpdated.getId(), recipe.getId());
        assertEquals(recipeDtoUpdated.getRecipeName(), recipeNewValues.getRecipeName());
        assertEquals(recipeDtoUpdated.getInstructions(), recipeNewValues.getInstructions());
    }
    @Test
    @DisplayName("When receive an Patch request to update with invalid Recipe ID then return Exception")
    void recipePatchRequestUpdateRecipeWithInvalidIdThenReturnException() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("A recipe Name")
                .ingredients("Some ingredients")
                .instructions("A complex instruction")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        recipeRepository.save(recipe);

        RecipeDTO recipeNewValues = RecipeDTO.builder()
                .id(1L)
                .recipeName("Another recipe name")
                .isVegetarian(Boolean.FALSE)
                .instructions("Update the complex instruction")
                .numberServings(6)
                .ingredients("Add more ingredients")
                .build();

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        patch("/recipe/update")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(recipeNewValues)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("response", nullValue()))
                                .andExpect(jsonPath("errors", notNullValue())).andDo(print())
                                .andReturn()
                                .getResponse().getContentAsString(),
                        ResponseDTO.class);
        assertEquals(responseDTO.getMessage(),"Recipe not found");
    }

    @Test
    @DisplayName("When receive an Patch request to update with invalid Data Values then return Exception")
    void recipePatchRequestUpdateRecipeWithInvalidDataValuesThenReturnException() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("A recipe Name")
                .ingredients("Some ingredients")
                .instructions("A complex instruction")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        recipe = recipeRepository.save(recipe);

        RecipeDTO recipeNewValues = RecipeDTO.builder()
                .id(recipe.getId())
                .recipeName(null)
                .ingredients("Add more ingredients")
                .build();

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        patch("/recipe/update")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(recipeNewValues))).andDo(print())
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("response", nullValue()))
                                .andExpect(jsonPath("errors", notNullValue())).andDo(print())
                                .andReturn()
                                .getResponse().getContentAsString(),
                        ResponseDTO.class);
//        assertEquals(responseDTO.getMessage(),"not-null property references a null or transient value : assessment.recipes.entity.Recipe.instructions");
        assertEquals(responseDTO.getMessage(),"could not execute statement; SQL [n/a]; constraint [null]");
    }

    @Test
    @DisplayName("When receive an Post request to filter recipes by a dynamic filter then return data")
    void recipePostRequestGetRecipeByFilterThenReturnRecipeData() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("Lemon Pie")
                .ingredients("Lemon, Milk, Flour")
                .instructions("Put in the oven for 25 minutes")
                .isVegetarian(Boolean.TRUE)
                .numberServings(3)
                .build();

        Recipe recipe2 = Recipe.builder()
                .recipeName("Banana Pancake")
                .ingredients("Banana, Flour, Oil")
                .instructions("in a fridge ...")
                .isVegetarian(Boolean.TRUE)
                .numberServings(1)
                .build();


        recipe = recipeRepository.save(recipe);
        recipe2 = recipeRepository.save(recipe2);

        SearchRequest searchRequest = buildSearchRequest();


        List<RecipeDTO> responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        post("/recipe/filter")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(searchRequest))).andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andReturn()
                                .getResponse().getContentAsString(),
                        ArrayList.class);

        responseDTO.size();
        Gson gson = new Gson();

        String listString = gson.toJson(
                responseDTO,
                new TypeReference<List<RecipeDTO>>() {}.getType());

        List<RecipeDTO> recipeDTOList = mapper.readValue(listString, new TypeReference<>() {
        });

        assertEquals(recipeDTOList.get(0).getId(), recipe.getId());
        assertEquals(recipeDTOList.get(1).getId(), recipe2.getId());
        assertEquals(recipeDTOList.get(0).getRecipeName(), recipe.getRecipeName());
        assertEquals(recipeDTOList.get(0).getInstructions(), recipe.getInstructions());
        assertEquals(recipeDTOList.get(1).getRecipeName(), recipe2.getRecipeName());
        assertEquals(recipeDTOList.get(1).getInstructions(), recipe2.getInstructions());
    }

    @Test
    @DisplayName("When receive an Post request to filter recipes with a invalid attribute in dynamic filter then throws Exception")
    void recipePostRequestGetRecipeByInvalidFilterThenThrowsException() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeName("Stamppot")
                .ingredients("potatoes, salt, butter, milk, extra virgin olive oil, etc...")
                .instructions("Put potatoes and 2 teaspoons salt in a large pot. Cover with cold water. Bring to a boil over high heat. Reduce to a simmer and cook until potatoes are tender, 10-15 minutes")
                .isVegetarian(Boolean.FALSE)
                .numberServings(4)
                .build();

        recipeRepository.save(recipe);

        SearchRequest invaslidSearchRequest = buildInvalidSearchRequest();

        ResponseDTO responseDTO = mapper
                .readValue(
                        mockMvc
                                .perform(
                                        post("/recipe/filter")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(mapper.writeValueAsString(invaslidSearchRequest)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("response", nullValue()))
                                .andExpect(jsonPath("errors", notNullValue())).andDo(print())

                                .andReturn()
                                .getResponse().getContentAsString(),
                        ResponseDTO.class);

        assertEquals(responseDTO.getMessage(),"org.hibernate.query.SemanticException: Could not resolve attribute 'ingredientsx' of 'assessment.recipes.entity.Recipe'");
    }


}
