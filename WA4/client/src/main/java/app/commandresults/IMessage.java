package app.commandresults;

import java.io.IOException;

public interface IMessage {

    byte[] getBytes() throws IOException;
}
