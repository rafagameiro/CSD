package app.commandresults;

import app.models.Transaction;

import java.util.Set;

public class LedgerGlobalCommandResult implements CommandResult {
    private Set<Transaction> ledger;

    public LedgerGlobalCommandResult(Set<Transaction> ledger) {
        this.ledger = ledger;
    }

    @Override
    public void show() {
        System.out.println("Ledger:");
        if (ledger.isEmpty()) {
            System.out.println("[Empty]");
        }
        for(Transaction t : ledger) {
            System.out.println(t);
        }
    }
}
