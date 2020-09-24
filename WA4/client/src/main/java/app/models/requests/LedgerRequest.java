package app.models.requests;

public class LedgerRequest {
    String contract;
    String signature;

    public LedgerRequest() {

    }

    public LedgerRequest(String contract) {
        this(contract, null);
    }

    public LedgerRequest(String contract, String signature) {
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
