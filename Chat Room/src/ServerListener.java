import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener implements Runnable {
    private ServerSocket serverSocket;

    private ArrayList<ObjectOutputStream> outputStreams = new ArrayList<>();


    public ServerListener(ServerSocket s) {
        this.serverSocket = s;
    }

    @Override
    public void run() {
        try
        {
            while(true)
            {

                Socket socket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStreams.add(outputStream);

                Thread cleintThread = new Thread(new ClientHandler(inputStream, outputStream)); //WLL ADD LATER
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private class ClientHandler implements Runnable {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private String username;

        public ClientHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run(){
            
        }
    }



    private synchronized void broadcastMessage(MessageToClient message) {
        for (ObjectOutputStream outputStream : outputStreams) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}