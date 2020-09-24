package app.commandresults;

public class TransferCommandResult implements CommandResult {
    private static final String SUCCESS_MSG = "Transfer was successful!";
    private static final String RECEIVER_ERROR_MSG = "The receiver does not exists!";
    private static final String BALANCE_ERROR_MSG = "Insufficient amount!";

    private boolean success;
    private String errorMsg;

    public TransferCommandResult() {
        this.success = true;
    }

    public TransferCommandResult(Exception e) {
        success = false;
        errorMsg = e.getMessage();
    }

    @Override
    public void show() {
        String msg = success ? SUCCESS_MSG : errorMsg;
        System.out.println(msg);
    }
}
