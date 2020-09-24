package app.exceptions;

public class ServerErrorException extends RepositoryException {

    public ServerErrorException() {
        super("There was a problem processing the request, operation did not execute. Try again!");
    }
}
