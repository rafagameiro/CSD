package app.replication.messages.requests;

import app.replication.messages.IMessage;

import java.io.*;

public class CreateUserRequestMessage implements IMessage {

    private String username;
    private String password;
    private byte[] certificate;

    public CreateUserRequestMessage(String username, String password, byte[] certificate) {
        this.username = username;
        this.password = password;
        this.certificate = certificate;
    }

    public static CreateUserRequestMessage deserialize(byte[] message) throws IOException {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                String username = dis.readUTF();
                String password = dis.readUTF();
                byte[] certificate = new byte[dis.readInt()];
                dis.read(certificate);
                return new CreateUserRequestMessage(username, password, certificate);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(this.username);
                dos.writeUTF(this.password);
                dos.writeInt(this.certificate.length);
                dos.write(this.certificate);

                return bos.toByteArray();
            }
        }
    }
}
