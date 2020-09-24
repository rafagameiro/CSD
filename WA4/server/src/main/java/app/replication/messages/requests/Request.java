package app.replication.messages.requests;

import app.replication.messages.IMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request {

    private RequestType type;
    private IMessage message;
    private PermissionProof permissionProof;

    public Request(RequestType type, IMessage message, PermissionProof permissionProof) {
        this.type = type;
        this.message = message;
        this.permissionProof = permissionProof;
    }

    public Request(RequestType type, IMessage message) {
        this.type = type;
        this.message = message;
        this.permissionProof = null;
    }

    public RequestType getType() {
        return type;
    }

    public IMessage getMessage() {
        return message;
    }

    public PermissionProof getPermissionProof() {
        return permissionProof;
    }

    public byte[] serialize() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeInt(this.type.ordinal());
                byte[] msg = message.getBytes();
                dos.writeInt(msg.length);
                dos.write(msg);
                if(permissionProof==null){
                    dos.writeInt(0);
                }else{
                    byte[] proofBytes = permissionProof.getBytes();
                    dos.writeInt(proofBytes.length);
                    dos.write(proofBytes);
                }

                return bos.toByteArray();
            }
        }
    }
}
