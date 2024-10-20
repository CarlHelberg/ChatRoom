import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ChatHandler> clientHandlers = new ArrayList<>(); // Stores all client handlers
    private static final String[] colors = {"red", "green", "yellow", "blue", "purple", "cyan"};
    private static Map<String, Integer> colorUsage = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println(TextColors.colorize("green","Chat server started..."));

            // Initialize color usage map
            for (String color : colors) {
                colorUsage.put(color, 0);
            }

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept a new client connection
                System.out.println(TextColors.colorize("green","New client connected.")); // Debugging output

                String assignedColor = assignColor(); // Assign least-used color
                int clientId = clientHandlers.size() + 1; // Assign unique client ID
                ChatHandler handler = new ChatHandler(clientSocket, assignedColor, clientId);
                clientHandlers.add(handler); // Add the handler to the list of active clients
                new Thread(handler).start(); // Start a new thread for the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Assign the least-used color, or random if all are equally used
    private static String assignColor() {
        int minUsage = Collections.min(colorUsage.values()); // Find the minimum usage count
        List<String> leastUsedColors = new ArrayList<>();

        // Collect all colors with the minimum usage count
        for (Map.Entry<String, Integer> entry : colorUsage.entrySet()) {
            if (entry.getValue() == minUsage) {
                leastUsedColors.add(entry.getKey());
            }
        }

        // Select a random color from the least-used ones if multiple are available
        String selectedColor = leastUsedColors.get(new Random().nextInt(leastUsedColors.size()));

        // Update usage count for the selected color
        colorUsage.put(selectedColor, colorUsage.get(selectedColor) + 1);

        return selectedColor;
    }

    // Broadcast message to all clients except the sender
    public static void broadcastMessage(String color, String message, ChatHandler sender) {
        for (ChatHandler handler : clientHandlers) {
            if (handler != sender) {
                handler.sendMessage(color, message); // Send color and message separately
            }
        }
    }

    // Remove a client handler when they disconnect
    public static void removeClient(ChatHandler handler) {
        clientHandlers.remove(handler);
        System.out.println(TextColors.colorize("green","Client disconnected.")); // Debugging output
    }

    // Get a list of usernames for all connected clients
    public static String getAllUsernames() {
        StringBuilder usernames = new StringBuilder();
        usernames.append("Connected users: ");
        for (ChatHandler handler : clientHandlers) {
            usernames.append(handler.getUsername()).append(", "); // Append each username
        }
        return usernames.toString().replaceAll(", $", ""); // Remove trailing comma and space
    }
}
