# Simple Online Chat Application

## Overview

For CS1103, week7 from UoPeople.

This is a simple online chat application built using Java socket programming. 
It allows multiple clients to connect to a central server, send messages, 
and receive messages from other clients in real-time.

## Instructions:
### Assignment Instructions
You are tasked with developing a simple online chat application using Java. 
The application should allow multiple users to connect to a central server, 
send messages, and receive messages from other users.

### Requirements:

#### Server Implementation:
a. Create a server class, ChatServer, using socket programming to manage connections from multiple clients.
b. The server should be able to handle incoming connections, assign a unique user ID to each connected client, 
and maintain a list of connected users.
Client Implementation:
a. Implement a client class, ChatClient, that connects to the server using sockets.

b. Each client should be able to send messages to the server, which will broadcast the messages to all 
connected clients.

c. Clients should also be able to receive messages from other users.
User Interface:
a. Include a simple text-based for the client to facilitate message input and display.

### Guidelines
Submit well-commented Java source code.
Screenshot of the Text based User interface.
Include a README file explaining how to run your chat application and providing details about your implementation.


### Grading Criteria
Server Implementation: Implementation of socket programming concepts, user management, connection handling.
Client Implementation: Implementation of socket connection, messaging sending, Message broadcasting
User Interface: Creation of a user interface which is usable and provides screenshots of output of the interface.  
Logic and Computation
Program Flow and Structure
Code style and readability.


### Features:
- Multi-client support
- Unique user IDs assigned to each client
- Message broadcasting to all connected clients
- Text-based user interface

## Running the Application

### Requirements:
- Java 8 or higher

### Steps to Run:

1. **Start the Chat Server**:
    - Compile and run the `ChatServer` class:
      ```
      javac ChatServer.java
      java ChatServer
      ```

2. **Start the Chat Clients**:
    - Compile and run the `ChatClient` class for each client:
      ```
      javac ChatClient.java
      java ChatClient
      ```

3. **Send and Receive Messages**:
    - Type your message into the client terminal, and press enter. The message will be broadcast to all connected 
   clients.
    - Type `exit` to leave the chat.

## Screenshots

### Client Interface:
