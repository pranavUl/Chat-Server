import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private static ObjectOutputStream outputStream;
    public static void main(String[] args) {
        try {

            String username;
            boolean validName = false;
            Scanner scanner = new Scanner(System.in);
            do {
                System.out.print("Enter your Username: ");
                username = scanner.nextLine();
                if (username != null && !username.trim().isEmpty()) {
                    validName = true;
                }
                else {
                    System.out.println("Please enter a valid username: ");
                }
            }
            while (!validName);


            // create a connection to server
            Socket socket = new Socket("127.0.0.1", 8011);
            outputStream = new ObjectOutputStream(socket.getOutputStream());


            // Determine if playing as X or O
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());


            ChatFrame chatFrame = new ChatFrame(username);
            chatFrame.setObjectOutputStream(outputStream); // Set ObjectOutputStream


            outputStream.writeObject(new MessageFromClient(username, "has joined"));


            ClientListener clientListener = new ClientListener(inputStream, chatFrame);
            Thread listenerThread = new Thread(clientListener);
            listenerThread.start();


            // Add the username to the user list on the chat frame
            chatFrame.addUser(username);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ObjectOutputStream getOutputStream() {
        return outputStream;
    }

}