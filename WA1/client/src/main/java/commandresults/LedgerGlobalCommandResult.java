package commandresults;

import models.Transaction;

import java.util.List;

public class LedgerGlobalCommandResult implements CommandResult {
    private List<Transaction> ledger;

    public LedgerGlobalCommandResult(List<Transaction> ledger) {
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
