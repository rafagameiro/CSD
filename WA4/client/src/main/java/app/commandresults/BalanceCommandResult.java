package app.commandresults;

import app.models.Amount;

public class BalanceCommandResult implements CommandResult {
    private static final String SUCCESS_MSG = "Balance: %.2f €";
    private String errorMsg;

    private boolean success;
    private Amount amount;

    public BalanceCommandResult(Amount amount) {
        this.success = true;
        this.amount = amount;
    }

    public BalanceCommandResult(String errorMsg) {
        this.success = false;
        this.errorMsg = errorMsg;
    }

    @Override
    public void show() {
        String msg = success ? String.format(SUCCESS_MSG, amount.getAmount()) : errorMsg;
        System.out.println(msg);
    }
}
