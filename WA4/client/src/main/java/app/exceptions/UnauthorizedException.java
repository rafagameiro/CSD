package app.exceptions;

public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
        super("The currently logged user does not have authorization to perform the requested operation!");
    }
}
