import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class gameClient {

    public static void main(String[] args) {
        String serverIP = "10.12.2.95"; // Server IP
        int mainPort = 8430; // Main server port
        Scanner kb = new Scanner(System.in); // Initialize Scanner for user input

        try (Socket socket = new Socket(serverIP, mainPort); // Connect to the main server
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            // Read the server's message
            String serverMessage = input.readLine();
            if (serverMessage.startsWith("REDIRECT")) {
                int newPort = Integer.parseInt(serverMessage.split(" ")[1]);
                System.out.println("Redirecting to port: " + newPort);

                // Update a variable or reconnect to the new port
                try (Socket newSocket = new Socket(serverIP, newPort);
                     BufferedReader inputStream = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
                     PrintWriter outputStream = new PrintWriter(new DataOutputStream(newSocket.getOutputStream()), true)) {

                    // Read messages from the server and send responses
                    String newServerMessage;
                    while ((newServerMessage = inputStream.readLine()) != null) {
                        System.out.println(newServerMessage);

                        // If the server expects input, send it
                        if (newServerMessage.contains("Where do you want to go in put an intiger between 1 and 7.") ||
                            newServerMessage.contains("Would you like to play again?")) {
                            String userInput = kb.nextLine();
                            outputStream.println(userInput);
                        }
                    }
                }
            } else if (serverMessage.equals("SERVER_FULL")) {
                System.out.println("Server is full. Try again later.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kb.close(); // Close the Scanner
        }
    }
}
