package app.models;

//Used as a model to transfer json data between the c/s interactions
public class Amount {

    private float amount;

    public Amount() {

    }

    public Amount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
