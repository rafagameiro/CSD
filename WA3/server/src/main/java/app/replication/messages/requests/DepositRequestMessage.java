package app.replication.messages.requests;

import app.replication.messages.IMessage;

import java.io.*;

public class DepositRequestMessage implements IMessage {

    private String username;
    private Float amount;

    public DepositRequestMessage(String username, Float amount) {
        this.username = username;
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(this.username);
                dos.writeFloat(this.amount);

                return bos.toByteArray();
            }
        }
    }

    public static DepositRequestMessage deserialize(byte[] message) throws IOException {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                String username = dis.readUTF();
                Float amount = dis.readFloat();
                return new DepositRequestMessage(username, amount);
            }
        }
    }
}
