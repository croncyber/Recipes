package recipes.services;

import org.springframework.stereotype.Service;
import recipes.models.User;
import recipes.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {


    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existUser(String email) {
        return repository.existsByEmail(email);
    }
}
