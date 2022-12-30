package recipes.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.Recipe;
import recipes.models.RecipeDtoResponse;
import recipes.models.User;
import recipes.services.RecipeService;
import recipes.services.UserServiceImpl;

import java.util.*;

@RestController
@RequestMapping("api/recipe")
public class RecipesController {
    private final RecipeService recipeService;
    private final UserServiceImpl userDetails;

    public RecipesController(RecipeService service, UserServiceImpl userDetails) {
        this.recipeService = service;
        this.userDetails = userDetails;
    }

    @GetMapping("/{id}")
    public Recipe getRecipe(@AuthenticationPrincipal UserDetails details, @PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        System.out.println(details.getUsername());
        System.out.println(details.getPassword());
        System.out.println(details.getAuthorities());
        if (!recipeService.findRecipe(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return new Recipe(
                    recipe.getName(),
                    recipe.getDescription(),
                    recipe.getCategory(),
                    recipe.getDate(),
                    recipe.getIngredients(),
                    recipe.getDirections()
            );
        }
    }

    @PostMapping("/new")
    public ResponseEntity<RecipeDtoResponse> postRecipe(@AuthenticationPrincipal UserDetails details, @RequestBody Recipe recipe) {
        if (recipe == null ||
                recipe.getName() == null || recipe.getName().isEmpty() || recipe.getName().isBlank() ||
                recipe.getDescription() == null || recipe.getDescription().isEmpty() || recipe.getDescription().isBlank() ||
                recipe.getCategory() == null || recipe.getCategory().isEmpty() || recipe.getCategory().isBlank() ||
                recipe.getIngredients() == null || recipe.getIngredients().isEmpty() ||
                recipe.getDirections() == null || recipe.getDirections().isEmpty()
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            User user = userDetails.getUserByEmail(details.getUsername());
            recipe.getUsersInRecipe().add(user);
            recipeService.saveRecipe(recipe);
        }
        return new ResponseEntity<>(new RecipeDtoResponse(recipe.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@AuthenticationPrincipal UserDetails details, @PathVariable Long id) {

        if (!recipeService.findRecipe(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            if (recipeService.isAuthorRecipe(id, details.getUsername())) {
                recipeService.deleteRecipeById(id);
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public void putRecipe(@AuthenticationPrincipal UserDetails details, @RequestBody Recipe recipe, @PathVariable Long id) {
        if (recipeService.findRecipe(id)) {
            if (
                    recipe.getName() == null || recipe.getName().isEmpty() || recipe.getName().isBlank() ||
                            recipe.getDescription() == null || recipe.getDescription().isEmpty() || recipe.getDescription().isBlank() ||
                            recipe.getCategory() == null || recipe.getCategory().isEmpty() || recipe.getCategory().isBlank() ||
                            recipe.getIngredients() == null || recipe.getIngredients().isEmpty() ||
                            recipe.getDirections() == null || recipe.getDirections().isEmpty()
            ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                if (recipeService.isAuthorRecipe(id, details.getUsername())) {
                    User user = userDetails.getUserByEmail(details.getUsername());
                    recipe.getUsersInRecipe().add(user);
                    recipe.setId(id);
                    recipeService.saveRecipe(recipe);
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT);
                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@AuthenticationPrincipal UserDetails details, @Nullable @RequestParam String name, @Nullable @RequestParam String category) {
        if (name != null && category == null) {
            return recipeService.getAllRecipes()
                    .stream()
                    .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                    .distinct()
                    .sorted(Comparator.comparing((Recipe::getDate)).reversed())
                    .toList();

        } else if (name == null && category != null) {
            return recipeService.getAllRecipes()
                    .stream()
                    .filter(r -> r.getCategory().equalsIgnoreCase(category))
                    .distinct()
                    .sorted(Comparator.comparing((Recipe::getDate)).reversed())
                    .toList();
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
