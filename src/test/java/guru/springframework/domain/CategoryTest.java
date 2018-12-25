package guru.springframework.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        category = new Category();
        category.setId(7L);
        category.setDescription("Test description");
    }

    @Test
    public void getId() {
        Long expected = 7L;
        assertEquals(expected, category.getId());
    }

    @Test
    public void getDescription() {
        assertEquals("Test description", category.getDescription());
    }

}