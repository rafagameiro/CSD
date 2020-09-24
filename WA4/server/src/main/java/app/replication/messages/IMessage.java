package app.replication.messages;

import java.io.IOException;

public interface IMessage {

    byte[] getBytes() throws IOException;
}
