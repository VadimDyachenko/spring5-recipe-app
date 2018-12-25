package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeServiceImplTest {

    private RecipeService service;

    @Mock
    private RecipeRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new RecipeServiceImpl(repository);
    }

    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(repository.findAll()).thenReturn(recipeData);

        Set<Recipe> actual = service.getRecipes();

        assertEquals(1, actual.size());
        verify(repository, times(1)).findAll();
    }
}