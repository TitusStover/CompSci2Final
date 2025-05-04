import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.PlatformLoggingMXBean;
import java.net.Socket;
import java.net.ServerSocket;

public class GameLogic {
// player, currentMove and board are public variables

public static int player = 1;
public static int currentMove;
public static Board board = new Board();
public static Socket tempSocket;
public static Socket player1Socket;
public static Socket player2Socket;
public static ServerSocket mainServerSocket = null;
public static ServerSocket serverSocket1 = null;
public static ServerSocket serverSocket2 = null;
public static String IPAddress = "10.50.0.133"; // Change this to the server's IP address if needed
public static int mainPort = 8430;
public static int player1Port = 8431;
public static int player2Port = 8432;
public static BufferedReader inputStream1 = null;
public static BufferedReader inputStream2 = null;
public static PrintWriter tempOutput;
public static PrintWriter outputStream1;
public static PrintWriter outputStream2;
public static int gameOverFlag;
public static int turnCount;
public static SaveGame saver = new SaveGame();


// Main - implements all of these in an iteration
    public static void main(String[] args) {

        connect();

        outputStream1.print("\033[H\033[2J");
        outputStream2.print("\033[H\033[2J");
        outputStream1.println("Welcome to Connect Four! \nYou're Player 1. You will be X.");
        outputStream2.println("Welcome to Connect Four! \nYou're Player 2. You will be O.");

        gameOverFlag = 0;
        player = 0;
        
        continueGame();
    
        
        // run one instance of the game
        runGame();

        if(gameOverFlag==1){
            outputStream1.println("Player " + player + " won the game!");
            outputStream2.println("Player " + player + " won the game!");
        } else {
            outputStream1.println("No more possible moves. Game over!");
            outputStream2.println("No more possible moves. Game over!");
        }
        
        // if playAgain returns true, run loop
        while(playAgain()){
            board.prepareBoard();
            gameOverFlag = 0;
            player = 1;
            turnCount = 0;
            // run one instance of the game
            runGame();
            

            
            if(gameOverFlag==1){ // the game was won
                outputStream1.println("Player " + player + " won the game!");
                outputStream2.println("Player " + player + " won the game!");
            } else { // the entire board is full w/out a win
                outputStream1.println("No more possible moves. Game over!");
                outputStream2.println("No more possible moves. Game over!");
            }
        }
        
    }

    public static void connect(){
        int connected = 0;

        try{
            //setting up server sockets
            mainServerSocket = new ServerSocket(mainPort);
            serverSocket1 = new ServerSocket(player1Port);
            serverSocket2 = new ServerSocket(player2Port);

            //outputs the ip address
            System.out.println("this server is running on " + IPAddress + ".");

            while(connected < 2){
                System.out.println("waiting for a new player to connect...");
                tempSocket = mainServerSocket.accept();
                System.out.println("A new player has connected! :)");

                tempOutput = new PrintWriter(tempSocket.getOutputStream(), true);
                
                

                if(player1Socket == null){
                    //player 1 port is empty
                    tempOutput.println("REDIRECTING YOU TO: " + player1Port);
                    tempSocket.close();
                    player1Socket = serverSocket1.accept();
                    outputStream1 = new PrintWriter(player1Socket.getOutputStream(), true);
                    inputStream1 = new BufferedReader(new InputStreamReader((player1Socket.getInputStream())));
                    connected += 1;
                    outputStream1.println("You are player 1");
                } else if (player2Socket == null){
                    //player  2 port is empty
                    tempOutput.println("REDIRECTING YOU TO: " + player2Port);
                    tempSocket.close();
                    player2Socket = serverSocket2.accept();
                    outputStream2 = new PrintWriter(player2Socket.getOutputStream(), true);
                    inputStream2 = new BufferedReader(new InputStreamReader((player2Socket.getInputStream())));
                    connected += 1;
                    outputStream2.println("You are player 2");
                }else {
                    //server is full
                    tempOutput.println("THE SERVER IS FULL");
                    tempSocket.close();
                    System.out.println("server is full. Connection rejected.");
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    

    // makeMove method - checks if passed in move is valid, passes valid move to board
    public static void makeMove(int move){
        // check if currentMove is valid (note- board already checks if it's between 1-7 and if the column is full, 
        // you just need to catch the exceptions) 
        try{
            board.setBoardSpace(player, move);
        } catch (InvalidColumnException ice){
            if(player == 1){
                outputStream1.println(ice.getMessage());
                makeMove(getMove(false));
            }
            if(player == 2){
                outputStream2.println(ice.getMessage());
                makeMove(getMove(false));
            }
        }
    }
    // gameOver method - checks if the game is over
    public static void gameOver(){
        try{
            board.horizontalWinner();
            board.verticalWinner();
            board.diagonalWinner();
            board.boardIsFull();
        } catch (GameEndException gwe){
            System.out.println(gwe.getMessage());
            if(gwe.gameWon()){
                gameOverFlag = 1;
            } else {
                gameOverFlag = 2;
            }
        }
    }
    // getMove method - gets a move from one of the players
    public static int getMove(boolean isIteration){
        int move = 1;
        // ask the user where they would like to move
        if(player == 1){
            if(!isIteration){
                System.out.println("Player 1's turn.");
                outputStream1.println("It's Player 1's turn.");
                outputStream2.println("It's Player 1's turn.");
                outputStream1.println("Where do you want to move? (1-7)");
            }
        
            try{
                move = Integer.parseInt(inputStream1.readLine());
            } catch (NumberFormatException | IOException e){
                outputStream1.println("Where do you want to move? (1-7)");
                getMove(true);
            }
            
        }

        if(player == 2){
            if(!isIteration){
                System.out.println("Player 2's turn.");
                outputStream1.println("It's Player 2's turn.");
                outputStream2.println("It's Player 2's turn.");
                outputStream2.println("Where do you want to move? (1-7)");
            }
            try{
                move = Integer.parseInt(inputStream2.readLine());
            } catch (NumberFormatException | IOException e){
                outputStream2.println("Where do you want to move? (1-7)");
                getMove(true);
            }

        }
        // update currentMove
        return move;
    }
    // runGame method - runs one iteration of the game
    public static void runGame(){
        // call runTurn
        while(gameOverFlag == 0){
            runTurn();
            turnCount++;
            gameOver();
            
        }

        // print the board to both users
        outputStream1.println(board.toString());
        outputStream2.println(board.toString());

    }
    // runTurn method - runs one iteration of a turn
    public static void runTurn(){
        // clear both user's terminals
        outputStream1.print("\033[H\033[2J");
        outputStream2.print("\033[H\033[2J");

        if(turnCount == 5){
            turnCount = 0;
            saver.saveGame(board, player);
        }

        // decide which player's turn it is
        if(player == 1){
            player = 2;
        } else {
            player = 1;
        }
        // print the board to both users
        outputStream1.println(board.toString());
        outputStream2.println(board.toString());
        // call makeMove
        makeMove(getMove(false));
        

    }
    // playAgain method - asks users if they want to play again in order (player 1 then player 2) 
    public static boolean playAgain() {
        outputStream1.println("Do you want to play again? (yes/no)");
        outputStream2.println("Do you want to play again? (yes/no)");

        try {
            String player1Response = inputStream1.readLine();
            String player2Response = inputStream2.readLine();

            // Check if both responses are invalid
            if (!player1Response.equalsIgnoreCase("yes") && !player1Response.equalsIgnoreCase("no") &&
                !player2Response.equalsIgnoreCase("yes") && !player2Response.equalsIgnoreCase("no")) {

                outputStream1.print("\033[H\033[2J");
                outputStream2.print("\033[H\033[2J");

                outputStream1.println("Please provide a valid answer (yes/no).");
                outputStream2.println("Please provide a valid answer (yes/no).");
                return playAgain(); // Recursively call playAgain to prompt again
            }

            // Check if both players want to play again
            if (player1Response.equalsIgnoreCase("yes") && player2Response.equalsIgnoreCase("yes")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public static void continueGame() {

        outputStream1.println("Do you want to continue saved game? (yes/no)");
        outputStream2.println("Do you want to continue saved game? (yes/no)");
        try {
            String player1Response = inputStream1.readLine();
            String player2Response = inputStream2.readLine();

            if (!player1Response.equalsIgnoreCase("yes") && !player1Response.equalsIgnoreCase("no") &&
                !player2Response.equalsIgnoreCase("yes") && !player2Response.equalsIgnoreCase("no")) {

                outputStream1.print("\033[H\033[2J");
                outputStream2.print("\033[H\033[2J");

                outputStream1.println("Please provide a valid answer.");
                outputStream2.println("Please provide a valid answer.");
                continueGame(); // Recursively call playAgain to prompt again
            }

            if (player1Response.equalsIgnoreCase("yes") && player2Response.equalsIgnoreCase("yes")) {
                GameState state = saver.loadGame();
                if (state != null) {
                    board = state.getBoard();
                    player = state.getPlayer();
                    System.out.println("Loaded Player: " + player);
                    System.out.println("Loaded Board:\n" + board);
                } else {
                    System.out.println("No saved game found. Starting a new game.");
                    board = new Board();
                    board.prepareBoard();
                }
            } else {
                board = new Board();
                board.prepareBoard();
            }
        
        } catch (IOException ioe) {
            board = new Board();
            board.prepareBoard();
            System.out.println("IOE error: didn't retrieve board/player");
        }
    }
    
}
