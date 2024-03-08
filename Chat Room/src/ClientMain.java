import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) {
        try {
            // create an object for the Connect4 game
            ChatData chatData = new ChatData();


            // create a connection to server
            Socket socket = new Socket("127.0.0.1", 8011);
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());


            // Determine if playing as X or O
            CommandFromServer cfs = (CommandFromServer) is.readObject();
            Connect4Frame frame;


            // Create the Frame based on which player the server says this client is
            if (cfs.getCommand() == CommandFromServer.CONNECTED_AS_X) {
                frame = new Connect4Frame(chatData, os, 'X');
                frame.setText("Waiting for Black to connect"); // Set initial text
            } else {
                frame = new Connect4Frame(chatData, os, 'O');
            }


            // Starts a thread that listens for commands from the server
            ClientsListener cl = new ClientsListener(is, os, frame);
            Thread t = new Thread(cl);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}