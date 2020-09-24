package app.exceptions;

public class InvalidTransactionException extends InvalidCommandException {
    public InvalidTransactionException() {
        super("Invalid transaction, use deposit to transfer to yourself!");
    }
}
