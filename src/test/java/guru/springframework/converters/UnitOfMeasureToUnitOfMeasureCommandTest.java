package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class UnitOfMeasureToUnitOfMeasureCommandTest {

    private static final String DESCRIPTION = "description";
    private static final Long LONG_VALUE = 1L;

    private UnitOfMeasureToUnitOfMeasureCommand converter;

    @Before
    public void setUp() {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    public void convertNullObjectConvert() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convertEmptyObj() {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    public void convert() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(LONG_VALUE);
        uom.setDescription(DESCRIPTION);

        UnitOfMeasureCommand result = converter.convert(uom);

        assertNotNull(result);
        assertEquals(LONG_VALUE, result.getId());
        assertEquals(DESCRIPTION, result.getDescription());
    }
}
