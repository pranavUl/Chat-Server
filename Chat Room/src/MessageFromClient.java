import java.io.Serializable;

public class MessageFromClient implements Serializable {

    public String sender;
    public String message;

    public MessageFromClient(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }


    public String getSender() {
        return sender;
    }


    public String getMessage() {
        return message;
    }
}