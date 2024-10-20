import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private String color;

    public ChatClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port); // Connect to server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // For receiving messages
            out = new PrintWriter(socket.getOutputStream(), true); // For sending messages

            Scanner scanner = new Scanner(System.in);

            // Initial connection message from the server, containing user color
            String welcomeMessage = in.readLine();
            System.out.println(welcomeMessage); // Display welcome message

            // Extract the user's color from the welcome message
            this.color = extractColorFromMessage(welcomeMessage);
            System.out.print("Enter your username: ");
            username = scanner.nextLine();

            // Send the username to the server
            out.println(username); // Send username first

            // Start a thread to listen for messages from the server
            new Thread(new IncomingMessageHandler()).start();

            // Start sending messages to the server
            sendMessageLoop(scanner);

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    // Main loop to read user input and send messages to the server
    private void sendMessageLoop(Scanner scanner) {
        while (true) {
            System.out.print("Message: "); // Placeholder for user input
            String message = scanner.nextLine(); // Read user input

            if (message.equalsIgnoreCase("/quit")) {
                closeConnection(); // Exit the loop if the user types /quit
            }

            // Send the message to the server
            sendMessage(message);
        }

        // Close resources when exiting
    }

    // Send a message to the server
    private void sendMessage(String message) {
        out.println(message); // Send the raw message; color is handled on the server
    }

    // Extract the user's color from the welcome message
    private String extractColorFromMessage(String message) {
        String[] parts = message.split("\\[|\\]"); // Splits on brackets if needed
        return parts.length > 1 ? parts[0] : "white"; // Default to white if no color found
    }

    // Close the socket and resources and exit the program
    private void closeConnection() {

        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        finally {
            System.exit(0); // Exit the program
        }
    }

    // IncomingMessageHandler in ChatClient class
    private class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String serverMessage;
                // Continuously listen for messages from the server
                while ((serverMessage = in.readLine()) != null) {
                    // Split the message by colon delimiter (color:message)
                    String[] parts = serverMessage.split(":", 2); // Split into color and message
                    if (parts.length == 2) {
                        String color = parts[0];   // First part is the color
                        String message = parts[1]; // Second part is the actual message
                        System.out.println(TextColors.colorize(color, message)); // Print colorized message
                    } else {
                        System.out.println(serverMessage); // Fallback if splitting fails
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading messages from server: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change this to the server's address
        int port = 12345; // Same port as the server

        new ChatClient(serverAddress, port); // Start the client
    }
}
