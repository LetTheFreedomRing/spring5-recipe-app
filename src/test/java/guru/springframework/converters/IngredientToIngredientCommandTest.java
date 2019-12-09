package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.model.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientToIngredientCommandTest {

    IngredientToIngredientCommand converter;

    static final Long INGREDIENT_ID = 1L;
    static final String INGREDIENT_DESCRIPTION = "dummy";
    static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(1);

    static final Long RECIPE_ID = 1L;

    static final Long UOM_ID = 2L;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    @Transactional
    public void convert() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setRecipe(recipe);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(INGREDIENT_AMOUNT);
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        ingredient.setUom(uom);
        IngredientCommand actual = converter.convert(ingredient);
        assertNotNull(actual);
        assertNotNull(actual.getUom());
        assertEquals(INGREDIENT_ID, actual.getId());
        assertEquals(INGREDIENT_AMOUNT, actual.getAmount());
        assertEquals(INGREDIENT_DESCRIPTION, actual.getDescription());
        assertEquals(UOM_ID, actual.getUom().getId());
        assertEquals(RECIPE_ID, actual.getRecipeId());
    }

    @Test
    public void convertWithNullUom() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setRecipe(recipe);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(INGREDIENT_AMOUNT);
        ingredient.setUom(null);
        IngredientCommand actual = converter.convert(ingredient);
        assertNotNull(actual);
        assertEquals(INGREDIENT_ID, actual.getId());
        assertEquals(INGREDIENT_AMOUNT, actual.getAmount());
        assertEquals(INGREDIENT_DESCRIPTION, actual.getDescription());
        assertEquals(RECIPE_ID, actual.getRecipeId());
        assertNull(actual.getUom());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}