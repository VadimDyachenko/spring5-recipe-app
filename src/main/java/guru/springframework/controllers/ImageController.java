package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.ImageService;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    @Autowired
    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{id}/image")
    public String getImageForm(@PathVariable Long id, Model model) {
        RecipeCommand command = recipeService.findCommandById(id);
        model.addAttribute("recipe", command);

        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {

        log.info("Starting upload image");
        imageService.saveImage(id, file);

        return "redirect:/recipe/" + id + "/show";
    }
}
