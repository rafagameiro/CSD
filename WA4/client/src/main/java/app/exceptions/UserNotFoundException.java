package app.exceptions;

public class UserNotFoundException extends RepositoryException {

    public UserNotFoundException() {
        super("User not found!");
    }

}
