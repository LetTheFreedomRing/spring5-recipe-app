package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final CategoryToCategoryCommand categoryConverter;
    private final NotesToNotesCommand notesConverter;
    private final IngredientToIngredientCommand ingredientConverter;

    public RecipeToRecipeCommand(CategoryToCategoryCommand categoryConverter,
                                 NotesToNotesCommand notesConverter,
                                 IngredientToIngredientCommand ingredientConverter) {
        this.categoryConverter = categoryConverter;
        this.notesConverter = notesConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe recipe) {
        if (recipe == null) return null;
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipe.getId());
        recipeCommand.setDirections(recipe.getDirections());
        recipeCommand.setDescription(recipe.getDescription());
        recipeCommand.setServings(recipe.getServings());
        recipeCommand.setCookTime(recipe.getCookTime());
        recipeCommand.setPrepTime(recipe.getPrepTime());
        recipeCommand.setDifficulty(recipe.getDifficulty());
        recipeCommand.setUrl(recipe.getUrl());
        recipeCommand.setSource(recipe.getSource());
        recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));
        recipe.getCategories().forEach(category -> {
            recipeCommand.getCategories().add(categoryConverter.convert(category));
        });
        recipe.getIngredients().forEach(ingredient -> {
            recipeCommand.getIngredients().add(ingredientConverter.convert(ingredient));
        });
        return recipeCommand;
    }
}
