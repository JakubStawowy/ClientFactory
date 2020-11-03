package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;
import Socket.SocketConnector;
public class Client extends SocketConnector {
    private String id;
    public Client() throws IOException {
        socket = new Socket("localhost", 4999);
        printWriter = new PrintWriter(socket.getOutputStream());
        id = UUID.randomUUID().toString();
        System.out.println("Client on\nClient id: "+id);
        sendMessage(id);
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
    @Override
    public void receiveMessage() throws IOException {
        while (!socket.isClosed()){
            try{
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String feedback = bufferedReader.readLine();
                if(feedback == null || feedback.equals("end")){
                    socket.close();
                    break;
                }
                System.out.println("[RECEIVER] Message "+feedback+" received");
                System.out.println("-----------------------------------------------------------------------------");
            }catch (Exception ignored){
                socket.close();
            }
        }
        System.out.println("[RECEIVER] Socket closed");
    }
    @Override
    public void sendMessage() throws IOException {
        while(!socket.isClosed()){
            Scanner scanner = new Scanner(System.in);
            System.out.println("[SENDER] Type message");
            String message = scanner.nextLine();
            sendMessage(message);
            System.out.println("[SENDER] Message "+message+" sended");
            if(message.toLowerCase().equals("end"))
                socket.close();
        }
        System.out.println("[SENDER] Socket closed");
    }
    public static void main(String[] args){
        try {
            new Client();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
