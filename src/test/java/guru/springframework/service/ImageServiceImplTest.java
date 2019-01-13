package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
    private ImageService imageService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void saveImageFile() throws IOException {
        Long id = 1L;
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testfile.png", "image/png", "1010101".getBytes());
        Recipe recipe = Recipe.builder().id(id).build();
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(id)).thenReturn(recipeOptional);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImage(id, multipartFile);

        verify(recipeRepository, times(1)).findById(id);
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

    @Test
    public void abortSavingImageFile() {
        Long id = 1L;
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testfile.png", "image/png", "1010101".getBytes());

        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        imageService.saveImage(id, multipartFile);

        verify(recipeRepository, times(1)).findById(id);
        verify(recipeRepository, never()).save(ArgumentMatchers.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveImageFileWithNullFileShouldThrownException() {
        imageService.saveImage(1L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveImageFileWithNullIdeShouldThrownException() {
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testfile.png", "image/png", "1010101".getBytes());
        imageService.saveImage(null, multipartFile);
    }
}
