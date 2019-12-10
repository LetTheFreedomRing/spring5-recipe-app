package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeService recipeService;
    private final IngredientToIngredientCommand ingredientConverter;
    private final IngredientCommandToIngredient ingredientCommandConverter;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeService recipeService,
                                 IngredientToIngredientCommand ingredientConverter,
                                 IngredientCommandToIngredient ingredientCommandConverter,
                                 RecipeRepository recipeRepository,
                                 IngredientRepository ingredientRepository,
                                 UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeService = recipeService;
        this.ingredientConverter = ingredientConverter;
        this.ingredientCommandConverter = ingredientCommandConverter;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand getCommandById(Long recipeId, Long ingredientId) {
        Optional<IngredientCommand> optionalIngredient = recipeService.getRecipeById(recipeId).getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientConverter::convert).findFirst();
        if (!optionalIngredient.isPresent()) {
            throw new RuntimeException("Ingredient with id " + ingredientId + " not found!");
        }
        return optionalIngredient.get();
    }

    @Override
    public IngredientCommand saveCommand(IngredientCommand command) {
        Recipe recipe = recipeService.getRecipeById(command.getRecipeId());
        Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();
        if (optionalIngredient.isPresent()) {
            Ingredient found = optionalIngredient.get();
            found.setDescription(command.getDescription());
            found.setAmount(command.getAmount());
            found.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM not found!")));
        } else {
            recipe.addIngredient(ingredientCommandConverter.convert(command));
        }
        Recipe savedRecipe = recipeRepository.save(recipe);
        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (!savedIngredient.isPresent()) {
            savedIngredient = savedRecipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        return ingredientConverter.convert(savedIngredient.get());
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
    }
}
