package app.models.requests;

import app.models.Transaction;

public class TransferRequest {
    Transaction transaction;
    String contract;
    String signature;

    public TransferRequest() {
    }

    public TransferRequest(Transaction transaction, String contract, String signature) {
        this.contract = contract;
        this.transaction = transaction;
        this.signature = signature;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
