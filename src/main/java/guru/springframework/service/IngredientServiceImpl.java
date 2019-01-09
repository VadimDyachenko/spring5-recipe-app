package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository uomRepository;

    @Autowired
    public IngredientServiceImpl(
            IngredientToIngredientCommand ingredientToIngredientCommand,
            IngredientCommandToIngredient ingredientCommandToIngredient,
            RecipeRepository recipeRepository,
            UnitOfMeasureRepository uomRepository
    ) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.uomRepository = uomRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if(!recipeOptional.isPresent()) {
            //TODO Add error handling.
            log.error("Recipe with id: {} not found", recipeId);
        }

        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();
        if (!ingredientCommandOptional.isPresent()) {
            //TODO Add error handling.
            log.error("Ingredient with id: {} not found", ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Long recipeId = command.getRecipeId();
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        IngredientCommand ingredientCommand;

        if (!recipeOptional.isPresent()) {
            log.error("Recipe with id: {} not found", recipeId); //TODO Add error handling.
            ingredientCommand = new IngredientCommand();
        } else {
            ingredientCommand = getIngredientCommand(command, recipeOptional.get());
        }

        return ingredientCommand;
    }

    @Override
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("Recipe with id: {} not found.", recipeId);
        } else {
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
                log.debug("Ingredient id: {}, for recipe id: {} successfully deleted", ingredientId, recipeId);
            } else {
                log.error("Ingredient with id: {} not found", ingredientId);
            }
        }
    }

    private IngredientCommand getIngredientCommand(IngredientCommand command, Recipe recipe) {
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            updateIngredient(command, ingredientOptional.get());
        } else {
            addIngredient(command, recipe);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (!savedIngredientOptional.isPresent()) {
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }

    private void addIngredient(IngredientCommand command, Recipe recipe) {
        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
        ingredient.setRecipe(recipe);
        recipe.addIngredient(ingredient);
    }

    private void updateIngredient(IngredientCommand command, Ingredient ingredient) {
        ingredient.setDescription(command.getDescription());
        ingredient.setAmount(command.getAmount());

        ingredient.setUom(
                uomRepository.findById(command.getUom().getId())
                .orElseThrow(() -> new RuntimeException("Unit of Measure not found.")) //TODO Add error handling.
        );
    }
}
