package app.replication.messages.requests;

import app.replication.messages.IMessage;

import java.io.*;

public class CreateUserRequestMessage implements IMessage {

    private String username;
    private String password;

    public CreateUserRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(this.username);
                dos.writeUTF(this.password);

                return bos.toByteArray();
            }
        }
    }

    public static CreateUserRequestMessage deserialize(byte[] message) throws IOException {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                String username = dis.readUTF();
                String password = dis.readUTF();
                return new CreateUserRequestMessage(username, password);
            }
        }
    }
}
