package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;

public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    @InjectMocks
    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void newImage() throws Exception {
        Long recipeId = 1L;
        RecipeCommand command = new RecipeCommand();
        command.setId(recipeId);
        Mockito.when(recipeService.getCommandById(ArgumentMatchers.anyLong())).thenReturn(command);
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/image"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/imageuploadform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
        Mockito.verify(recipeService, Mockito.times(1)).getCommandById(ArgumentMatchers.anyLong());
    }

    @Test
    public void uploadImage() throws Exception {
        Long recipeId = 1L;
        MockMultipartFile file = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "Dummy".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/recipe/" + recipeId + "/image").file(file))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/recipe/" + recipeId + "/show"));
        Mockito.verify(imageService, Mockito.times(1)).saveImage(ArgumentMatchers.anyLong(), ArgumentMatchers.any());
    }

    @Test
    public void renderImageFromDb() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        String s = "dummy image text";
        Byte[] bytes = new Byte[s.getBytes().length];

        int i = 0;
        for (byte b : s.getBytes()) {
            bytes[i++] = b;
        }

        command.setImage(bytes);

        Mockito.when(recipeService.getCommandById(ArgumentMatchers.anyLong())).thenReturn(command);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/recipeimage"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();
        assertArrayEquals(s.getBytes(), responseBytes);
    }
}