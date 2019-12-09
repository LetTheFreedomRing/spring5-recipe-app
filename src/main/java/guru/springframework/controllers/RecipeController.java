package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/{id}/show")
    public String showById(@PathVariable String id, Model model) {
        Recipe recipe = recipeService.getRecipeById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }

    @RequestMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @RequestMapping("/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        RecipeCommand command = recipeService.getCommandById(Long.valueOf(id));
        model.addAttribute("recipe", command);
        return "recipe/recipeform";
    }

    @PostMapping
    @RequestMapping("/")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @PostMapping
    @RequestMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
}
