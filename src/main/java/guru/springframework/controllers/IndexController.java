package guru.springframework.controllers;

import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService service;

    @Autowired
    public IndexController(RecipeService service) {
        this.service = service;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        log.debug("Getting index page");
        model.addAttribute("recipes", service.getRecipes());
        return "index";
    }
}

