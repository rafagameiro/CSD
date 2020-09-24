package app.models;

import java.io.*;

public class QuorumReply {
    private int senderId;
    private byte[] message;
    private byte[] hash;
    private byte[] signature;

    public QuorumReply() {

    }

    public QuorumReply(int senderId, byte[] message, byte[] hash, byte[] signature) {
        this.senderId = senderId;
        this.message = message;
        this.hash = hash;
        this.signature = signature;
    }

    public static QuorumReply deserialize(byte[] message) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                int senderId = dis.readInt();

                byte[] content = new byte[dis.readInt()];
                dis.read(content);

                byte[] hash = new byte[dis.readInt()];
                dis.read(hash);

                byte[] signature = new byte[dis.readInt()];
                dis.read(signature);

                return new QuorumReply(senderId, content, hash, signature);
            }
        }
    }

    public int getSenderId() {
        return senderId;
    }

    public byte[] getMessage() {
        return message;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeInt(this.senderId);

                dos.writeInt(this.message.length);
                dos.write(this.message);

                dos.writeInt(this.hash.length);
                dos.write(this.hash);

                dos.writeInt(this.signature.length);
                dos.write(this.signature);

                return bos.toByteArray();
            }
        }
    }
}
