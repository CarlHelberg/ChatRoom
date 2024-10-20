import java.io.*;
import java.net.*;

class ChatHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int clientId;
    private String color; // The assigned color for this client

    public ChatHandler(Socket socket, int clientId, String color) {
        this.socket = socket;
        this.clientId = clientId;
        this.color = color;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Notify the client about their assigned color
            TextColors.colorText(color, "Welcome! You are User#" + clientId);

            String message;
            // Keep listening for messages from this client
            while ((message = in.readLine()) != null) {
                System.out.println("User#" + clientId + ": " + message);
                // Broadcast this message to all other clients
                ChatServer.broadcastMessage(color, "User#" + clientId + ": " + message, this);
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

    // Send a message to the client
    public void sendMessage(String color, String message) {
        TextColors.colorText(color, message); // Send formatted message to the client
    }
}
