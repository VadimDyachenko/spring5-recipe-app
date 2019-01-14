package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.NotesCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecipeCommandToRecipeTest {

    private static final Long RECIPE_ID = 1L;
    private static final Integer COOK_TIME = 10;
    private static final Integer PREP_TIME = 15;
    private static final String DESCRIPTION = "Recipe description";
    private static final String DIRECTIONS = "Directions";
    private static final Difficulty DIFFICULTY = Difficulty.EASY;
    private static final Integer SERVINGS = 4;
    private static final String SOURCE = "Source";
    private static final String URL = "URL";
    private static final Long CAT_ID_1 = 1L;
    private static final Long CAT_ID_2 = 2L;
    private static final Long INGREDIENT_ID_1 = 3L;
    private static final Long INGREDIENT_ID_2 = 4L;
    private static final Long NOTES_ID = 9L;
    private static final Byte[] IMAGE_BYTES = getBytes();

    private RecipeCommandToRecipe converter;

    private static Byte[] getBytes() {
        byte[] bytes = "1010101".getBytes();
        Byte[] result = new Byte[bytes.length];
        int i = 0;
        for (byte source : bytes) {
            result[i++] = source;
        }
        return result;
    }

    @Before
    public void setUp() {
        converter = new RecipeCommandToRecipe(
                new CategoryCommandToCategory(),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
                new NotesCommandToNotes()
        );
    }

    @Test
    public void convertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convertEmptyObject() {
        assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    public void convert() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setCookTime(COOK_TIME);
        recipeCommand.setPrepTime(PREP_TIME);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setDifficulty(DIFFICULTY);
        recipeCommand.setDirections(DIRECTIONS);
        recipeCommand.setServings(SERVINGS);
        recipeCommand.setSource(SOURCE);
        recipeCommand.setUrl(URL);
        recipeCommand.setImage(IMAGE_BYTES);

        NotesCommand notes = new NotesCommand();
        notes.setId(NOTES_ID);

        recipeCommand.setNotes(notes);

        CategoryCommand category = new CategoryCommand();
        category.setId(CAT_ID_1);
        CategoryCommand category2 = new CategoryCommand();
        category2.setId(CAT_ID_2);
        recipeCommand.getCategories().add(category);
        recipeCommand.getCategories().add(category2);

        IngredientCommand ingredient = new IngredientCommand();
        ingredient.setId(INGREDIENT_ID_1);
        IngredientCommand ingredient2 = new IngredientCommand();
        ingredient2.setId(INGREDIENT_ID_2);
        recipeCommand.getIngredients().add(ingredient);
        recipeCommand.getIngredients().add(ingredient2);

        Recipe result  = converter.convert(recipeCommand);

        assertNotNull(result);
        assertEquals(RECIPE_ID, result.getId());
        assertEquals(COOK_TIME, result.getCookTime());
        assertEquals(PREP_TIME, result.getPrepTime());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(DIFFICULTY, result.getDifficulty());
        assertEquals(DIRECTIONS, result.getDirections());
        assertEquals(SERVINGS, result.getServings());
        assertEquals(SOURCE, result.getSource());
        assertEquals(URL, result.getUrl());
        assertEquals(NOTES_ID, result.getNotes().getId());
        assertEquals(2, result.getCategories().size());
        assertEquals(2, result.getIngredients().size());
        assertEquals(IMAGE_BYTES, result.getImage());
    }
}