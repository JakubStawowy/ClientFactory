package Client;
import Socket.SocketConnector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client implements SocketConnector {
    private String id;
    private Socket socket;
    private PrintWriter printWriter;
    public Client() throws IOException {
        socket = new Socket("localhost", 4999);
        printWriter = new PrintWriter(socket.getOutputStream());
        id = UUID.randomUUID().toString();
        System.out.println("Client on");
        System.out.println("Client id: "+id);
        printWriter.println(id);
        printWriter.flush();
        Thread sender = new Thread(() -> {
            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sender.start();

        Thread receiver = new Thread(() -> {
            try {
                receiveMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiver.start();
    }
    public String getId(){
        return id;
    }
    @Override
    public void receiveMessage() throws IOException {
        while (!socket.isClosed()){
            try{
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String feedback = bufferedReader.readLine();
                System.out.println("Message "+feedback+" received");
            }catch (Exception ignored){
                socket.close();
            }
        }

    }
    @Override
    public void sendMessage() throws IOException {
        while(!socket.isClosed()){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type message");
            String message = scanner.nextLine();
            printWriter.println(message);
            printWriter.flush();
            System.out.println("Message "+message+" sended");
            if(message.equals("end")){
                socket.close();
            }
        }
    }
    public static void main(String[] args){
        try {
            new Client();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
