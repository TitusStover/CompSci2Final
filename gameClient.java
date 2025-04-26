import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class gameClient {

    public static void main(String[] args) {

        try {

            Scanner kb = new Scanner(System.in);
            String host = "";
            int port = 0;
            int player = 0;

            System.out.println("what is the hosts ip address or machine name");
            host = kb.nextLine();

            while(port != 8431 && port != 8432){
                System.out.println("what port do you want to port 8431 or port 8432");
                port = kb.nextInt();
            }


            if(port == 8431){
                player = 1; 
            }else if(port == 8432){
                player = 2;
            }

            // Connect to the server
            Socket socket = new Socket(host, port);
            System.out.println("Connected to the server as Player " + player + "!");

            // Set up input and output streams
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputStream = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);

            // Read messages from the server and send responses
            String serverMessage;
            while ((serverMessage = inputStream.readLine()) != null) {
                System.out.println(serverMessage);

                // If the server expects input, send it
                if (serverMessage.contains("Where do you want to go in put an intiger between 1 and 7.") || serverMessage.contains("Would you like to play again?")) {
                    String userInput = kb.nextLine();
                    outputStream.println(userInput);
                }
            }

            // Close resources
            inputStream.close();
            outputStream.close();
            socket.close();
            kb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
