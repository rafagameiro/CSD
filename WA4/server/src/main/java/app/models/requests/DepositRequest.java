package app.models.requests;

public class DepositRequest {
    float amount;
    String contract;
    String signature;

    public DepositRequest() {
    }

    public DepositRequest(float amount, String contract) {
        this(amount, contract, null);
    }

    public DepositRequest(float amount, String contract, String signature) {
        this.contract = contract;
        this.amount = amount;
        this.signature = signature;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
