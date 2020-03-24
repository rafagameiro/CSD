package g2.labs.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found!");
    }

}
