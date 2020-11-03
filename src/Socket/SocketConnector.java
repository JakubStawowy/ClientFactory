package Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SocketConnector {

    protected Socket socket;
    protected PrintWriter printWriter;
    protected abstract void sendMessage() throws IOException;
    protected abstract void receiveMessage() throws IOException;
    protected void sendMessage(String message){
        printWriter.println(message);
        printWriter.flush();
    }
}
