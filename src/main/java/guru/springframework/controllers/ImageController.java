package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    private final ImageService service;

    private final RecipeService recipeService;

    public ImageController(ImageService service, RecipeService recipeService) {
        this.service = service;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{recipeId}/image")
    public String newImage(@PathVariable String recipeId, Model model) {
        log.debug("New image request for recipe : " + recipeId);
        model.addAttribute("recipe", recipeService.getCommandById(Long.valueOf(recipeId)));
        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{recipeId}/image")
    public String uploadImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file) {
        service.saveImage(Long.valueOf(recipeId), file);
        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("recipe/{recipeId}/recipeimage")
    public void renderImageFromDb(@PathVariable String recipeId, HttpServletResponse response) {
        RecipeCommand command = recipeService.getCommandById(Long.valueOf(recipeId));
        byte[] bytes = new byte[command.getImage().length];

        int i = 0;
        for (Byte b : command.getImage()) {
            bytes[i++] = b;
        }

        response.setContentType("image/jpeg");
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException ex) {
            log.error("Error returning image", ex);
        }
    }
}
