package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.NotesCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

public class RecipeCommandToRecipeTest {

    RecipeCommandToRecipe converter;

    static final Long RECIPE_ID = 1L;
    static final Integer RECIPE_COOK_TIME = 1;
    static final Integer RECIPE_PREP_TIME = 1;
    static final Integer RECIPE_SERVINGS = 1;
    static final String RECIPE_DIRECTIONS = "dummy";
    static final Difficulty RECIPE_DIFFICULTY = Difficulty.EASY;
    static final String RECIPE_DESCRIPTION = "dummy";
    static final String RECIPE_SOURCE = "dummy";
    static final String RECIPE_URL = "dummy";

    static final Long INGREDIENT_1_ID = 1L;
    static final Long INGREDIENT_2_ID = 2L;

    static final Long NOTES_ID = 1L;

    static final Long CATEGORY_1_ID = 1L;
    static final Long CATEGORY_2_UD = 2L;

    static IngredientCommand ingredient1;
    static IngredientCommand ingredient2;

    static CategoryCommand category1;
    static CategoryCommand category2;

    static NotesCommand notes;

    @Before
    public void setUp() throws Exception {
        converter = new RecipeCommandToRecipe(new CategoryCommandToCategory(),
                new NotesCommandToNotes(),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()));
        ingredient1 = new IngredientCommand();
        ingredient1.setId(INGREDIENT_1_ID);
        ingredient2 = new IngredientCommand();
        ingredient2.setId(INGREDIENT_2_ID);
        category1 = new CategoryCommand();
        category1.setId(CATEGORY_1_ID);
        category2 = new CategoryCommand();
        category2.setId(CATEGORY_2_UD);
        notes = new NotesCommand();
        notes.setId(NOTES_ID);
    }

    @Test
    @Transactional
    public void convert() {
        RecipeCommand command = new RecipeCommand();
        command.setId(RECIPE_ID);
        command.setCookTime(RECIPE_COOK_TIME);
        command.setDifficulty(RECIPE_DIFFICULTY);
        command.setDescription(RECIPE_DESCRIPTION);
        command.setPrepTime(RECIPE_PREP_TIME);
        command.setServings(RECIPE_SERVINGS);
        command.setSource(RECIPE_SOURCE);
        command.setUrl(RECIPE_URL);
        command.setDirections(RECIPE_DIRECTIONS);
        command.getIngredients().add(ingredient1);
        command.getIngredients().add(ingredient2);
        command.setNotes(notes);
        command.getCategories().add(category1);
        command.getCategories().add(category2);

        Recipe actual = converter.convert(command);
        assertNotNull(actual);
        assertNotNull(actual.getCategories());
        assertNotNull(actual.getIngredients());
        assertNotNull(actual.getNotes());
        assertEquals(RECIPE_ID, actual.getId());
        assertEquals(RECIPE_COOK_TIME, actual.getCookTime());
        assertEquals(RECIPE_DESCRIPTION, actual.getDescription());
        assertEquals(RECIPE_DIFFICULTY, actual.getDifficulty());
        assertEquals(RECIPE_PREP_TIME, actual.getPrepTime());
        assertEquals(RECIPE_SERVINGS, actual.getServings());
        assertEquals(RECIPE_SOURCE, actual.getSource());
        assertEquals(RECIPE_URL, actual.getUrl());
        assertEquals(RECIPE_DIRECTIONS, actual.getDirections());
        assertEquals(NOTES_ID, actual.getNotes().getId());
        assertEquals(2, actual.getIngredients().size());
        assertEquals(2, actual.getCategories().size());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}