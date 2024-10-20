import java.io.*;
import java.net.*;

public class ChatHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String color; // The assigned color for this client
    private int clientId; // The ID of the client

    public ChatHandler(Socket socket, String color, int clientId) {
        this.socket = socket;
        this.color = color;
        this.clientId = clientId; // Assign the client ID
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Notify the client of their assigned user number
            sendMessage(color, "Welcome! You are User#" + clientId);

            // Read username from the client
            username = in.readLine(); // The first message should be the username
            System.out.println("User connected: " + username); // Debugging output

            String message;
            // Keep listening for messages from this client
            while ((message = in.readLine()) != null) {
                // Format the message in the user's color
                String formattedMessage = TextColors.colorize(color, username + ": " + message);
                System.out.println(formattedMessage); // Print to the server console in color

                // Broadcast this message to all other clients in the user's color
                ChatServer.broadcastMessage(color, formattedMessage, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                ChatServer.removeClient(this); // Remove the client when they disconnect
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ChatHandler sendMessage method
    public void sendMessage(String color, String message) {
        out.println(color + ":" + message); // Use colon as delimiter between color and message
    }

    // Getter for username (optional if needed)
    public String getUsername() {
        return username;
    }
}
