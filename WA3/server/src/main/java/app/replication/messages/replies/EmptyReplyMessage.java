package app.replication.messages.replies;

import app.replication.messages.IMessage;

public class EmptyReplyMessage implements IMessage {
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
