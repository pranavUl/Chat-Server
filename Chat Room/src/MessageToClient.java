

import java.io.Serializable;
import java.util.List;

public class MessageToClient implements Serializable {
    private String sender;
    private String message;
    private List<String> userList;




    public MessageToClient(String sender, String message, List<String> userList) {
        this.sender = sender;
        this.message = message;
        this.userList = userList;
    }




    public String getSender() {
        return sender;
    }




    public String getMessage() {
        return message;
    }




    public List<String> getUserList() {
        return userList;
    }




    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}



