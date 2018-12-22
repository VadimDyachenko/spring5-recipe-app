package guru.springframework.bootstarp;

import guru.springframework.domain.Category;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Notes;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository uomRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public DataInitializer(
            CategoryRepository categoryRepository,
            UnitOfMeasureRepository unitOfMeasureRepository,
            RecipeRepository recipeRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.uomRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

        Category mexican = getCategory("Mexican");
        Category american = getCategory("American");

        UnitOfMeasure piece = getUnitOfMeasure("Piece");
        UnitOfMeasure tablespoon = getUnitOfMeasure("Tablespoon");
        UnitOfMeasure dash = getUnitOfMeasure("Dash");
        UnitOfMeasure teaspoon = getUnitOfMeasure("Teaspoon");
        UnitOfMeasure cup = getUnitOfMeasure("Cup");
        UnitOfMeasure pint = getUnitOfMeasure("Pint");

        Recipe guacamole = new Recipe();
        guacamole.setDescription("Perfect Guacamole");
        guacamole.setPrepTime(10);
        guacamole.setCookTime(0);
        guacamole.setDirections(
                "1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside" +
                " of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel " +
                "an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should " +
                "be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the" +
                " lime juice will provide some balance to the richness of the avocado and will help delay the " +
                "avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their" +
                " hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree" +
                " of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. " +
                "Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it" +
                " and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the " +
                "guacamole brown.) Refrigerate until ready to serve."
        );

        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes(
                "For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "\n" +
                "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches " +
                "in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries (see " +
                "our Strawberry Guacamole).\n" +
                "\n" +
                "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of " +
                "availability of other ingredients stop you from making guacamole.\n" +
                "\n" +
                "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole " +
                "dip. Purists may be horrified, but so what? It tastes great.\n"
        );
        guacamoleNotes.setRecipe(guacamole);

        guacamole.setNotes(guacamoleNotes);
        guacamole.setDifficulty(Difficulty.EASY);

        guacamole.addIngredient(new Ingredient("Avocado", BigDecimal.valueOf(0.5), piece));
        guacamole.addIngredient(new Ingredient("Fresh lime juice", BigDecimal.valueOf(1L), tablespoon));
        guacamole.addIngredient(new Ingredient("Minced red onion", BigDecimal.valueOf(2L), tablespoon));
        guacamole.addIngredient(new Ingredient("Serrano chilies", BigDecimal.valueOf(2L), piece));
        guacamole.addIngredient(new Ingredient("Cilantro", BigDecimal.valueOf(2L), tablespoon));
        guacamole.addIngredient(new Ingredient("Pepper", BigDecimal.valueOf(1L), dash));
        guacamole.addIngredient(new Ingredient("Tomato", BigDecimal.valueOf(0.5), piece));

        guacamole.setServings(4);
        guacamole.setSource("Simplyrecipes");
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.getCategories().add(mexican);

        recipes.add(guacamole);

        // Tacos recipe
        Recipe tacos = new Recipe();

        tacos.setDescription("Spicy Grilled Chicken Taco");
        tacos.setPrepTime(20);
        tacos.setCookTime(9);
        tacos.setDifficulty(Difficulty.MODERATE);
        tacos.setSource("Simplyrecipes");
        tacos.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");

        tacos.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, " +
                "cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose " +
                "paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted " +
                "into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high " +
                "heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs " +
                "and heat for a few seconds on the other side.\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of " +
                "arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle " +
                "with the thinned sour cream. Serve with lime wedges.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvtrAnNm");

        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of " +
                "pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating " +
                "in a hot pan on the stove comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and " +
                "sweet orange juice while the grill is heating. You can also use this time to prepare the taco " +
                "toppings.\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the " +
                "tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvu7Q0MJ");
        tacoNotes.setRecipe(tacos);
        tacos.setNotes(tacoNotes);

        tacos.addIngredient(new Ingredient("Ancho chili powder", new BigDecimal(2L), tablespoon));
        tacos.addIngredient(new Ingredient("Dried oregano", new BigDecimal(1L), teaspoon));
        tacos.addIngredient(new Ingredient("Dried cumin", new BigDecimal(1L), teaspoon));
        tacos.addIngredient(new Ingredient("Sugar", new BigDecimal(1L), teaspoon));
        tacos.addIngredient(new Ingredient("Salt", new BigDecimal(0.5), teaspoon));
        tacos.addIngredient(new Ingredient("Clove of garlic, chopped", new BigDecimal(1L), piece));
        tacos.addIngredient(new Ingredient("finely grated orange zest", new BigDecimal(1L), tablespoon));
        tacos.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3L), tablespoon));
        tacos.addIngredient(new Ingredient("Olive oil", new BigDecimal(2L), tablespoon));
        tacos.addIngredient(new Ingredient("boneless chicken thighs", new BigDecimal(4L), tablespoon));
        tacos.addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8L), piece));
        tacos.addIngredient(new Ingredient("packed baby arugula", new BigDecimal(3L), cup));
        tacos.addIngredient(new Ingredient("medium ripe avocados, sliced", new BigDecimal(2L), piece));
        tacos.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4L), piece));
        tacos.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(0.5), pint));
        tacos.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(0.25), piece));
        tacos.addIngredient(new Ingredient("Roughly chopped cilantro", new BigDecimal(4L), piece));
        tacos.addIngredient(new Ingredient("cup sour cream thinned with 1/4 cup milk", new BigDecimal(4L), cup));
        tacos.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(4L), piece));

        tacos.getCategories().add(american);
        tacos.getCategories().add(mexican);

        recipes.add(tacos);

        return recipes;
    }

    private Category getCategory(String category) {
        return Optional.of(categoryRepository.findByDescription(category)).get()
                .orElseThrow(() -> new RuntimeException(String.format("Category %s is not found", category)));
    }

    private UnitOfMeasure getUnitOfMeasure(String unit) {
        return Optional.of(uomRepository.findByDescription(unit)).get()
                .orElseThrow(() -> new RuntimeException(String.format("Unit of measure %s is not found", unit)));
    }
}
