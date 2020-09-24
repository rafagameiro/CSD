package app.replication.messages.requests;

import java.io.*;

public class PermissionProof {
    private String username;
    private String contract;
    private String signature;

    public PermissionProof(String username, String contract, String signature) {
        this.username = username;
        this.contract = contract;
        this.signature = signature;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static PermissionProof deserialize(byte[] bytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                String username = dis.readUTF();
                String contract = dis.readUTF();
                String signature = dis.readUTF();
                return new PermissionProof(username,contract,signature);
            }
        }
    }

    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(this.username);
                dos.writeUTF(this.contract);
                dos.writeUTF(this.signature);

                return bos.toByteArray();
            }
        }
    }
}
