package recipes.services;

import recipes.models.User;


public interface UserService {
    User saveUser(User user);
    User getUserByEmail(String email);
    boolean existUser(String email);
}
