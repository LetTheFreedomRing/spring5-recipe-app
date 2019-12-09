package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();

    Recipe getRecipeById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand getCommandById(Long id);

    void deleteById(Long id);
}
