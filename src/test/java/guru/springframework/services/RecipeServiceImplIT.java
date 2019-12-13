package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.*;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceImplIT {

    private static final Long RECIPE_ID = 1L;
    private static final String RECIPE_DESCRIPTION = "dummy";
    private static final String RECIPE_DIRECTIONS = "dummy";


    @Autowired
    RecipeCommandToRecipe recipeCommandConverter;

    @Autowired
    RecipeToRecipeCommand recipeConverter;

    @Autowired
    RecipeRepository repository;

    @Autowired
    RecipeService service;

    private RecipeCommand recipeCommand;


    @Test
    @Transactional
    public void saveOrUpdate() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setDescription(RECIPE_DESCRIPTION);
        recipeCommand.setDirections(RECIPE_DIRECTIONS);
        RecipeCommand savedCommand = service.saveRecipeCommand(recipeCommand);
        assertNotNull(savedCommand);
        assertEquals(RECIPE_ID, savedCommand.getId());
    }

    @Test
    @Transactional
    public void updateDescription() {
        Set<Recipe> recipes = service.getAllRecipes();
        Recipe recipe = recipes.iterator().next();
        RecipeCommand command = recipeConverter.convert(recipe);
        command.setDescription(RECIPE_DESCRIPTION);
        command.setDirections(RECIPE_DIRECTIONS);
        RecipeCommand savedCommand = service.saveRecipeCommand(command);
        assertEquals(command.getId(), savedCommand.getId());
        assertEquals(command.getCategories().size(), savedCommand.getCategories().size());
        assertEquals(command.getIngredients().size(), savedCommand.getIngredients().size());
        assertEquals(RECIPE_DESCRIPTION, savedCommand.getDescription());
    }

}
