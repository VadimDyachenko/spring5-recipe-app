package guru.springframework.converters;


import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NotesToNotesCommandTest {

    private static final Long ID_VALUE = 1L;
    private static final String RECIPE_NOTES = "Notes";
    private NotesToNotesCommand converter;

    @Before
    public void setUp() {
        converter = new NotesToNotesCommand();
    }

    @Test
    public void convertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void convertEmptyObject() {
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    public void convert() {
        Notes notes = new Notes();
        notes.setId(ID_VALUE);
        notes.setRecipeNotes(RECIPE_NOTES);

        NotesCommand result = converter.convert(notes);

        assertNotNull(result);
        assertEquals(ID_VALUE, result.getId());
        assertEquals(RECIPE_NOTES, result.getRecipeNotes());
    }
}
