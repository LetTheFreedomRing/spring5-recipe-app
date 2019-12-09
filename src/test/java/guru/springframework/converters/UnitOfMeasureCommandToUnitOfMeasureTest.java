package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureCommandToUnitOfMeasureTest {

    UnitOfMeasureCommandToUnitOfMeasure converter;
    static final Long uomId = 1L;
    static final String uomDescription = "dummy uom description";

    @Before
    public void setUp() throws Exception {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    public void convert() {
        UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(uomId);
        command.setUom(uomDescription);
        UnitOfMeasure uom = converter.convert(command);
        assertNotNull(uom);
        assertEquals(command.getId(), uom.getId());
        assertEquals(command.getUom(), uom.getUom());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}