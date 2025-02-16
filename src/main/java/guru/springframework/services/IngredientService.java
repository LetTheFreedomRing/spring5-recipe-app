package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand getCommandById(Long recipeId, Long ingredientId);

    IngredientCommand saveCommand(IngredientCommand command);

    void deleteById(Long id);
}
