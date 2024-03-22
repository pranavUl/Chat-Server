import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain
{
    public static void main(String[] args)
    {
        try
        {
            // creates a socket that allows connections on port 8001
            ServerSocket serverSocket = new ServerSocket(8011);

            // allow X to connect and build streams to / from X
            /*Socket xCon = serverSocket.accept();
            ObjectOutputStream xos = new ObjectOutputStream(xCon.getOutputStream());
            ObjectInputStream xis = new ObjectInputStream(xCon.getInputStream());

            // Lets the client know they are the X player
            xos.writeObject(new CommandFromServer(CommandFromServer.CONNECTED_AS_X,null));
            System.out.println("X has Connected.");*/

            // Creates a Thread to listen to the X client
            System.out.println("Server is running...");// check to see if running
            ServerListener sl = new ServerListener(serverSocket);
            Thread t = new Thread(sl);
            t.start();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}