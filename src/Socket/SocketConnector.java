package Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public interface SocketConnector {
    void sendMessage() throws IOException;
    void receiveMessage() throws IOException;
}
