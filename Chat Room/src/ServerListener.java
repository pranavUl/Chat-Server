import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ServerListener implements Runnable {
    private ServerSocket serverSocket;

    private Set<String> usernames = new HashSet<>();

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
                cleintThread.start();
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
            try {
                while (true) {
                    MessageFromClient message = (MessageFromClient) inputStream.readObject();
                    if (message != null) {
                        if (message.getMessage().equalsIgnoreCase("exit")) {
                            usernames.remove(username);
                            broadcastMessage(new MessageToClient(username, "exit", new ArrayList<>(usernames)));
                            outputStreams.remove(outputStream);
                            break;
                        } else {
                            String newUsername = message.getSender().toLowerCase().trim();
                            if (!usernames.contains(newUsername)) {
                                if (!newUsername.isEmpty()) { // Ensure non-empty username
                                    username = newUsername;
                                    usernames.add(username);
                                } else {
                                    // Send a message to the client informing about invalid username
                                    outputStream.writeObject(new MessageToClient("", "Invalid username", new ArrayList<>(usernames)));
                                    continue; // Skip broadcasting
                                }
                            } else {
                                // Send a message to the client informing about duplicate username
                                outputStream.writeObject(new MessageToClient("", "Username already exists", new ArrayList<>(usernames)));
                                continue; // Skip broadcasting
                            }
                            broadcastMessage(new MessageToClient(username, message.getMessage(), new ArrayList<>(usernames)));
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
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