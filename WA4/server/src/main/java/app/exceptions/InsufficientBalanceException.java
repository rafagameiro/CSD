package app.exceptions;

public class InsufficientBalanceException extends RepositoryException {

    public InsufficientBalanceException() {
        super("Insufficient amount!");
    }

}
