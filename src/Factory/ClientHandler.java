package Factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Observer, Runnable {
    private Socket client;
    private Server server;
    private String clientId;
    private PrintWriter printWriter;
    public ClientHandler(Socket client, Server server) {
        this.client = client;
        this.server = server;
}

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
            printWriter = new PrintWriter(client.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            clientId = bufferedReader.readLine();
            addClientToDatabase();
            String feedback;
            label:
            while (true) {
                String message = bufferedReader.readLine();
                switch (message) {
                    case "end":
                        server.getServerSocket().close();
                        break label;
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
                for (Observer observer: server.getObservers()) {
                    observer.update();
                }
                printWriter.println(feedback);
                printWriter.flush();
            }
            setClientNotWorking();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
    private void addClientToDatabase() throws SQLException {
        String query = "INSERT INTO clients VALUES(0, \""+clientId+"\", now(), true);";
        server.getDatabaseConnector().execute(query);
    }
    private void setClientNotWorking() throws SQLException {
        String query = "UPDATE clients SET working=false WHERE CID=\""+clientId+"\";";
        server.getDatabaseConnector().execute(query);
    }

    @Override
    public void update() {

    }
}
