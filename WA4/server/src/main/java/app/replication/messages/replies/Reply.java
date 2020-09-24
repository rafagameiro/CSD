package app.replication.messages.replies;

import app.replication.messages.IMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Reply {

    private ReplyType type;
    private IMessage message;

    public Reply(ReplyType type, IMessage message) {
        this.type = type;
        this.message = message;
    }

    public Reply(ReplyType type) {
        this(type, new EmptyReplyMessage());
    }

    public ReplyType getType() {
        return type;
    }

    public IMessage getMessage() {
        return message;
    }

    public byte[] serialize() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeInt(this.type.ordinal());
                byte[] msg = message.getBytes();
                dos.write(msg);

                return bos.toByteArray();
            }
        } catch (IOException e) {
            return new byte[0];
        }
    }

}
