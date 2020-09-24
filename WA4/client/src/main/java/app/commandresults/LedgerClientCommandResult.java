package app.commandresults;

import app.models.Transaction;

import java.util.Set;

public class LedgerClientCommandResult implements CommandResult {
    private Set<Transaction> ledger;
    private String errorMsg;

    public LedgerClientCommandResult(Set<Transaction> ledger) {
        this.ledger = ledger;
    }

    public LedgerClientCommandResult(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void show() {
        if (ledger == null) {
            System.out.println(errorMsg);
        } else {
            System.out.println("Ledger:");
            if (ledger.isEmpty()) {
                System.out.println("[Empty]");
            }
            for (Transaction t : ledger) {
                System.out.println(t);
            }
        }
    }
}
