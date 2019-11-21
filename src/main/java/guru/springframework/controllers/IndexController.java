package guru.springframework.controllers;

import guru.springframework.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private final RecipeRepository recipeRepository;

    public IndexController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @RequestMapping({"/", "index", "index.html"})
    public String getIndexPage(Model model) {

        model.addAttribute("recipes", recipeRepository.findAll());

        return "index";
    }

}
