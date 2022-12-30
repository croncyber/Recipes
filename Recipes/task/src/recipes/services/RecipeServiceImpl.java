package recipes.services;

import org.springframework.stereotype.Service;
import recipes.models.Recipe;
import recipes.repositories.RecipeRepository;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository repository;

    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        repository.save(recipe);
    }


    @Override
    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) repository.findAll();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteRecipeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean findRecipe(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean isAuthorRecipe(Long id, String login) {
        return repository.existsByIdAndUsersInRecipe_Email(id, login);
    }
}
