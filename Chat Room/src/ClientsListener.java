import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientsListener implements Runnable
{
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private Connect4Frame frame = null;

    public ClientsListener(ObjectInputStream is,
                           ObjectOutputStream os,
                           Connect4Frame frame) {
        this.is = is;
        this.os = os;
        this.frame = frame;

    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromServer cfs = (CommandFromServer)is.readObject();

                if(cfs.getCommand()==CommandFromServer.CLOSING) {
                    try {
                        frame.closing();
                    } catch (InterruptedException e) {
                        System.out.println("Exception exit");
                    }
                }
                if(cfs.getCommand()== CommandFromServer.CONFIRMRESET) {
                    System.out.println("Confirming repaint from clientlistener");
                    frame.resetGrid();
                    frame.setTurn('X');
                    frame.repaint();
                }
                // processes the received command
                if(cfs.getCommand()==CommandFromServer.RESET) {
                    //frame.resetGrid();
                    //frame.setTurn('X');
                    if(!frame.getSentResetRequest()) {
                        frame.initializeConfirm("Your opposition would like to rematch Press OK to accept a rematch, else exit out the pop up", "Rematch Request");

                    }
                    //display for only one of the frames
                    frame.repaint();
                    //System.out.println(frame.getGrid());
                }
                else if(cfs.getCommand() == CommandFromServer.X_TURN) {
                    frame.setTurn('X');
                    //frame.repaint();
                    //System.out.println(frame.getGrid());
                }
                else if(cfs.getCommand() == CommandFromServer.O_TURN)
                    frame.setTurn('O');
                else if(cfs.getCommand() == cfs.MOVE)
                {
                    String data = cfs.getData();
                    // pulls data for the move from the data field
                    int c = data.charAt(0) - '0';
                    int r = data.charAt(1) - '0';


                    // changes the board and redraw the screen
                    frame.makeMove(c,r,data.charAt(2));
                }
                // handles the various end game states
                else if(cfs.getCommand() == CommandFromServer.TIE)
                {
                    frame.setText("Tie game.");
                    frame.setTurn('e');
                }
                else if(cfs.getCommand() == CommandFromServer.X_WINS)
                {
                    frame.setText("Red wins!");
                    frame.setTurn('e');

                }
                else if(cfs.getCommand() == CommandFromServer.O_WINS)
                {
                    frame.setText("Black wins!");
                    frame.setTurn('e');

                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}