package g2.labs.exceptions;

public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException() {
        super("Insufficient amount!");
    }

}
