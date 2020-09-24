package app.commandresults;

import app.models.Amount;

public class BalanceCommandResult implements CommandResult {
    private static final String SUCCESS_MSG = "Balance: %.2f €";
    private static final String ERROR_MSG = "Could not retrieve balance from the logged account, try to re-log!";

    private boolean success;
    private Amount amount;

    public BalanceCommandResult(boolean success, Amount amount) {
        this.success = success;
        this.amount = amount;
    }

    @Override
    public void show() {
        String msg = success ? String.format(SUCCESS_MSG, amount.getAmount()) : ERROR_MSG;
        System.out.println(msg);
    }
}
