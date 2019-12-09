package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
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

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void showById() throws Exception {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        Mockito.when(recipeService.getRecipeById(ArgumentMatchers.anyLong())).thenReturn(recipe);
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/recipe/show/" + recipeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"))
                .andExpect(MockMvcResultMatchers.model().attribute("recipe", recipe));
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
                .post("/recipe/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/show/" + RECIPE_ID));
        Mockito.verify(recipeService, Mockito.times(1)).saveRecipeCommand(ArgumentMatchers.any());
    }
}