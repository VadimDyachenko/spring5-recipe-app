package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IngredientCommandToIngredientTest {

    private static final BigDecimal AMOUNT = new BigDecimal(10);
    private static final String DESCRIPTION = "Test description";
    private static final Long ID_VALUE = 1L;
    private static final Long UOM_ID = 2L;

    private IngredientCommandToIngredient converter;

    @Before
    public void setUp() {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void convertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convertEmptyObject() {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    public void convert() {
        IngredientCommand command = new IngredientCommand();
        command.setId(ID_VALUE);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        command.setUnitOfMeasure(unitOfMeasureCommand);

        Ingredient result = converter.convert(command);

        assertNotNull(result);
        assertNotNull(result.getUom());
        assertEquals(ID_VALUE, result.getId());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(UOM_ID, result.getUom().getId());
    }

    @Test
    public void convertWithNullUOM() {
        IngredientCommand command = new IngredientCommand();
        command.setId(ID_VALUE);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);

        Ingredient result = converter.convert(command);

        assertNotNull(result);
        assertNull(result.getUom());
        assertEquals(ID_VALUE, result.getId());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(DESCRIPTION, result.getDescription());
    }
}
