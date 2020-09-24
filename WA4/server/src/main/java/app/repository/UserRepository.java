package app.repository;

import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import app.models.User;

public interface UserRepository {


    User findByUsername(String username) throws UserNotFoundException;

    void save(User user) throws UserAlreadyExistsException;
}
