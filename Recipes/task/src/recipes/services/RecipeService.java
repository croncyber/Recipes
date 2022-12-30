package recipes.services;

import recipes.models.Recipe;

import java.util.List;


public interface RecipeService {
    void saveRecipe(Recipe recipe);

    List<Recipe> getAllRecipes();

    Recipe getRecipeById(Long id);

    void deleteRecipeById(Long id);

    boolean findRecipe(Long id);

    boolean isAuthorRecipe(Long id, String login);

}