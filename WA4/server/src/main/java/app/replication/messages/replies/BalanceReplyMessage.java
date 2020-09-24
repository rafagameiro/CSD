package app.replication.messages.replies;

import app.replication.messages.IMessage;

import java.io.*;

public class BalanceReplyMessage implements IMessage {

    private Float amount;

    public BalanceReplyMessage(Float amount) {
        this.amount = amount;
    }

    public static BalanceReplyMessage deserialize(byte[] message) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                Float amount = dis.readFloat();
                return new BalanceReplyMessage(amount);
            }
        }
    }

    public Float getAmount() {
        return amount;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeFloat(this.amount);
                return bos.toByteArray();
            }
        }
    }
}
