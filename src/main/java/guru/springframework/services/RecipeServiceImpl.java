package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    @Value("classpath:static/images/default_recipe_image.jpg")
    private Resource defaultImage;

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandConverter;
    private final RecipeToRecipeCommand recipeConverter;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandConverter, RecipeToRecipeCommand recipeConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandConverter = recipeCommandConverter;
        this.recipeConverter = recipeConverter;
    }

    @Override
    public Set<Recipe> getAllRecipes() {
        log.debug("getAllRecipes()");
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (!recipe.isPresent()) {
            throw new NotFoundException("Recipe Not Found!");
        }
        return recipe.get();
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        if (command.getId() == null) {
            Byte[] bytes = getDefaultImageBytes();
            command.setImage(bytes);
        } else {
            // can ignore this warning, because recipe exists in repo
            Recipe recipe = recipeRepository.findById(command.getId()).get();
            command.setImage(recipe.getImage());
        }
        Recipe savedRecipe = recipeRepository.save(recipeCommandConverter.convert(command));
        log.debug("Saved recipe id : " + savedRecipe.getId());
        return recipeConverter.convert(savedRecipe);
    }

    @Override
    public RecipeCommand getCommandById(Long id) {
        return recipeConverter.convert(getRecipeById(id));
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
        log.debug("Deleted recipe id : " + id);
    }

    private Byte[] getDefaultImageBytes() {
        try {
            File imageFile =  defaultImage.getFile();
            log.debug("File exists : " + imageFile.exists());
            byte[] bytes = Files.readAllBytes(imageFile.toPath());
            Byte[] res = new Byte[bytes.length];
            int i = 0;
            for (byte b : bytes) {
                res[i++] = b;
            }
            return res;

        } catch (IOException e) {
            log.error("getDefaultImage() error : " + e.getMessage());
        }
        return null;
    }
}
