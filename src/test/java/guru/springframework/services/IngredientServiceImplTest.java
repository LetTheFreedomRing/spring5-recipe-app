package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IngredientServiceImplTest {

    private final IngredientCommandToIngredient ingredientCommandConverter =
            new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

    private final IngredientToIngredientCommand ingredientConverter =
            new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

    @Mock
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @InjectMocks
    IngredientServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new IngredientServiceImpl(recipeService, ingredientConverter, ingredientCommandConverter,
                recipeRepository, ingredientRepository, unitOfMeasureRepository);
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
        IngredientCommand command = service.getCommandById(recipeId, ingredientId);
        assertNotNull(command);
        Mockito.verify(recipeService, Mockito.times(1)).getRecipeById(ArgumentMatchers.anyLong());
    }

    @Test
    public void saveCommand() {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        IngredientCommand command = new IngredientCommand();
        command.setId(ingredientId);
        command.setRecipeId(recipeId);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(ingredientId);

        Mockito.when(recipeService.getRecipeById(ArgumentMatchers.anyLong())).thenReturn(new Recipe());
        Mockito.when(recipeRepository.save(ArgumentMatchers.any())).thenReturn(savedRecipe);

        IngredientCommand savedCommand = service.saveCommand(command);

        assertEquals(ingredientId, savedCommand.getId());
        Mockito.verify(recipeService, Mockito.times(1)).getRecipeById(ArgumentMatchers.anyLong());
        Mockito.verify(recipeRepository, Mockito.times(1)).save(ArgumentMatchers.any(Recipe.class));
    }

    @Test
    public void deleteById() {
        service.deleteById(1L);
        Mockito.verify(ingredientRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
    }

}
