package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.Ingredient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientCommandToIngredientTest {

    IngredientCommandToIngredient converter;

    static final Long INGREDIENT_ID = 1L;
    static final String INGREDIENT_DESCRIPTION = "dummy";
    static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(1);

    static final Long UOM_ID = 2L;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void convert() {
        IngredientCommand command = new IngredientCommand();
        command.setId(INGREDIENT_ID);
        command.setDescription(INGREDIENT_DESCRIPTION);
        command.setAmount(INGREDIENT_AMOUNT);
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(UOM_ID);
        command.setUom(uomCommand);
        Ingredient actual = converter.convert(command);
        assertNotNull(actual);
        assertNotNull(actual.getUom());
        assertEquals(INGREDIENT_ID, actual.getId());
        assertEquals(INGREDIENT_AMOUNT, actual.getAmount());
        assertEquals(INGREDIENT_DESCRIPTION, actual.getDescription());
        assertEquals(UOM_ID, actual.getUom().getId());
    }

    @Test
    @Transactional
    public void convertWithNullUom() {
        IngredientCommand command = new IngredientCommand();
        command.setId(INGREDIENT_ID);
        command.setDescription(INGREDIENT_DESCRIPTION);
        command.setAmount(INGREDIENT_AMOUNT);
        command.setUom(null);
        Ingredient actual = converter.convert(command);
        assertNotNull(actual);
        assertEquals(INGREDIENT_ID, actual.getId());
        assertEquals(INGREDIENT_AMOUNT, actual.getAmount());
        assertEquals(INGREDIENT_DESCRIPTION, actual.getDescription());
        assertNull(actual.getUom());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}