package Factory;

import Client.Client;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private DatabaseConnector databaseConnector;
    private final int port = 4999;
    private final int maximumNumberOfClients = 20;
    private ExecutorService pool = Executors.newFixedThreadPool(maximumNumberOfClients);
    private Manufacture firstProductManufacture = new FirstProductManufacture();
    private Manufacture secondProductManufacture = new SecondProductManufacture();
    private List<Observer> observers = new ArrayList<>();
    public Server() {
        try {
            databaseConnector = DatabaseConnector.getInstance();
            System.out.println("Database connected");
            serverSocket = new ServerSocket(port);
            System.out.println("Server on");
            Thread listener = new Thread(() -> {
                try{
                    Scanner scanner = new Scanner(System.in);
                    while (!serverSocket.isClosed()){
                        String command = scanner.nextLine().toLowerCase();
                        if(command.equals("end") || command.equals("quit")){
                            serverSocket.close();
                            for(Observer observer: observers)
                                observer.close();
                        }
                        else if(command.equals("help"))
                            System.out.println("To close connection, type help or quit");
                        else
                            System.out.println(command+" - unrecognized command");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            listener.start();
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client, this);
                observers.add(clientHandler);
                pool.execute(clientHandler);
            }
            System.out.println("Koniec");
            databaseConnector.disconnect();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public ServerSocket getServerSocket(){
        return serverSocket;
    }
    public DatabaseConnector getDatabaseConnector(){
        return this.databaseConnector;
    }
    public Manufacture getFirstProductManufacture(){
        return firstProductManufacture;
    }
    public Manufacture getSecondProductManufacture(){
        return secondProductManufacture;
    }
    public List<Observer> getObservers(){
        return observers;
    }
    public static void main(String[] args){
            new Server();
    }
}
