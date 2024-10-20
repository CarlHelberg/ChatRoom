import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<ChatHandler> clientHandlers = new HashSet<>();
    private static int clientIdCounter = 0;

    public static void main(String[] args) {
        int port = 12345; // Port number for the server to listen on
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientIdCounter++;
                ChatHandler handler = new ChatHandler(clientSocket, clientIdCounter);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcasts a message to all connected clients
    public static void broadcastMessage(String message, ChatHandler sender) {
        for (ChatHandler handler : clientHandlers) {
            if (handler != sender) {
                handler.sendMessage(message);
            }
        }
    }

    // Removes a client from the handler set
    public static void removeClient(ChatHandler handler) {
        clientHandlers.remove(handler);
    }
}

// Handles individual client communication
class ChatHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int clientId;

    public ChatHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Welcome! You are User#" + clientId);
            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("User#" + clientId + ": " + message);
                ChatServer.broadcastMessage("User#" + clientId + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // Sends a message to the client
    public void sendMessage(String message) {
        out.println(message);
    }

    // Closes the connection and removes the client from the server's handler set
    private void closeConnection() {
        try {
            socket.close();
            ChatServer.removeClient(this);
            System.out.println("User#" + clientId + " has disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
