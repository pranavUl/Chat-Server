
import java.io.Serializable;
import java.util.List;

public class MessageFromClient implements Serializable {
    private String sender;
    private String message;




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



