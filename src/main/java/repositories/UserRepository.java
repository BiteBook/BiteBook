package repositories;

import models.User;

public interface UserRepository {
    User findByUsername(String username);
}
