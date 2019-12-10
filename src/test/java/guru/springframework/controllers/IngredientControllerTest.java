package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

public class IngredientControllerTest {

    @Mock
    IngredientService ingredientService;

    @Mock
    RecipeService recipeService;

    @Mock
    UnitOfMeasureService uomService;

    @InjectMocks
    IngredientController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getIngredients() throws Exception {
        Long recipeId = 1L;
        Mockito.when(recipeService.getCommandById(ArgumentMatchers.anyLong())).thenReturn(new RecipeCommand());
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
        Mockito.verify(recipeService, Mockito.times(1)).getCommandById(ArgumentMatchers.anyLong());
    }

    @Test
    public void showIngredient() throws Exception {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        IngredientCommand ingredientCommand = new IngredientCommand();
        Mockito.when(ingredientService.getCommandById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/ingredient/" + ingredientId + "/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"));
        Mockito.verify(ingredientService, Mockito.times(1)).getCommandById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
    }

    @Test
    public void updateIngredient() throws Exception {
        Mockito.when(ingredientService.getCommandById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(new IngredientCommand());
        Mockito.when(uomService.listAllUoms()).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/1/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient", "uomList"));
    }

    @Test
    public void saveOrUpdate() throws Exception {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(recipeId);
        command.setId(ingredientId);

        Mockito.when(ingredientService.saveCommand(ArgumentMatchers.any())).thenReturn(command);
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe/1/ingredient"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/" + recipeId + "/ingredient/" + ingredientId + "/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"));
        Mockito.verify(ingredientService, Mockito.times(1)).saveCommand(ArgumentMatchers.any());
    }

    @Test
    public void newIngredient() throws Exception {
        Long recipeID = 1L;
        RecipeCommand command = new RecipeCommand();
        command.setId(recipeID);

        Mockito.when(recipeService.getCommandById(ArgumentMatchers.anyLong())).thenReturn(command);
        Mockito.when(uomService.listAllUoms()).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeID + "/ingredient/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient", "uomList"));
        Mockito.verify(recipeService, Mockito.times(1)).getCommandById(ArgumentMatchers.anyLong());
    }
}
