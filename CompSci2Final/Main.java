import java.util.Scanner;

public class Main {

    public static Scanner kb = new Scanner(System.in);
    public static Board board = new Board();
    public static int player;
    public static int gameOverFlag;

    public static void main(String[] args) {

        boolean playAgain = true;
        int winner;
        System.out.println("\nWelcome to Connect Four!");


        // While both want to play again, loop
        while (playAgain) {
            //reset the board for the game
            board.prepareBoard();
            // Introduce the game
            System.out.println("Player One will be X's, Player Two will be O's");
            // set player and gameOver
            player = 0;
            gameOverFlag = 0;
            // print the board
            board.printBoard();

            // while the game isn't over
            while (gameOverFlag == 0) {
                // if player on just moved
                if (player == 1) {
                    player = 2;
                } else {
                    player = 1;
                }
                // have the current player make a move
                makeMove();

                // print board
                board.printBoard();

                // check if the game is over
                checkGameOver();
            }

            if (gameOverFlag == 1) { // a player won the game
                System.out.println("Player " + player + " won the game!");
            } else if (gameOverFlag == 2) { // the game was tied
                System.out.println("No more possible moves. Game over!");
            }

            System.out.println();

            kb.nextLine();

            // ask if the player wants to play again
            System.out.println("Would you like to play again?");

            if (!kb.nextLine().equalsIgnoreCase("yes")) {
                // set playAgain to false
                playAgain = false;
            }

        }
    }

    public static void makeMove() {
        // Ask which move the player wants to make
        System.out.println("Which move (1-7) would you like to make?");
        int move = -1;

            while(!kb.hasNextInt()){
                // discard the next line and ask for a number
                kb.nextLine();
                System.out.println("Your move must be a number");
            }
            move = kb.nextInt();

            try {
                board.setBoardSpace(player, move);
            } catch (InvalidColumnException cofbe) {
                System.out.println(cofbe.getMessage());
                makeMove();
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