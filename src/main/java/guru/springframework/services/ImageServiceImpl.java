package guru.springframework.services;

import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImage(Long recipeId, MultipartFile image) {
        try {
            Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
            if (!optionalRecipe.isPresent()) {
                throw new RuntimeException("Recipe with id " + recipeId + " not found");
            }
            Recipe recipe = optionalRecipe.get();

            byte[] imageBytes = image.getBytes();
            Byte[] bytes = new Byte[imageBytes.length];
            for (int i = 0; i < bytes.length; bytes[i] = imageBytes[i], i++);
            recipe.setImage(bytes);
            recipeRepository.save(recipe);
        } catch (IOException e) {
            //todo: handle better
            log.error("Error occurred", e);
            e.printStackTrace();
        }

    }
}
