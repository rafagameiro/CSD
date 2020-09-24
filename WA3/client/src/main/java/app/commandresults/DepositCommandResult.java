package app.commandresults;

public class DepositCommandResult implements CommandResult {
    private static final String SUCCESS_MSG = "Deposit was successful!";
    private static final String ERROR_MSG = "Could not deposit amount into the logged account, try to re-log!";

    private boolean success;

    public DepositCommandResult(boolean success) {
        this.success = success;
    }

    @Override
    public void show() {
        String msg = success ? SUCCESS_MSG : ERROR_MSG;
        System.out.println(msg);
    }
}
