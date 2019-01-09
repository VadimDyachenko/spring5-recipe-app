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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    private static final String NEW_DESCRIPTION = "New description";
    private static final String OLD_DESCRIPTION = "Old description";

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
    public void testSaveWithUpdateIngredient() {
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
    public void testSaveWithAddIngredient() {

        Long ingredientId = 3L;
        Long recipeId = 2L;
        Long uomId = 5L;
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(uomId);
        UnitOfMeasureCommand uomCommand = unitOfMeasureToUnitOfMeasureCommand.convert(uom);

        IngredientCommand command = new IngredientCommand();
        command.setId(ingredientId);
        command.setRecipeId(recipeId);
        command.setAmount(BigDecimal.valueOf(10));
        command.setUom(uomCommand);
        command.setDescription(NEW_DESCRIPTION);

        Ingredient ingredient = getIngredient(ingredientId, uom, BigDecimal.valueOf(10), OLD_DESCRIPTION);

        Recipe recipe = new Recipe();
        recipe.addIngredient(ingredient);


        Ingredient savedIngredient = getIngredient(ingredientId, uom, BigDecimal.valueOf(10), NEW_DESCRIPTION);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(savedIngredient);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(recipeId)).thenReturn(recipeOptional);
        when(recipeRepository.save(recipe)).thenReturn(savedRecipe);
        when(uomRepository.findById(uomId)).thenReturn(Optional.of(uom));

        IngredientCommand savedCommand = service.saveIngredientCommand(command);

        assertEquals(ingredientId, savedCommand.getId());
        assertEquals(NEW_DESCRIPTION, savedCommand.getDescription());
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    public void testSaveWithAddIngredientWhenIdNotFoundButOtherFieldsIsSame() {

        Long ingredientId = 3L;
        Long recipeId = 2L;
        Long uomId = 5L;
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(uomId);
        UnitOfMeasureCommand uomCommand = unitOfMeasureToUnitOfMeasureCommand.convert(uom);

        IngredientCommand command = new IngredientCommand();
        command.setId(ingredientId);
        command.setRecipeId(recipeId);
        command.setAmount(BigDecimal.valueOf(10));
        command.setUom(uomCommand);
        command.setDescription(NEW_DESCRIPTION);

        Ingredient ingredient = getIngredient(ingredientId, uom, BigDecimal.valueOf(10), OLD_DESCRIPTION);

        Recipe recipe = new Recipe();
        recipe.addIngredient(ingredient);


        Long newIngredientId = 1L;
        Ingredient savedIngredient = getIngredient(newIngredientId, uom, BigDecimal.valueOf(10), NEW_DESCRIPTION);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(savedIngredient);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(recipeId)).thenReturn(recipeOptional);
        when(recipeRepository.save(recipe)).thenReturn(savedRecipe);
        when(uomRepository.findById(uomId)).thenReturn(Optional.of(uom));

        IngredientCommand savedCommand = service.saveIngredientCommand(command);

        assertEquals(newIngredientId, savedCommand.getId());
        assertEquals(NEW_DESCRIPTION, savedCommand.getDescription());
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).save(recipe);
    }

    private Ingredient getIngredient(Long id, UnitOfMeasure uom, BigDecimal amount, String description) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setUom(uom);
        ingredient.setAmount(amount);
        ingredient.setDescription(description);

        return ingredient;
    }

    @Test
    public void testDeleteByRecipeIdAndIngredientId() {
        Long recipeId = 1L;
        Long secondIngredientId = 2L;

        Ingredient firstIngredient = new Ingredient();
        firstIngredient.setId(1L);
        Ingredient secondIngredient = new Ingredient();
        secondIngredient.setId(secondIngredientId);

        Set<Ingredient> ingredients = new HashSet<>(Arrays.asList(firstIngredient, secondIngredient));

        Recipe recipe = Recipe.builder().id(recipeId).ingredients(ingredients).build();
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(recipeId)).thenReturn(recipeOptional);

        Set<Ingredient> modifiedIngredients = new HashSet<>(Collections.singletonList(firstIngredient));
        Recipe modifiedRecipe = Recipe.builder().id(recipeId).ingredients(modifiedIngredients).build();

        service.deleteByRecipeIdAndIngredientId(recipeId, secondIngredientId);

        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).save(modifiedRecipe);
    }
}
