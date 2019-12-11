package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryConverter;
    private final NotesCommandToNotes notesConverter;
    private final IngredientCommandToIngredient ingredientConverter;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConverter,
                                 NotesCommandToNotes notesConverter,
                                 IngredientCommandToIngredient ingredientConverter) {
        this.categoryConverter = categoryConverter;
        this.notesConverter = notesConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if (recipeCommand == null) return null;
        final Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setServings(recipeCommand.getServings());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setDifficulty(recipeCommand.getDifficulty());
        recipe.setUrl(recipeCommand.getUrl());
        recipe.setSource(recipeCommand.getSource());
        recipe.setImage(recipeCommand.getImage());
        recipe.setNotes(notesConverter.convert(recipeCommand.getNotes()));
        recipeCommand.getCategories().forEach(category -> {
            recipe.getCategories().add(categoryConverter.convert(category));
        });
        recipeCommand.getIngredients().forEach(ingredient -> {
            recipe.getIngredients().add(ingredientConverter.convert(ingredient));
        });
        return recipe;
    }
}
