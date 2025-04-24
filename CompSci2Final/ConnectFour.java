import java.util.Scanner;

public class ConnectFour {

    public static char[][] board = new char[6][7];
    public static char[] possibleMoves = {'1', '2', '3', '4', '5', '6', '7'};

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        boolean playAgain = true;
        System.out.println();
        System.out.println("Welcome to Connect Four!");

        // while they want to play again, loop
        while(playAgain){
            // reset the board for the game
            prepareBoard();
            // Introduce the game
            System.out.println("Player One will be X's, Player Two will be O's");
            // set player
            int player = 0;
            int gameOverFlag = 0;
            // print the board
            printBoard();

            //while the game isn't over
            while(gameOverFlag == 0){
                // if player one just moved
                if(player == 1){
                    // set player to 2
                    player = 2;
                } else{ // if player two just moved
                    // set player to 1
                    player = 1;
                }
                // have the player make a move
                makeMove(player, kb);
                // print the board to the screen
                printBoard();
                // check if the game has been won
                gameOverFlag = checkGameOver();

            }

                if(gameOverFlag == 1){ // if the game was one
                    // print who won the game
                    System.out.println("Player " + player + " won the game!");
                    System.out.println();
                } else if(gameOverFlag == 2){ // if there are no moves left
                    // print that there are no moves left
                    System.out.println("No more possible moves. Game over");
                    System.out.println();
            }

            kb.nextLine();

            // ask if user wants to play again
            System.out.println("Would you like to play again? (yes/no)");

            if (kb.nextLine().toLowerCase().equals("yes")) { // if the answer is no
                // set playAgain to false (end the game)
                playAgain = true;
            } else{
                playAgain = false;
            }
        }
    }

    // reset the board
    public static void prepareBoard() {
        // for every row in the board
        for (int r = 0; r < board.length; r++) {
            // for every column in the board
            for (int c = 0; c < board[r].length; c++) {
                // set the space to '-'
                board[r][c] = '-';
            }
        }
    }

    // print the board to the screen
    public static void printBoard() {
        // for every row in board
        for (int i = 0; i <= board.length; i++) {
            // print the number corrisponding with a move
            System.out.print(possibleMoves[i]);
            // print a space
            System.out.print(" ");
        }

        // print an empty line
        System.out.println();
        // for every row in board
        for (int r = 0; r < board.length; r++) {
            // for every column in board
            for (int c = 0; c < board[r].length; c++) {
                // print the value of board at the row and column
                System.out.print(board[r][c]);
                // print a space
                System.out.print(" ");
            }
            // print an empty line
            System.out.println();
        }
    }

    // make a move
    public static void makeMove(int player, Scanner kb) {
        // ask which move the player wants to make
        System.out.println("Which move (1-7) would you like to make?");
        // set move to -1
        int move = -1;
        // set isValid to false
        boolean isValid = false;

        // while isValid is false
        while (isValid == false) {
            // while the Scanner does not have an int
            while (!kb.hasNextInt()) {
                // discard the next line and ask more a number
                kb.nextLine();
                System.out.println("Your move must be a number");
            }

            move = kb.nextInt();

            if (move < 1 || move > 7) { // if the move is not between 1 and 7
                // ask for a number between 1 and 7
                System.out.println("Your move must be between 1 and 7");
            } else if (columnIsFull(move)) { // if the column is full
                // ask for a move in an empty space
                System.out.println("Your move must be in an empty space");
            } else { // if the move is valid
                // set isValid to true
                isValid = true;
            }
        }

        int row = -1;
        // for every row in board
        for (int r = 0; r < board.length; r++) {
            if (!(board[r][move - 1] == 'X' || board[r][move - 1] == 'O')) { // if the space at r does not have a move
               // set row to r
                row = r;
            }
        }

        if (player == 1) {// if it's player one's turn
            // set the space to X
            board[row][move - 1] = 'X';
        } else { // if it's player two's turn
            // set the space to O
            board[row][move - 1] = 'O';
        }
    }

    // checks if the column is full
    public static boolean columnIsFull(int move) {

        if (board[0][move - 1] == 'X' || board[0][move - 1] == 'O') { //  if the last space in the move's column is taken
            return true;
        } else { // if the last space in the move's column isn't taken
            return false;
        }
    }

    // checks if and how the game is over
    public static int checkGameOver() {

        if (horizontalWinner() || verticalWinner() || diagonalWinner()) {// if the game has been won
            return 1;
        }else if(boardIsFull()){ //  if the board is completely full
            return 2;
        }
        // if the game hasn't been won
        return 0;
    }

    // checks to see if there is a horizontal win
    public static boolean horizontalWinner() {
        // for every row in board
        for (int r = 0; r < board.length; r++) {
            // for four columns
            for (int c = 0; c < 4; c++) {
                if(board[r][c] != '-'){ // if board at r and c is not empty
                    // if the next four spaces are the same
                    if (board[r][c] == board[r][c + 1] && board[r][c + 1] == board[r][c + 2] && board[r][c + 2] == board[r][c + 3]) {
                        // return a horizontal win
                        return true;
                    }
                }
            }
        }
        // return no horizontal win
        return false;
    }

    // checks to see if there is a vertical win
    public static boolean verticalWinner() {
        // for every column in board
        for(int c = 0; c < board.length + 1; c++){
            // for three rows
            for(int r = 0; r < 3; r++){
                if(board[r][c] != '-'){ // if board at r and c is not empty
                    // if the next four vertical spaces are the same
                    if(board[r][c] == board[r+1][c] && board[r+1][c] == board[r+2][c] && board[r+2][c] == board[r+3][c]){
                       // return a vertical win
                        return true;
                    }
                }
            }
        }
        // return no vertical win
        return false;
    }

    // checks if there is a diagonal winner
    public static boolean diagonalWinner() {

        // goes through every row backward
        for(int r = 5; r > 0; r--){
            // goes through the columns backward until (c-3) is less than or equal to 0
            for(int c = 6; (c-3) >= 0; c--){
                // if the space at [r][c] is not empty
                if(board[r][c] != '-'){
                    // if the four diagonal spaces starting at [r][c] are the same
                    if(board[r][c] == board[r-1][c-1] && board[r-1][c-1] == board[r-2][c-2] && board[r-2][c-2] == board[r-3][c-3]){
                       // return a diagonal winner
                        return true;
                    }
                }
            }
            // goes through the columns until (c+3) is greater than 6
            for(int c = 0; (c+3) <= 6; c++){
                if(board[r][c] != '-'){ // if board at r and c is not empty
                    // if the next four diagonal spaces are the same
                    if(board[r][c] == board[r-1][c+1] && board[r-1][c+1] == board[r-2][c+2] && board[r-2][c+2] == board[r-3][c+3]){
                        // return a diagonal win
                        return true;
                    }
                }
            }
        }
        // return no diagonal win
        return false;
    }

    // checks to see if the board is full
    public static boolean boardIsFull(){
        // for every row in board
        for(int r = 0; r < board.length; r++){
            // for every column in board
            for(int c = 0; c < board[r].length; c++){
                if(board[r][c] == '-'){ // if there is an empty space
                    // return that the board is not empty
                    return false;
                }
            }
        }
        // return that the board is empty
        return true;
    }
}