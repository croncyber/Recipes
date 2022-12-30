package recipes.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import recipes.models.User;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(@NonNull String email);
    Optional<User> findByEmail(@Nullable String email);
}
