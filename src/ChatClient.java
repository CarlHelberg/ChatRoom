import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
    private Socket socket; // Socket for connecting to the server
    private BufferedReader in; // Input stream for receiving messages from the server
    private PrintWriter out; // Output stream for sending messages to the server
    private String username; // The username of the client
    private String color; // The assigned color of the client

    public ChatClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port); // Connect to server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // For receiving messages
            out = new PrintWriter(socket.getOutputStream(), true); // For sending messages

            Scanner scanner = new Scanner(System.in);

            // Initial connection message from the server, containing user color
            String welcomeMessage = in.readLine();

            // Remove the color part from the welcome message
            String cleanedWelcomeMessage = removeColorFromMessage(welcomeMessage); // Call method to remove the color
            System.out.println(cleanedWelcomeMessage); // Display cleaned welcome message

            // Extract the user's color from the welcome message (if needed)
            this.color = extractColorFromMessage(welcomeMessage); // Optionally extract color information
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

    // Method to remove the color part (e.g., "cyan:") from the message
    private String removeColorFromMessage(String message) {
        // Split the message by colon, and return only the message part
        String[] parts = message.split(":", 2); // Split by the first colon
        return parts.length == 2 ? parts[1] : message; // Return the part after the colon, or the whole message if no colon is found
    }

    // Main loop to read user input and send messages to the server
    private void sendMessageLoop(Scanner scanner) {
        while (true) { // Infinite loop to read user input
            String message = scanner.nextLine(); // Read user input

            if (message.equalsIgnoreCase("/quit")) { // Check for the quit command
                closeConnection(); // Exit the loop if the user types /quit
            }

            // Send the message to the server
            sendMessage(message); // Send the user's message to the server
        }

        // Close resources when exiting
    }

    // Send a message to the server
    private void sendMessage(String message) {
        out.println(message); // Send the raw message; color is handled on the server
    }

    // Extract the user's color from the welcome message
    private String extractColorFromMessage(String message) {
        String[] parts = message.split("\\[|\\]"); // Split the message on brackets
        return parts.length > 1 ? parts[0] : "white"; // Return the extracted color or default to white if not found
    }

    // Close the socket and resources and exit the program
    private void closeConnection() {
        try {
            out.close(); // Close the output stream
            socket.close(); // Close the socket connection
        } catch (IOException e) { // Catch any IO exceptions during closing
            System.err.println("Error closing connection: " + e.getMessage()); // Print error message
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
                String serverMessage; // Variable to hold incoming messages from the server
                // Continuously listen for messages from the server
                while ((serverMessage = in.readLine()) != null) { // Read messages from the server
                    // Split the message by colon delimiter (color:message)
                    String[] parts = serverMessage.split(":", 2); // Split into color and message
                    if (parts.length == 2) { // Check if split was successful
                        String color = parts[0];   // First part is the color
                        String message = parts[1]; // Second part is the actual message
                        System.out.println(TextColors.colorize(color, message)); // Print colorized message
                    } else {
                        System.out.println(serverMessage); // Fallback if splitting fails
                    }
                }
            } catch (IOException e) { // Catch any IO exceptions that occur
                System.err.println("Error reading messages from server: " + e.getMessage()); // Print error message
            }
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change this to the server's address
        int port = 12345; // Same port as the server

        new ChatClient(serverAddress, port); // Start the client
    }
}
