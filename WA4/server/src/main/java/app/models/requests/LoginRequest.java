package app.models.requests;

public class LoginRequest {
    String contract;
    String signature;

    public LoginRequest() {

    }

    public LoginRequest(String contract, String signature) {
        this.contract = contract;
        this.signature = signature;
    }

    public LoginRequest(String contract) {
        this(contract, null);
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
