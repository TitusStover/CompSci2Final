import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class gameClient {

    public static void main(String[] args) {
        System.out.println("what is the hosts ip address or machine name");
        host = kb.nextLine();

        System.out.println("what is the hosts port");
        port = kb.nextInt();
        kb.nextLine();

        try {
            // Connect to the server
            Socket socket = new Socket(host, playerPort);
            System.out.println("Connected to the server as Player " + player + "!");

            // Set up input and output streams
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputStream = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);

            // Read messages from the server and send responses
            String serverMessage;
            while ((serverMessage = inputStream.readLine()) != null) {
                System.out.println(serverMessage);

                // If the server expects input, send it
                if (serverMessage.contains("Enter your move") || serverMessage.contains("Would you like to play again?")) {
                    String userInput = scanner.nextLine();
                    outputStream.println(userInput);
                }
            }

            // Close resources
            inputStream.close();
            outputStream.close();
            socket.close();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
