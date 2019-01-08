package guru.springframework.service;


import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UnitOfMeasureRepository uomRepository;

    private IngredientService service;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        this.unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new IngredientServiceImpl(
                ingredientToIngredientCommand, ingredientCommandToIngredient, recipeRepository, uomRepository
        );
    }

    @Test
    public void findByRecipeIdAndIngredientId() {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        Long ingredientId = 3L;
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(ingredientId);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(recipeId)).thenReturn(recipeOptional);

        IngredientCommand ingredientCommand = service.findByRecipeIdAndIngredientId(recipeId, ingredientId);

        assertEquals(ingredientId, ingredientCommand.getId());
        assertEquals(recipeId, ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    public void testSaveIngredientCommand() {
        IngredientCommand command = new IngredientCommand();
        Long ingredientId = 3L;
        Long recipeId = 2L;
        command.setId(ingredientId);
        command.setRecipeId(recipeId);

        Recipe recipe = new Recipe();
        Recipe savedRecipe = new Recipe();
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        savedRecipe.addIngredient(ingredient);

        when(recipeRepository.findById(recipeId)).thenReturn(recipeOptional);
        when(recipeRepository.save(recipe)).thenReturn(savedRecipe);

        IngredientCommand savedCommand = service.saveIngredientCommand(command);

        assertEquals(ingredientId, savedCommand.getId());
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).save(savedRecipe);
    }

    @Test
    public void testSaveNewIngredientCommand() {

        Long ingredientId = 3L;
        Long recipeId = 2L;
        Long uomId = 5L;
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(uomId);
        UnitOfMeasureCommand uomCommand = unitOfMeasureToUnitOfMeasureCommand.convert(uom);

        IngredientCommand command = new IngredientCommand();
        command.setId(ingredientId);
        command.setRecipeId(recipeId);
        command.setUom(uomCommand);
        command.setDescription("New description");

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        ingredient.setUom(uom);
        ingredient.setDescription("Old description");

        Recipe recipe = new Recipe();
        recipe.addIngredient(ingredient);

        Recipe savedRecipe = new Recipe();
        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId(ingredientId);
        savedIngredient.setUom(uom);
        savedIngredient.setDescription("New description");
        savedRecipe.addIngredient(savedIngredient);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(recipeId)).thenReturn(recipeOptional);
        when(recipeRepository.save(recipe)).thenReturn(savedRecipe);
        when(uomRepository.findById(uomId)).thenReturn(Optional.of(uom));

        IngredientCommand savedCommand = service.saveIngredientCommand(command);

        assertEquals(ingredientId, savedCommand.getId());
        assertEquals("New description", savedCommand.getDescription());
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).save(recipe);
    }
}
