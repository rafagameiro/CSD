package app.replication.messages.requests;

import app.replication.messages.IMessage;

import java.io.*;

public class LedgerRequestMessage implements IMessage {

    private String username;

    public LedgerRequestMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(this.username);

                return bos.toByteArray();
            }
        }
    }

    public static LedgerRequestMessage deserialize(byte[] message) throws IOException {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                String username = dis.readUTF();
                return new LedgerRequestMessage(username);
            }
        }
    }
}
