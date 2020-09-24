package app.models;

public class HashSignaturePair {
    private byte[] hash;
    private byte[] signature;
    private int senderId;

    public HashSignaturePair() {

    }

    public HashSignaturePair(byte[] hash, byte[] signature, int senderId) {
        this.hash = hash;
        this.signature = signature;
        this.senderId = senderId;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
}
