package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class RecipeControllerTest {

    private static final Long RECIPE_ID = 4L;
    private static final String RECIPE_DESCRIPTION = "dummy";
    private static final Integer RECIPE_COOK_TIME = 10;
    private static final Integer RECIPE_PREP_TIME = 10;
    private static final Integer RECIPE_SERVINGS = 10;
    private static final String RECIPE_URL = "https://www.example.com";
    private static final String RECIPE_DIRECTIONS = "dummy";

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void showById() throws Exception {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        Mockito.when(recipeService.getRecipeById(ArgumentMatchers.anyLong())).thenReturn(recipe);
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/recipe/" + recipeId + "/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"))
                .andExpect(MockMvcResultMatchers.model().attribute("recipe", recipe));
    }

    @Test
    public void showByIdNotFound() throws Exception {
        Mockito.when(recipeService.getRecipeById(ArgumentMatchers.anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/{recipeId}/show",  RECIPE_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void showByIdBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/{recipeId}/show",  "bad request"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void newRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/new/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"));
    }

    @Test
    public void saveOrUpdate() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(RECIPE_ID);
        Mockito.when(recipeService.saveRecipeCommand(ArgumentMatchers.any())).thenReturn(command);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/recipe/")
                .param("cookTime", String.valueOf(RECIPE_COOK_TIME))
                .param("prepTime", String.valueOf(RECIPE_PREP_TIME))
                .param("servings", String.valueOf(RECIPE_SERVINGS))
                .param("directions", RECIPE_DIRECTIONS)
                .param("description", RECIPE_DESCRIPTION)
                .param("url", RECIPE_URL)
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/" + RECIPE_ID + "/show"));
        Mockito.verify(recipeService, Mockito.times(1)).saveRecipeCommand(ArgumentMatchers.any());
    }

    @Test
    public void saveOrUpdateFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/recipe/")
                .param("servings", String.valueOf(RECIPE_SERVINGS))
                .param("directions", RECIPE_DIRECTIONS)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"));
        Mockito.verifyZeroInteractions(recipeService);
    }

    @Test
    public void updateRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(RECIPE_ID);
        Mockito.when(recipeService.getCommandById(ArgumentMatchers.any())).thenReturn(command);
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + RECIPE_ID + "/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
                .andExpect(MockMvcResultMatchers.model().attribute("recipe", command));
        Mockito.verify(recipeService, Mockito.times(1)).getCommandById(ArgumentMatchers.anyLong());
    }

    @Test
    public void deleteRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + RECIPE_ID + "/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
        Mockito.verify(recipeService, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
    }
}