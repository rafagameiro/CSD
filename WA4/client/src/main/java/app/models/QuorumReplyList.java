package app.models;

import java.util.List;

public class QuorumReplyList {

    private List<HashSignaturePair> signatures;
    private byte[] message;

    public QuorumReplyList(List<HashSignaturePair> signatures, byte[] message) {
        this.signatures = signatures;
        this.message = message;
    }

    public QuorumReplyList(){

    }

    public List<HashSignaturePair> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<HashSignaturePair> signatures) {
        this.signatures = signatures;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
