package Factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import Socket.SocketConnector;
public class ClientHandler extends SocketConnector implements Observer, Runnable {
    private Server server;
    private String clientId;
    private String feedback;
    private BufferedReader bufferedReader;
    public ClientHandler(Socket client, Server server) throws IOException {
        this.socket = client;
        this.server = server;
        printWriter = new PrintWriter(client.getOutputStream());
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
        clientId = bufferedReader.readLine();
        System.out.println("New client added. Client ID: "+clientId);
    }

    @Override
    public void run() {
        try {
            addClientToDatabase();
            setClientNotWorking();
            Thread receiver = new Thread(() -> {
                try {
                    receiveMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiver.start();

            Thread sender = new Thread(this::sendMessage);
            sender.start();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void receiveMessage() throws IOException {

        while(!socket.isClosed()){
            try{
                String message = bufferedReader.readLine();
                if(message != null){
                    switch (message) {
                        case "first":
                            feedback = server.getFirstProductManufacture().createProduct().showProduct();
                            break;
                        case "second":
                            feedback = server.getSecondProductManufacture().createProduct().showProduct();
                            break;
                        default:
                            feedback = "Cannot find product";
                            break;
                    }
                }
            }catch (SocketException ignored){

            }
        }
    }
    @Override
    public void sendMessage(){
        while(!socket.isClosed()){
            if(feedback!=null){
                for (Observer observer: server.getObservers()) {
                    observer.update();
                }
                sendMessage(feedback);
                feedback = null;
            }
        }
    }


    private void addClientToDatabase() throws SQLException {
        String query = "INSERT INTO client VALUES(0, \""+clientId+"\", now(), true);";
        server.getDatabaseConnector().execute(query);
    }
    private void setClientNotWorking() throws SQLException {
        String query = "UPDATE client SET working=false WHERE CID=\""+clientId+"\";";
        server.getDatabaseConnector().execute(query);
    }
    @Override
    public String getFeedback(){
        return feedback;
    }
    @Override
    public void update() {
        sendMessage("Server active");
    }

    @Override
    public void close() {
        sendMessage("end");
    }
}
