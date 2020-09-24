package app.exceptions;

public class InvalidAmountException extends Exception {

    public InvalidAmountException() {
        super("Amount is not valid, try again!");
    }
}
