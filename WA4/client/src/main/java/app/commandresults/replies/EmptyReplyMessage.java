package app.commandresults.replies;

import app.commandresults.IMessage;

public class EmptyReplyMessage implements IMessage {
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
