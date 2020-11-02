package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private String id;
    public Client() throws IOException {
        Socket socket = new Socket("localhost", 4999);
        id = UUID.randomUUID().toString();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        System.out.println("Client on");
        System.out.println("Client id: "+id);
        printWriter.println(id);
        printWriter.flush();
        while(true){
            String message = scanner.nextLine();
            printWriter.println(message);
            printWriter.flush();
            System.out.println("Message "+message+" sended");
            if(message.equals("end"))
                break;
            String feedback = bufferedReader.readLine();
            System.out.println("Message "+feedback+" received");
        }
    }
    public String getId(){
        return id;
    }
    public static void main(String[] args){
        try {
            Client client = new Client();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
