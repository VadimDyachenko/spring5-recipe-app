package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.ImageService;
import guru.springframework.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @Mock
    private RecipeService recipeService;

    private ImageController controller;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getImageForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        Long commandId = 1L;
        command.setId(commandId);

        when(recipeService.findCommandById(commandId)).thenReturn(command);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(commandId);
    }

    @Test
    public void uploadImage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image", "image.png", "image", "image bytes".getBytes()
        );

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImage(1L, multipartFile);
    }

    @Test
    public void renderImageFromDB() throws Exception {
        Long commandId = 1L;
        RecipeCommand command = new RecipeCommand();
        command.setId(commandId);

        String fake = "10101010101";
        Byte[] bytes = new Byte[fake.getBytes().length];

        int i = 0;
        for (byte source : fake.getBytes()) {
            bytes[i++] = source;
        }

        command.setImage(bytes);

        when(recipeService.findCommandById(commandId)).thenReturn(command);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();
        assertThat(bytes, is(responseBytes));
    }
}