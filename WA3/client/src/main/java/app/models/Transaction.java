package app.models;

import java.util.UUID;

public class Transaction {
    private UUID id;
    private String from, to;
    private float amount;

    public Transaction() {

    }

    public Transaction(UUID id, String from, String to, float amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Transaction(String from, String to, float amount) {
        this(UUID.nameUUIDFromBytes(new byte[0]), from, to, amount);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "[" + from + "->" + to + ":" + amount + "€]";
    }
}
