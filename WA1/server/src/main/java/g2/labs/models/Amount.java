package g2.labs.models;

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

    public void add(Amount amount) {
        this.amount += amount.getAmount();
    }

    public boolean canWithdraw(Amount amount) {
        return this.amount >= amount.getAmount();
    }

    public void withdraw(Amount amount) {
        this.amount -= amount.getAmount();
    }
}
