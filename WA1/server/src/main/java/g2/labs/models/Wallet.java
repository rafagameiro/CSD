package g2.labs.models;

import java.util.LinkedList;
import java.util.List;

public class Wallet {
    private Amount amount;
    private List<Transaction> ledger;

    public Wallet() {
        this.amount = new Amount(0);
        this.ledger = new LinkedList<>();
    }

    public Amount getAmount() {
        return amount;
    }

    public void add(Amount amount) {
        this.amount.add(amount);
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setLedger(List<Transaction> ledger) {
        this.ledger = ledger;
    }

    public void addToLedger(Transaction transaction) {
        this.ledger.add(transaction);
    }

    public List<Transaction> getLedger() {
        return this.ledger;
    }
}
