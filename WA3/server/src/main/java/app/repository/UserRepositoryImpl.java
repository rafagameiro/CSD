package app.repository;

import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private UserRepositoryWrapper repository;

    @Autowired
    public UserRepositoryImpl(UserRepositoryWrapper repository) {
        this.repository = repository;
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        Optional<User> user = repository.findById(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    @Override
    public void save(User user) throws UserAlreadyExistsException {
        Optional<User> old = repository.findById(user.getUsername());
        if (old.isPresent()) {
            throw new UserAlreadyExistsException();

        }

        repository.save(user);
    }
}
