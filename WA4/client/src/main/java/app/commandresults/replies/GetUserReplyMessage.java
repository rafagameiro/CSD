package app.commandresults.replies;

import app.commandresults.IMessage;
import app.models.User;

import java.io.*;

public class GetUserReplyMessage implements IMessage {

    private User user;

    public GetUserReplyMessage(User user) {
        this.user = user;
    }

    public static GetUserReplyMessage deserialize(byte[] message) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                String username = dis.readUTF();
                String password = dis.readUTF();
                return new GetUserReplyMessage(new User(username, password));
            }
        }
    }

    public User getUser() {
        return user;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(user.getUsername());
                dos.writeUTF(user.getPassword());

                return bos.toByteArray();
            }
        }
    }
}
