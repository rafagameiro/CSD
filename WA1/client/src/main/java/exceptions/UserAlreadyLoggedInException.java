package exceptions;

public class UserAlreadyLoggedInException extends InvalidCommandException {
    public UserAlreadyLoggedInException() {
        super("There is a user already logged in!");
    }
}
