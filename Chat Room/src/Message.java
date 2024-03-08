public class Message {

    private String text;
    private String username;

    public Message(String t, String u) {
        this.text = t;
        this.username = u;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username + ": " + text;
    }
}
