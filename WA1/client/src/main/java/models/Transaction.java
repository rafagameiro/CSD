package models;

public class Transaction {
    private String from, to;
    private Amount amount;

    public Transaction(String from, String to, Amount amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Transaction() {

    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Amount getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "[" + from + "->" + to + ":" + amount + "€]";
    }
}
