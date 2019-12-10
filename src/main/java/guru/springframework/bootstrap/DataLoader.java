package guru.springframework.bootstrap;

import guru.springframework.model.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    private UnitOfMeasure getUOM(String uom) {
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByUom(uom);
        if (!uomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }
        return uomOptional.get();
    }

    private Category getCategory(String category) {
        Optional<Category> categoryOptional = categoryRepository.findByDescription(category);
        if (!categoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category not found");
        }
        return categoryOptional.get();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loaded data on bootstrap");
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

        Category mexican = getCategory("Mexican");
        Category american = getCategory("American");

        UnitOfMeasure unit = getUOM("Unit");
        UnitOfMeasure teaspoon = getUOM("Teaspoon");
        UnitOfMeasure tablespoon = getUOM("Tablespoon");
        UnitOfMeasure cup = getUOM("Cup");
        UnitOfMeasure gram = getUOM("Gram");
        UnitOfMeasure dash = getUOM("Dash");

        Recipe guacamole = new Recipe();
        guacamole.getCategories().add(mexican);
        guacamole.setDirections("1. Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. Place in a bowl.\n" +
                "2. Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "3. Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                        "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                        "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");
        guacamole.setCookTime(5);
        guacamole.setPrepTime(10);
        guacamole.setDescription("How to Make Perfect Guacamole");
        guacamole.setServings(4);
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.setDifficulty(Difficulty.EASY);

        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes("Garnish with red radishes or jicama. Serve with tortilla chips.");

        guacamole.addIngredient(new Ingredient("Ripe avocado", new BigDecimal(2), unit));
        guacamole.addIngredient(new Ingredient("Kosher salt", new BigDecimal(0.5), teaspoon));
        guacamole.addIngredient(new Ingredient("Lime juice", new BigDecimal(1), tablespoon));
        guacamole.addIngredient(new Ingredient("Minced red onion", new BigDecimal(2), tablespoon));
        guacamole.addIngredient(new Ingredient("Serrano chiles, stems and seeds removed, minced", new BigDecimal(2), unit));
        guacamole.addIngredient(new Ingredient("Cilantro (leaves and tender stems), finely chopped", new BigDecimal(1), tablespoon));
        guacamole.addIngredient(new Ingredient("Freshly grated black pepper", new BigDecimal(1), dash));
        guacamole.addIngredient(new Ingredient("Ripe tomato, seeds and pulp removed, chopped", new BigDecimal(0.5), unit));
        guacamole.setNotes(guacamoleNotes);

        log.debug("Created recipe : " + guacamole);
        recipes.add(0, guacamole);

        Recipe tacos = new Recipe();
        tacos.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacos.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");
        tacos.setDifficulty(Difficulty.MODERATE);
        tacos.setPrepTime(20);
        tacos.setCookTime(15);
        tacos.setServings(6);
        tacos.getCategories().add(american);
        tacos.setDescription("Spicy Grilled Chicken Tacos Recipe");

        Notes tacosNotes = new Notes();
        tacosNotes.setRecipeNotes("Dummy notes for tacos");

        tacos.setNotes(tacosNotes);

        tacos.addIngredient(new Ingredient("Ancho chili powder", new BigDecimal(2), tablespoon));
        tacos.addIngredient(new Ingredient("Dried oregano", new BigDecimal(1), teaspoon));
        tacos.addIngredient(new Ingredient("Salt", new BigDecimal(0.5), teaspoon));
        tacos.addIngredient(new Ingredient("Sugar", new BigDecimal(1), teaspoon));
        tacos.addIngredient(new Ingredient("Dried cumin", new BigDecimal(1), teaspoon));
        tacos.addIngredient(new Ingredient("Clove garlic, finely chopped", new BigDecimal(1), unit));
        tacos.addIngredient(new Ingredient("Finely grated orange zest", new BigDecimal(1), tablespoon));
        tacos.addIngredient(new Ingredient("Fresh-squeezed orange juice", new BigDecimal(3), tablespoon));
        tacos.addIngredient(new Ingredient("Olive oil", new BigDecimal(2), tablespoon));
        tacos.addIngredient(new Ingredient("Skinless, boneless chicken thighs", new BigDecimal(6), unit));
        tacos.addIngredient(new Ingredient("Small corn tortillas", new BigDecimal(8), unit));
        tacos.addIngredient(new Ingredient("Packed baby arugula", new BigDecimal(3), cup));
        tacos.addIngredient(new Ingredient("Medium ripe avocados, sliced", new BigDecimal(2), unit));
        tacos.addIngredient(new Ingredient("Radishes, thinly sliced", new BigDecimal(4), unit));
        tacos.addIngredient(new Ingredient("Cherry tomatoes, halved", new BigDecimal(400), gram));
        tacos.addIngredient(new Ingredient("Red onion, thinly sliced", new BigDecimal(0.25), unit));
        tacos.addIngredient(new Ingredient("Roughly chopped cilantro", new BigDecimal(1), unit));
        tacos.addIngredient(new Ingredient("Sour cream", new BigDecimal(0.5), cup));
        tacos.addIngredient(new Ingredient("Lime, cut into wedges", new BigDecimal(1), unit));

        recipes.add(1, tacos);

        log.debug("Created recipe : " + tacos);
        return recipes;
    }
}
