package recipes.models;

import recipes.models.Recipe;

import java.io.Serializable;

/**
 * A DTO for the {@link Recipe} entity
 */
public record RecipeDtoResponse(Long id) implements Serializable {
}