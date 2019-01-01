package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
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
    public void getRecipeById() {
        Long recipeId = 1L;
        Recipe recipe = Recipe.builder().id(recipeId).build();
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(repository.findById(recipeId)).thenReturn(recipeOptional);

        Recipe recipeReturned = service.findById(recipeId);

        assertNotNull("Null recipe returned", recipeReturned);
        verify(repository, times(1)).findById(recipeId);
        verify(repository, never()).findAll();
    }

    @Test(expected = RuntimeException.class)
    public void getRecipeByIdShouldTrowRuntimeException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        service.findById(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).findAll();
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