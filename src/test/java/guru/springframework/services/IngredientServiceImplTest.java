package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertNotNull;

public class IngredientServiceImplTest {

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientToIngredientCommand converter;

    @InjectMocks
    IngredientServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCommandById() {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        recipe.addIngredient(ingredient);
        Mockito.when(recipeService.getRecipeById(ArgumentMatchers.anyLong())).thenReturn(recipe);
        Mockito.when(converter.convert(ArgumentMatchers.any())).thenReturn(new IngredientCommand());
        IngredientCommand command = service.getCommandById(recipeId, ingredientId);
        assertNotNull(command);
        Mockito.verify(recipeService, Mockito.times(1)).getRecipeById(ArgumentMatchers.anyLong());
        Mockito.verify(converter, Mockito.times(1)).convert(ArgumentMatchers.any());
    }

}
