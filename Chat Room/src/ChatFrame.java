import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class ChatFrame extends JFrame implements ActionListener {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton exitButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private String username;
    private ObjectOutputStream outputStream;

    public ChatFrame(String username) {
        super("Chat - " + username);
        this.username = username;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());


        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        add(chatScrollPane, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        buttonPanel.add(sendButton);


        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);


        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);


        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, getHeight()));
        add(userScrollPane, BorderLayout.EAST);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitChat();
            }
        });


        setVisible(true);
    }
    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }


    public String getMessage() {
        return messageField.getText();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = getMessage();
            if (!message.isEmpty()) {
                // Send the message to the server
                sendMessageToServer(message);
                messageField.setText(""); // Clear the message field
            }
        } else if (e.getSource() == exitButton) {
            // Handle exit action
            exitChat();
        }
    }


    private void sendMessageToServer(String message) {
        try {
            outputStream.writeObject(new MessageFromClient(username, message));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void exitChat() {
        try {
            outputStream.writeObject(new MessageFromClient(username, "exit"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        dispose(); // Close the window
    }


    public void setObjectOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public String getUsername() {
        return username;
    }


    public void addUser(String user) {
        userListModel.addElement(user);
    }


    public void removeUser(String user) {
        userListModel.removeElement(user);
    }


    public void updateUserList(List<String> users) {
        userListModel.clear();
        for (String user : users) {
            userListModel.addElement(user);
        }
    }



}
