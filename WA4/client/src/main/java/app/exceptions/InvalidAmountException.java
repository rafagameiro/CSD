package app.exceptions;

public class InvalidAmountException extends InvalidCommandException {

    public InvalidAmountException() {
        super("Amount is not valid, try again!");
    }
}
