import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerListener implements Runnable
{
    private ServerSocket serverSocket;


    public ServerListener(ServerSocket s) {
        this.serverSocket = s;
    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                CommandFromClient cfc = (CommandFromClient) is.readObject();
                System.out.println(cfc);
                // handle the received command
                if(cfc.getCommand()==CommandFromClient.CLOSING) {
                    System.out.println("Time to close");
                    sendCommand(new CommandFromServer(CommandFromServer.CLOSING, null));

                }
                if(cfc.getCommand()==CommandFromClient.CONFIRM) {
                    System.out.println("Confirming reset from reset");
                    sendCommand(new CommandFromServer(CommandFromServer.CONFIRMRESET, null));
                }
                if(cfc.getCommand()==CommandFromClient.RESTART) {
                    //System.out.println("In process of reset");
                    gameData.reset();

                    //reset the gamedatas of other clients

                    //sendCommand(new CommandFromServer(CommandFromServer.X_TURN,""));
                    resetTurn();
                    System.out.println("reset board: " + Arrays.deepToString(gameData.getGrid()));
                }


                if(cfc.getCommand()==CommandFromClient.MOVE &&
                        turn==user && !gameData.isWinner('X')
                        && !gameData.isWinner('O')
                        && !gameData.isCat())
                {
                    // pulls data for the move from the data field
                    String data=cfc.getData();
                    int c = data.charAt(0) - '0';
                    int r = data.charAt(1) - '0';
                    System.out.println("gathered data: " + data);

                    // if the move is invalid it, do not process it
                    if(gameData.getGrid()[r][c]!=' ')
                        continue;

                    // changes the server side game board
                    gameData.getGrid()[r][c] = user;

                    // sends the move out to both users
                    sendCommand(new CommandFromServer(CommandFromServer.MOVE,data));

                    // changes the turn and checks to see if the game is over
                    changeTurn();
                    checkGameOver();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeTurn()
    {
        // changes the turn
        if(turn=='X')
            turn = 'O';
        else
            turn ='X';

        // informs both client of the new users turn
        if (turn == 'X')
            sendCommand(new CommandFromServer(CommandFromServer.X_TURN, null));
        else
            sendCommand(new CommandFromServer(CommandFromServer.O_TURN, null));
    }

    public void resetTurn() {
        turn='X';
        sendCommand(new CommandFromServer(CommandFromServer.RESET, null));
    }

    public void checkGameOver()
    {
        int command = -1;
        if(gameData.isCat())
            command = CommandFromServer.TIE;
        else if(gameData.isWinner('X'))
            command = CommandFromServer.X_WINS;
        else if(gameData.isWinner('O'))
            command = CommandFromServer.O_WINS;

        // if the game ended, informs both clients of the game's end state
        if(command!=-1)
            sendCommand(new CommandFromServer(command, null));
    }
    public void sendCommand(CommandFromServer cfs)
    {
        // Sends command to both users
        for (ObjectOutputStream o : outs) {
            try {
                o.writeObject(cfs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}