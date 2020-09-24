package app.exceptions;

public class UserAlreadyExistsException extends RepositoryException {

    public UserAlreadyExistsException() {
        super("User already exists!");
    }

}
