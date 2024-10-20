import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ChatHandler> clientHandlers = new ArrayList<>();
    private static final String[] colors = {"red", "green", "yellow", "blue", "purple", "cyan", "white"};
    private static Map<String, Integer> colorUsage = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Chat server started...");

            // Initialize color usage map
            for (String color : colors) {
                colorUsage.put(color, 0);
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected."); // Debugging output

                String assignedColor = assignColor(); // Assign least-used color
                int clientId = clientHandlers.size() + 1; // Assign unique client ID
                ChatHandler handler = new ChatHandler(clientSocket, assignedColor, clientId);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String assignColor() {
        int minUsage = Collections.min(colorUsage.values()); // Find the minimum usage count
        List<String> leastUsedColors = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : colorUsage.entrySet()) {
            if (entry.getValue() == minUsage) {
                leastUsedColors.add(entry.getKey());
            }
        }

        // Select a random color from the least-used ones if multiple are available
        String selectedColor = leastUsedColors.get(new Random().nextInt(leastUsedColors.size()));
        colorUsage.put(selectedColor, colorUsage.get(selectedColor) + 1);
        return selectedColor;
    }

    public static void broadcastMessage(String color, String message, ChatHandler sender) {
        for (ChatHandler handler : clientHandlers) {
            if (handler != sender) {
                handler.sendMessage(color, message); // Send color and message separately
            }
        }
    }

    public static void removeClient(ChatHandler handler) {
        clientHandlers.remove(handler);
        System.out.println("Client disconnected."); // Debugging output
    }
}
