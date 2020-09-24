package app.commandresults;

public class DepositCommandResult implements CommandResult {
    private static final String SUCCESS_MSG = "Deposit was successful!";

    private String errorMsg;
    private boolean success;

    public DepositCommandResult() {
        this.success = true;
    }

    public DepositCommandResult(String errorMsg) {
        this.success = false;
        this.errorMsg = errorMsg;
    }

    @Override
    public void show() {
        String msg = success ? SUCCESS_MSG : errorMsg;
        System.out.println(msg);
    }
}
