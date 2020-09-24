package app.models;

public class HashSignaturePair {
    private byte[] hash;
    private byte[] signature;
    private int senderId;

    public HashSignaturePair(byte[] hash, byte[] signature, int senderId) {
        this.hash = hash;
        this.signature = signature;
        this.senderId = senderId;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public int getSenderId() {
        return senderId;
    }

}
