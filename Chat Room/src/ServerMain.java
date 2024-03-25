import java.io.IOException;
import java.net.ServerSocket;




public class ServerMain {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8011);
            System.out.println("Server is running...");
            ServerListener serverListener = new ServerListener(serverSocket);
            Thread listenerThread = new Thread(serverListener);
            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}














