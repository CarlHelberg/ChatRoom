import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ChatHandler> clientHandlers = new ArrayList<>();
    private static final String[] colors = {"red", "green", "yellow", "blue", "purple", "cyan", "white"}; // Color names
    private static Map<String, Integer> colorUsage = new HashMap<>(); // To track how often each color is used

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Chat server started...");

            // Initialize color usage map
            for (String color : colors) {
                colorUsage.put(color, 0); // Set initial usage count for each color to 0
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientHandlers.size() + 1; // Assign unique client ID
                String assignedColor = assignColor(); // Assign least-used color
                ChatHandler handler = new ChatHandler(clientSocket, clientId, assignedColor);
                clientHandlers.add(handler);
                new Thread(handler).start();
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
                handler.sendMessage(color, message);
            }
        }
    }

    // Remove a client handler when they disconnect
    public static void removeClient(ChatHandler handler) {
        clientHandlers.remove(handler);
    }
}
