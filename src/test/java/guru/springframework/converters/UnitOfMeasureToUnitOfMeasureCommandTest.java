package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureToUnitOfMeasureCommandTest {

    UnitOfMeasureToUnitOfMeasureCommand converter;
    static final Long uomId = 1L;
    static final String uomDescription = "dummy uom description";

    @Before
    public void setUp() throws Exception {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    public void convert() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(uomId);
        uom.setUom(uomDescription);
        UnitOfMeasureCommand command = converter.convert(uom);
        assertNotNull(command);
        assertEquals(uom.getId(), command.getId());
        assertEquals(uom.getUom(), command.getUom());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}