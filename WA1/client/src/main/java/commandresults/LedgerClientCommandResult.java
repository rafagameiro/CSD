package commandresults;

import models.Transaction;

import java.util.List;

public class LedgerClientCommandResult implements CommandResult {
    private List<Transaction> ledger;

    public LedgerClientCommandResult(List<Transaction> ledger) {
        this.ledger = ledger;
    }

    @Override
    public void show() {
        if (ledger == null) {
            System.out.println("User not found!");
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
