package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeService recipeService;
    private final IngredientToIngredientCommand converter;

    public IngredientServiceImpl(RecipeService recipeService, IngredientToIngredientCommand converter) {
        this.recipeService = recipeService;
        this.converter = converter;
    }

    @Override
    public IngredientCommand getCommandById(Long recipeId, Long ingredientId) {
        Optional<IngredientCommand> optionalIngredient = recipeService.getRecipeById(recipeId).getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(converter::convert).findFirst();
        if (!optionalIngredient.isPresent()) {
            throw new RuntimeException("Ingredient with id " + ingredientId + " not found!");
        }
        return optionalIngredient.get();
    }
}
