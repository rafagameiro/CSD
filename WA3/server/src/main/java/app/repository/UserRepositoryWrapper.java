package app.repository;

import app.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepositoryWrapper extends CrudRepository<User, String> {

}
