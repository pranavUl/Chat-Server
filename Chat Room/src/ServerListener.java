import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;




public class ServerListener implements Runnable {
    private ServerSocket serverSocket;
    private Set<String> usernames = new HashSet<>();
    private ArrayList<ObjectOutputStream> outputStreams = new ArrayList<>();




    public ServerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }




    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());




                outputStreams.add(outputStream);




                Thread clientThread = new Thread(new ClientHandler(inputStream, outputStream));
                clientThread.start();
            }
        } catch (IOException e) {
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
        public void run() {
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
                            if (!usernames.contains(message.getSender())) {
                                username = message.getSender();
                                usernames.add(username);

                            }
                            broadcastMessage(new MessageToClient(message.getSender(), message.getMessage(), new ArrayList<>(usernames)));
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



