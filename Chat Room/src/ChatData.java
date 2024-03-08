import java.util.ArrayList;

public class ChatData
{
    private ArrayList<Message> chat = new ArrayList<Message>();

    public ArrayList<Message> getGrid()
    {
        return chat;
    }

    public ChatData() {
        reset();
    }

    public void reset()
    {
        chat.clear();
    }

    public void send(String text, String username) {
        chat.add(new Message(text, username));
    }

}
