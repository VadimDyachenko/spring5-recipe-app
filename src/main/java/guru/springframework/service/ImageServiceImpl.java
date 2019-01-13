package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Default implementation for {@link ImageService}
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImage(Long id, MultipartFile file) {

        if (id == null || file == null) {
            throw new IllegalArgumentException("Arguments 'id' and 'file' can not be null!");
        }

        try {
            Optional<Recipe> recipeOptional = recipeRepository.findById(id);
            if(recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();
                Byte[] bytes = new Byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()) {
                    bytes[i++] = b;
                }
                recipe.setImage(bytes);
                recipeRepository.save(recipe);
            } else {
                log.error("Recipe with id: {} not found, saving image file {} aborted", id, file.getOriginalFilename());
            }
        } catch (IOException ex) {
            log.error("Can not save file: {}", file.getOriginalFilename(), ex);
        }
    }
}
