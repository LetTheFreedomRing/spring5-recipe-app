package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeConverter;

    @Mock
    RecipeCommandToRecipe recipeCommandConverter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandConverter, recipeConverter);
    }

    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        Set<Recipe> set = new HashSet<>();
        set.add(recipe);

        Mockito.when(recipeRepository.findAll()).thenReturn(set);

        Set<Recipe> recipes = recipeService.getAllRecipes();

        assertEquals(1, recipes.size());
        Mockito.verify(recipeRepository, Mockito.times(1)).findAll();
        assertTrue(recipes.contains(recipe));
    }

    @Test
    public void getRecipeById() {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        Mockito.when(recipeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(recipe));

        Recipe foundRecipe = recipeService.getRecipeById(recipeId);
        assertEquals(recipeId, foundRecipe.getId());
    }

    @Test
    public void getCommandById() {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        RecipeCommand command = new RecipeCommand();
        command.setId(recipeId);
        Mockito.when(recipeConverter.convert(ArgumentMatchers.any())).thenReturn(command);
        Mockito.when(recipeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(recipe));

        RecipeCommand foundCommand = recipeService.getCommandById(recipeId);
        assertEquals(recipeId, foundCommand.getId());
        Mockito.verify(recipeRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
    }

    @Test
    public void deleteById() {
        Long id = 2L;
        recipeService.deleteById(id);

        Mockito.verify(recipeRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());

    }

}