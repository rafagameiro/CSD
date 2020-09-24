package app.models.requests;

public class BalanceRequest {
    String contract;
    String signature;

    public BalanceRequest() {

    }

    public BalanceRequest(String contract) {
        this(contract, null);
    }

    public BalanceRequest(String contract, String signature) {
        this.contract = contract;
        this.signature = signature;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
