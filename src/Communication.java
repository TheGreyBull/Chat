import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Communication {

    private ArrayList<String> messages;
    private ArrayList<String> time;
    protected final String username;
    private final Socket connection;
    private final DataOutputStream out;
    private final BufferedReader in;

    protected Communication(String username, Socket connection, DataOutputStream out, BufferedReader in) {
        this.username = username;
        this.connection = connection;
        this.out = out;
        this.in = in;
        messages = new ArrayList<>();
        time = new ArrayList<>();
    }

    protected void closeConnection() {
        try {
            in.close();
            out.close();
            connection.close();
        } catch (IOException ignore) {}
    }

    protected void appendMessage(String message, String time) {
        messages.add(message);
        this.time.add(time);
    }

    protected String[] getMessages() {
        String[] messagesArray = new String[messages.size()];
        for (int i = 0; i < messagesArray.length; i++) {
            messagesArray[i] = messages.get(i);
        }
        return messagesArray;
    }

    protected String[] getTimeMessages() {
        String[] timeArray = new String[messages.size()];
        for (int i = 0; i < timeArray.length; i++) {
            timeArray[i] = time.get(i);
        }
        return timeArray;
    }

    protected String getUsername() {
        return username;
    }

    protected DataOutputStream getOut() {
        return out;
    }

    protected BufferedReader getIn() {
        return in;
    }
}
