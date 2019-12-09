package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RecipeToRecipeCommandTest {

    RecipeToRecipeCommand converter;

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

    static Ingredient ingredient1;
    static Ingredient ingredient2;

    static Category category1;
    static Category category2;

    static Notes notes;

    @Before
    public void setUp() throws Exception {
        converter = new RecipeToRecipeCommand(new CategoryToCategoryCommand(),
                new NotesToNotesCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()));
        ingredient1 = new Ingredient();
        ingredient1.setId(INGREDIENT_1_ID);
        ingredient2 = new Ingredient();
        ingredient2.setId(INGREDIENT_2_ID);
        category1 = new Category();
        category1.setId(CATEGORY_1_ID);
        category2 = new Category();
        category2.setId(CATEGORY_2_UD);
        notes = new Notes();
        notes.setId(NOTES_ID);
    }

    @Test
    @Transactional
    public void convert() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setCookTime(RECIPE_COOK_TIME);
        recipe.setDifficulty(RECIPE_DIFFICULTY);
        recipe.setDescription(RECIPE_DESCRIPTION);
        recipe.setPrepTime(RECIPE_PREP_TIME);
        recipe.setServings(RECIPE_SERVINGS);
        recipe.setSource(RECIPE_SOURCE);
        recipe.setUrl(RECIPE_URL);
        recipe.setDirections(RECIPE_DIRECTIONS);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.setNotes(notes);
        recipe.getCategories().add(category1);
        recipe.getCategories().add(category2);

        RecipeCommand actual = converter.convert(recipe);
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