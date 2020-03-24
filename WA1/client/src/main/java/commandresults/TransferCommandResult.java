package commandresults;

import org.springframework.http.HttpStatus;

public class TransferCommandResult implements CommandResult {
    private static final String SUCCESS_MSG = "Transfer was successful!";
    private static final String RECEIVER_ERROR_MSG = "The receiver does not exists!";
    private static final String BALANCE_ERROR_MSG = "Insufficient amount!";

    private HttpStatus status;

    public TransferCommandResult(HttpStatus status) {
        this.status = status;
    }

    @Override
    public void show() {
        switch (this.status) {
            case OK:
                System.out.println(SUCCESS_MSG);
                break;
            case NOT_FOUND:
                System.out.println(RECEIVER_ERROR_MSG);
                break;
            case BAD_REQUEST:
                System.out.println(BALANCE_ERROR_MSG);
                break;
        }
    }
}
