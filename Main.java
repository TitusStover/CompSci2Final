import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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
        ServerSocket serverSocket1 = null;
        ServerSocket serverSocket2 = null;
        String IPAddress = "localhost"; // Change this to the server's IP address if needed
        int player1Port = 8431;
        int player2Port = 8432;
        BufferedReader inputStream1 = null;
        BufferedReader inputStream2 = null;

        try{
            serverSocket1 = new ServerSocket(player1Port); // Initialize for Player 1
            serverSocket2 = new ServerSocket(player2Port); // Initialize for Player 2

            while(true){
                System.out.println("Waiting for Player 1 to connect to " + IPAddress + " on port " + player1Port + "...");
                player1Socket = serverSocket1.accept(); // Accept Player 1 connection
                System.out.println("Player 1 connected!");
                inputStream1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));

                System.out.println("Waiting for Player 2 to connect to " + IPAddress + " on port " + player2Port + "...");
                player2Socket = serverSocket2.accept(); // Accept Player 2 connection
                System.out.println("Player 2 connected!");
                inputStream2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));

                PrintWriter outputStream1 = new PrintWriter(new DataOutputStream(player1Socket.getOutputStream()), true);
                PrintWriter outputStream2 = new PrintWriter(new DataOutputStream(player2Socket.getOutputStream()), true);
                
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
                        // if player on just moved
                        if (player == 1) {
                            player = 2;
                        } else {
                            player = 1;
                        }
                        // have the current player make a move
                        makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);

                        // print board
                        outputStream1.println(board.toString());
                        outputStream2.println(board.toString());

                        // check if the game is over
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
                        outputStream2.println("Would you like to play again?");

                        //reads both clients for their answer
                        String response1 = inputStream1.readLine();
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
            }

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void makeMove(int player, PrintWriter outputStream1, PrintWriter outputStream2, BufferedReader inputStream1, BufferedReader inputStream2) {

        if(player == 1){

            try {
                
                outputStream1.println("Where do you want to go in put an intiger between 1 and 7.");
                int move = Integer.parseInt(inputStream1.readLine());

                if (move < 1 || move > 7) {
                    outputStream1.println("Your move must be a number between 1 and 7.");
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
                outputStream2.println("Where do you want to go in put an intiger between 1 and 7.");
                int move = Integer.parseInt(inputStream2.readLine());
                
                if (move < 1 || move > 7) {
                    outputStream2.println("Your move must be a number between 1 and 7.");
                } else {
                    try {
                        board.setBoardSpace(player, move);
                    } catch (InvalidColumnException cofbe) {
                        outputStream2.println(cofbe.getMessage());
                        makeMove(player, outputStream1, outputStream2, inputStream1, inputStream2);
                    } 
                }

            } catch (NumberFormatException | IOException e) {
                outputStream1.println("Invalid input. Please enter a number between 1 and 7.");
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

}