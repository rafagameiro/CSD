package app.exceptions;

public class UserNotLoggedInException extends InvalidCommandException {
    public UserNotLoggedInException() {
        super("There is no user logged in!");
    }
}
