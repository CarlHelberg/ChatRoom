import java.io.*;
import java.net.*;
import java.util.Scanner;

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
            System.out.println(welcomeMessage); // Display welcome message with assigned color

            // Extract the user's color from the welcome message (optional parsing if needed)
            this.color = extractColorFromMessage(welcomeMessage);

            System.out.print("Enter your username: ");
            username = scanner.nextLine();

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
            System.out.print("You: ");
            String message = scanner.nextLine(); // Read user input

            if (message.equalsIgnoreCase("/quit")) {
                break; // Exit the loop if the user types /quit
            }

            // Send message to the server with the user's color
            sendMessage(message);
        }

        // Close resources when exiting
        closeConnection();
    }

    // Send a message to the server
    private void sendMessage(String message) {
        out.println(message); // Send raw message to the server (color is handled on server-side)
    }

    // Extract the user's color from the welcome message (for customization)
    private String extractColorFromMessage(String message) {
        // Example parsing, assumes format "Welcome! You are User#ID [color]"
        String[] parts = message.split("\\[|\\]"); // Splits on brackets if needed
        return parts.length > 1 ? parts[1] : "white"; // Default to white if no color found
    }

    // Close the socket and resources
    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    // Handle incoming messages from the server
    private class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String serverMessage;
                // Continuously listen for messages from the server
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage); // Display message with color formatting
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
