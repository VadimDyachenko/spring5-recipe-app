package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
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
    @Mock
    private RecipeCommandToRecipe recipeCommandToRecipe;
    @Mock
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new RecipeServiceImpl(repository, recipeCommandToRecipe, recipeToRecipeCommand);
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

    @Test
    public void saveRecipeCommand() {
        Long recipeId = 1L;
        Recipe recipe = Recipe.builder().id(recipeId).build();
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);

        when(recipeCommandToRecipe.convert(recipeCommand)).thenReturn(recipe);
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);
        when(repository.save(recipe)).thenReturn(recipe);

        RecipeCommand recipeCommandReturned = service.saveRecipeCommand(recipeCommand);

        assertNotNull("Null recipe command returned", recipeCommandReturned);
        verify(repository, times(1)).save(recipe);
        verify(recipeCommandToRecipe, times(1)).convert(recipeCommand);
        verify(recipeToRecipeCommand, times(1)).convert(recipe);
    }

    @Test
    public void getRecipeCommandById() {
        Long recipeId = 1L;
        Recipe recipe = Recipe.builder().id(recipeId).build();
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(repository.findById(recipeId)).thenReturn(recipeOptional);
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);

        RecipeCommand recipeCommandReturned = service.findCommandById(recipeId);

        assertNotNull("Null recipe command returned", recipeCommandReturned);
        verify(repository, times(1)).findById(recipeId);
        verify(repository, never()).findAll();
        verify(recipeToRecipeCommand, times(1)).convert(recipe);
    }

    @Test
    public void deleteById() {
        Long recipeId = 1L;

        service.deleteById(recipeId);

        verify(repository, times(1)).deleteById(recipeId);
    }
}