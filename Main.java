import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class Main {

    public static Scanner kb = new Scanner(System.in);
    public static Board board = new Board();
    public static int player;
    public static int gameOverFlag;
    public static Socket player1Socket;
    public static Socket player2Socket;

    public static void main(String[] args) {
        ServerSocket mainServerSocket = null;
        ServerSocket serverSocket1 = null;
        ServerSocket serverSocket2 = null;
        String IPAddress = "10.12.2.95"; // Change this to the server's IP address if needed
        int mainPort = 8430; // Main port for initial connections
        int player1Port = 8431;
        int player2Port = 8432;
        BufferedReader inputStream1 = null;
        BufferedReader inputStream2 = null;
        PrintWriter outputStream1 = null;
        PrintWriter outputStream2 = null;

        try {
            mainServerSocket = new ServerSocket(mainPort); // Main port for initial connections
            serverSocket1 = new ServerSocket(player1Port); // Port for Player 1
            serverSocket2 = new ServerSocket(player2Port); // Port for Player 2

            System.out.println("Server is running on " + IPAddress + " and listening on port " + mainPort);

            while (true) {
                System.out.println("Waiting for a new player to connect...");
                Socket tempSocket = mainServerSocket.accept(); // Accept initial connection
                System.out.println("A new player connected!");

                PrintWriter tempOutput = new PrintWriter(tempSocket.getOutputStream(), true);

                if (player1Socket == null) {
                    // Assign to Player 1
                    tempOutput.println("REDIRECT " + player1Port);
                    tempSocket.close();
                    player1Socket = serverSocket1.accept(); // Wait for Player 1 to reconnect
                    outputStream1 = new PrintWriter(player1Socket.getOutputStream(), true);
                    outputStream1.println("You are player 1");
                } else if (player2Socket == null) {
                    // Assign to Player 2
                    tempOutput.println("REDIRECT " + player2Port);
                    tempSocket.close();
                    player2Socket = serverSocket2.accept(); // Wait for Player 2 to reconnect
                    outputStream2 = new PrintWriter(player2Socket.getOutputStream(), true);
                    outputStream2.println("You are player 2");
                } else {
                    // Server is full
                    tempOutput.println("SERVER_FULL");
                    tempSocket.close();
                    System.out.println("Server is full. Connection rejected.");
                }

                if (player1Socket != null && player2Socket != null) {
                    // Start the game logic when both players are connected
                    handleGame(player1Socket, player2Socket);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void handleGame(Socket player1Socket, Socket player2Socket) {
        BufferedReader inputStream1 = null;
        BufferedReader inputStream2 = null;
        PrintWriter outputStream1 = null;
        PrintWriter outputStream2 = null;

        try {
            inputStream1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            inputStream2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            outputStream1 = new PrintWriter(player1Socket.getOutputStream(), true);
            outputStream2 = new PrintWriter(player2Socket.getOutputStream(), true);

            // While both want to play again, loop
            boolean playAgain = true;
            while (playAgain) {

                //output welcome line on both clients
                outputStream1.println("Welcome to Connect Four!");
                outputStream2.println("Welcome to Connect Four!");

                //reset the board for the game
                board.prepareBoard();
                // Introduce the game
                outputStream1.println("Player One will be X's, Player Two will be O's");
                outputStream2.println("Player One will be X's, Player Two will be O's");

                // set player and gameOver
                player = 0;
                gameOverFlag = 0;

                // print the board
                outputStream1.println(board.toString());
                outputStream2.println(board.toString());

                // while the game isn't over
                while (gameOverFlag == 0) {
                    System.out.println("Starting game.");
                    // if player on just moved
                    if (player == 1) {
                        player = 2;
                    } else {
                        player = 1;
                    }
                    System.out.println("Chose player");
                    // have the current player make a move
                    makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);

                    System.out.println("Made move");
                    // print board
                    outputStream1.println(board.toString());
                    outputStream2.println(board.toString());

                    // check if the game is over
                    System.out.println("Checking game over");
                    checkGameOver();
                }

                if (gameOverFlag == 1) { // a player won the game
                    outputStream1.println("Player " + player + " won the game!");
                    outputStream2.println("Player " + player + " won the game!");
                } else if (gameOverFlag == 2) { // the game was tied
                    outputStream1.println("No more possible moves. Game over!");
                    outputStream2.println("No more possible moves. Game over!");
                }

                System.out.println("gameOverFlag: " + gameOverFlag);
                if(gameOverFlag != 0){
                    // ask if the player wants to play again
                    outputStream1.println("Would you like to play again?");
                    String response1 = inputStream1.readLine();

                    outputStream2.println("Would you like to play again?");
                    String response2 = inputStream2.readLine();

                    //checks to see if player one dosn't answer yes or no
                    while (!response1.equalsIgnoreCase("no") && !response1.equalsIgnoreCase("yes")){
                        outputStream1.println("Would you like to play again?");
                        response1 = inputStream1.readLine();
                    }

                    //checks to see if player two dosn't answer yes or no
                    while (!response2.equalsIgnoreCase("no") && !response2.equalsIgnoreCase("yes")){
                        outputStream2.println("Would you like to play again?");
                        response2 = inputStream2.readLine();
                    }
                    
                    //if both players answer no the code will finish if yes the game will start again
                    if (response1.equalsIgnoreCase("no") || response2.equalsIgnoreCase("no")) {
                        playAgain = false;
                    } 
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void makeMove(int player, PrintWriter outputStream1, PrintWriter outputStream2, BufferedReader inputStream1, BufferedReader inputStream2) {

        System.out.println("Entered make move.");
        if(player == 1){

            System.out.println("Player one's turn.");
            try {
                
                outputStream1.println("Where do you want to go in put an intiger between 1 and 7. \n If you wanted to save the game type (9).");
                int move = Integer.parseInt(inputStream1.readLine());

                if (move < 1 || move > 7) {
                    if( move != 9){
                        outputStream1.println("Your move must be a number between 1 and 7. \n If you want to save type (9.)");
                        move = Integer.parseInt(inputStream1.readLine());
                    }
                } else if( move == 9){
                    WritingBoardToBinary(outputStream1, outputStream2);
                    makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);
                } else {
                    try {
                        board.setBoardSpace(player, move);
                    } catch (InvalidColumnException cofbe) {
                        outputStream1.println(cofbe.getMessage());
                        makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);
                    } 
                }
            } catch (NumberFormatException | IOException e) {
                outputStream1.println("Invalid input. Please enter a number between 1 and 7.");
            }
            
        } else {
            try {
                outputStream2.println("Where do you want to go in put an intiger between 1 and 7. \n If you wanted to save the game type (9).");
                int move = Integer.parseInt(inputStream2.readLine());

                if (move < 1 || move > 7) {
                    if( move != 9){
                        outputStream2.println("Your move must be a number between 1 and 7. \n If you want to save type (9).");
                        move = Integer.parseInt(inputStream2.readLine());
                    }
                } else {
                    try {
                        board.setBoardSpace(player, move);
                        makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);
                    } catch (InvalidColumnException cofbe) {
                        outputStream2.println(cofbe.getMessage());
                        makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);
                    } 
                }

            } catch (NumberFormatException | IOException e) {
                outputStream2.println("Invalid input. Please enter a number between 1 and 7. \\n If you want to save type (9).");
            }
        }
    }

    public static void checkGameOver() {
        try{
            board.horizontalWinner();
            board.verticalWinner();
            board.diagonalWinner();
            board.boardIsFull();
        } catch (GameEndException gwe) {
            System.out.println(gwe.getMessage());
            if (gwe.gameWon()) {
                gameOverFlag = 1;
            } else {
                gameOverFlag = 2;
            }
        }
    }

    public static void WritingBoardToBinary(PrintWriter outputStream1, PrintWriter outputStream2){
        try{
            FileOutputStream fos = new FileOutputStream("saveFile.obj", false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(board.toString());
            fos = new FileOutputStream("saveFile.obj", true);
            
            oos.writeObject(player);
            fos.close();
            oos.close();
            outputStream1.println("The game has been saved 🥔");
            outputStream2.println("The game has been saved 🥔");


        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        }  catch (IOException  ioe) {
            System.out.println(ioe.getMessage());
        }

    }
}