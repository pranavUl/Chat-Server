import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Connect4Frame extends JFrame implements KeyListener, WindowListener, MouseListener {
    private String text = "";
    private char user;
    private int circleSize = 40; // Adjusted circle size
    private ChatData chatData = null;
    private long startTime = -1;
    private boolean sentResetRequest = false;
    private boolean sendingConfirmReset = false;
    ObjectOutputStream os;
    private char turn;

    public Connect4Frame(ChatData gameData, ObjectOutputStream os, char user) {
        super("Connect 4 Game");
        this.chatData = gameData;
        this.os = os;
        this.user = user;
        addKeyListener(this);
        addMouseListener(this);
        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 800); // Adjusted frame size
        setResizable(false);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.white);
        int actualR = 0;
        for (int r = gameData.getGrid().length - 1; r >= 0; r--) {
            for (int c = 0; c < gameData.getGrid()[0].length; c++) {
                if (gameData.getGrid()[r][c] == 'X') {
                    g.setColor(Color.RED);
                } else if (gameData.getGrid()[r][c] == 'O') {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.white);
                }
                g.fillOval(c * 50 + 30, actualR * 50 + 120, circleSize, circleSize); // Adjusted circle positions
            }
            actualR++;
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.drawString(text, 30, 90);
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public void setTurn(char t) {
        turn = t;
        if (t == user)
            text = "Your turn";
        else {
            if (t == 'e') {
            } else {
                if (t == 'X') {
                    text = "Red's turn";
                } else {
                    text = "Black's turn";
                }
            }
        }
        repaint();
    }

    public void makeMove(int c, int r, char letter) {
        gameData.getGrid()[r][c] = letter;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        char key = event.getKeyChar();
        int r;

        if (user != turn) {
            return;
        }
        switch (key) {
            case '1':
                r = 1;
                break;
            case '2':
                r = 2;
                break;
            case '3':
                r = 3;
                break;
            case '4':
                r = 4;
                break;
            case '5':
                r = 5;
                break;
            case '6':
                r = 6;
                break;
            case '7':
                r = 7;
                break;
            default:
                r = -1;
        }
        if (r != -1) {
            int row = gameData.addToLowest(r, user);
            int col = r - 1;
            if (row != -1) {
                try {
                    os.writeObject(new CommandFromClient(CommandFromClient.MOVE, "" + col + row + user));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            os.writeObject(new CommandFromClient(CommandFromClient.CLOSING, "" + user));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public void closing() throws InterruptedException {
        startTime = System.currentTimeMillis();
        long prev = -1;
        while (true) {
            long timepassed = System.currentTimeMillis() - startTime;
            long secondspassed = timepassed / 1000;
            if (secondspassed > prev) {
                text = "Other Client Disconnected. Closing in: " + (5 - secondspassed);
                prev = secondspassed;
                repaint();
            }
            if (secondspassed >= 6) {
                break;
            }
        }
        System.exit(0);
    }

    public void resetGrid() {
        gameData.reset();
        startTime = -1;
        sentResetRequest = false;
        sendingConfirmReset = false;
    }

    public void initializeConfirm(String m, String title) {
        sendingConfirmReset = true;
        if (user == turn) {
            text = "Waiting for the other user to accept the request...";
        } else {
            text = "Right click to accept the reset request.";
        }
        repaint();
    }

    public boolean getSentResetRequest() {
        return sentResetRequest;
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            if (gameData.isWinner('X') || gameData.isWinner('O') || gameData.isCat()) {
                if (sendingConfirmReset) {
                    try {
                        os.writeObject(new CommandFromClient(CommandFromClient.CONFIRM, ""));
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    return;
                }
                sentResetRequest = true;
                try {
                    os.writeObject(new CommandFromClient(CommandFromClient.RESTART, ""));
                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
            return;
        }

        if (user != turn) // If it's not the user's turn, do nothing
            return;

        if (gameData.isWinner('X') || gameData.isWinner('O') || gameData.isCat()) {
            // If the game is done, return without making any move
            return;
        }

        // Left mouse button clicked, implement existing functionality
        // Get the x-coordinate of the mouse click
        int x = e.getX();

        // Calculate the column based on the x-coordinate
        int column = (x - 30) / 50;

        // Make a move in the corresponding column
        if (column >= 0 && column < 7) {
            int row = gameData.addToLowest(column + 1, user);
            if (row != -1) {
                try {
                    os.writeObject(new CommandFromClient(CommandFromClient.MOVE, "" + column + row + user));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void resetGame() {
        if (user == turn) {
            if (gameData.isWinner('X') || gameData.isWinner('O') || gameData.isCat()) {
                if (sendingConfirmReset) {
                    try {
                        os.writeObject(new CommandFromClient(CommandFromClient.CONFIRM, ""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                sentResetRequest = true;
                try {
                    os.writeObject(new CommandFromClient(CommandFromClient.RESTART, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
