package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IngredientToIngredientCommandTest {

    private static final Recipe RECIPE = new Recipe();
    private static final BigDecimal AMOUNT = new BigDecimal(10);
    private static final String DESCRIPTION = "Test Description";
    private static final Long UOM_ID = 2L;
    private static final Long ID_VALUE = 1L;


    private IngredientToIngredientCommand converter;

    @Before
    public void setUp() {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void convertNullConvert() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convertEmptyObject() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    public void convertConvertNullUOM() {

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setRecipe(RECIPE);
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setUom(null);

        IngredientCommand result = converter.convert(ingredient);

        assertNotNull(result);
        assertNull(result.getUnitOfMeasure());
        assertEquals(ID_VALUE, result.getId());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(DESCRIPTION, result.getDescription());
    }

    @Test
    public void convertConvertWithUom() {

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setRecipe(RECIPE);
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        ingredient.setUom(uom);

        IngredientCommand result = converter.convert(ingredient);

        assertNotNull(result);
        assertEquals(ID_VALUE, result.getId());
        assertNotNull(result.getUnitOfMeasure());
        assertEquals(UOM_ID, result.getUnitOfMeasure().getId());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(DESCRIPTION, result.getDescription());
    }
}
