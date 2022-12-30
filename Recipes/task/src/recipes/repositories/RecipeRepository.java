package recipes.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import recipes.models.Recipe;


@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query("""
            select (count(r) > 0) from Recipe r inner join r.usersInRecipe usersInRecipe
            where r.id = ?1 and usersInRecipe.email = ?2""")
    boolean existsByIdAndUsersInRecipe_Email(@NonNull Long id, @NonNull String email);

}
