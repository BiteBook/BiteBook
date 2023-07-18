package controllers;

import models.Recipe;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import repositories.RecipeRepository;
import repositories.UserRepository;

@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    // other methods...

    @GetMapping("/recipes/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        model.addAttribute("recipe", recipe);
        return "editRecipe";
    }

    @PostMapping("/recipes/edit/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe recipe) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        if (!recipe.getUser().equals(currentUser)) {
            return "redirect:/error";
        }

        recipeRepository.save(recipe);
        return "redirect:/recipes/" + id;
    }
}
