import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) {
        try {

            String username;
            boolean validName = false;
            do {
                username = JOptionPane.showInputDialog("Enter username: ");
                if (username != null && !username.trim().isEmpty()) {
                    validName = true;
                }
                else {
                    JOptionPane.showMessageDialog(null, "Pleae enter a valid username");
                }
            }
            while (!validName);


            // create a connection to server
            Socket socket = new Socket("127.0.0.1", 8011);
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());


            // Determine if playing as X or O
            CommandFromServer cfs = (CommandFromServer) is.readObject();
            ChatFrame frame;


            // Create the Frame based on which player the server says this client is
            /*if (cfs.getCommand() == CommandFromServer.CONNECTED_AS_X) {
                frame = new Connect4Frame(chatData, os, 'X');
                frame.setText("Waiting for Black to connect"); // Set initial text
            } else {
                frame = new Connect4Frame(chatData, os, 'O');
            }*/


            // Starts a thread that listens for commands from the server
            ClientListener cl = new ClientListener(is, os, frame);
            Thread t = new Thread(cl);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}