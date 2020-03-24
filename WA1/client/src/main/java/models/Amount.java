package models;

public class Amount {
    private float amount;

    public Amount(float amount) {
        this.amount = amount;
    }

    public Amount() {
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount);
    }
}
