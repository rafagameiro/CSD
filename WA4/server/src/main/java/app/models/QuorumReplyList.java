package app.models;

import java.util.List;

public class QuorumReplyList {
    private List<HashSignaturePair> signatures;
    private byte[] message;

    public QuorumReplyList(List<HashSignaturePair> signatures, byte[] message) {
        this.signatures = signatures;
        this.message = message;
    }

    public List<HashSignaturePair> getSignatures() {
        return signatures;
    }

    public byte[] getMessage() {
        return message;
    }
}
