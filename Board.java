import java.io.Serializable;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L; // Add serialVersionUID for versioning
    private char[][] board = new char[6][7]; // Make this non-static

    public Board() {
        prepareBoard();
    }

    // reset the board
    public void prepareBoard() {
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
    @Override 
    public String toString() {
        StringBuilder outputString = new StringBuilder("1 2 3 4 5 6 7\n");
        for (char[] row : board) {
            for (char cell : row) {
                outputString.append(cell).append(" ");
            }
            outputString.append("\n");
        }
        return outputString.toString();
    }

    // Set a space on the board to the corresponding player's symbol
    public void setBoardSpace(int player, int move) throws InvalidColumnException {
        if(move < 1 || move > 7) {
            throw new InvalidColumnException("Your move must be between 1 and 7");
        } else if (columnIsFull(move)) {
            throw new InvalidColumnException("Your move must be in an empty space.");
        }
        for (int r = board.length - 1; r >= 0; r--) {
            if (board[r][move - 1] == '-') {
                board[r][move - 1] = (player == 1) ? 'X' : 'O';
                return;
            }
        }
    }

    // checks if the column is full
    public boolean columnIsFull(int move) {
        return board[0][move - 1] != '-';
    }

    // checks to see if there is a horizontal win
    public void horizontalWinner() throws GameEndException {
        // for every row in board
        for (int r = 0; r < board.length; r++) {
            // for four columns
            for (int c = 0; c < 4; c++) {
                if(board[r][c] != '-'){ // if board at r and c is not empty
                    // if the next four spaces are the same
                    if (board[r][c] == board[r][c + 1] && board[r][c + 1] == board[r][c + 2] && board[r][c + 2] == board[r][c + 3]) {
                        // return a horizontal win
                        throw new GameEndException("The game was won!", true);
                    }
                }
            }
        }
    }

    // checks to see if there is a vertical win
    public void verticalWinner() throws GameEndException {
        // for every column in board
        for(int c = 0; c < board.length + 1; c++){
            // for three rows
            for(int r = 0; r < 3; r++){
                if(board[r][c] != '-'){ // if board at r and c is not empty
                    // if the next four vertical spaces are the same
                    if(board[r][c] == board[r+1][c] && board[r+1][c] == board[r+2][c] && board[r+2][c] == board[r+3][c]){
                        // return a vertical win
                        throw new GameEndException("The game was won!", true);
                    }
                }
            }
        }
    }

    // checks if there is a diagonal winner
    public void diagonalWinner() throws GameEndException {

        // goes through every row backward
        for(int r = 5; r > 0; r--){
            // goes through the columns backward until (c-3) is less than or equal to 0
            for(int c = 6; (c-3) >= 0; c--){
                // if the space at [r][c] is not empty
                if(board[r][c] != '-'){
                    // if the four diagonal spaces starting at [r][c] are the same
                    if(r-1 > 0 && c-1 > 0 && r-2 > 0 && c-2 > 0 && r-3 > 0 && c-3 > 0 && r-1 < 6 && r-2 < 6 && r-3 < 6 && c-1 < 7 && c-2 < 7 && c-3 < 7){
                        if(board[r][c] == board[r-1][c-1] && board[r-1][c-1] == board[r-2][c-2] && board[r-2][c-2] == board[r-3][c-3]){
                            // return a diagonal winner
                            throw new GameEndException("The game was won!", true);
                        }
                    }
                    
                }
            }
            // goes through the columns until (c+3) is greater than 6
            for(int c = 0; (c+3) <= 6; c++){
                if(board[r][c] != '-'){ // if board at r and c is not empty
                    // if the next four diagonal spaces are the same
                    if(r-1 > 0 && c-1 > 0 && r-2 > 0 && c-2 > 0 && r-3 > 0 && c-3 > 0 && r-1 < 6 && r-2 < 6 && r-3 < 6 && c-1 < 7 && c-2 < 7 && c-3 < 7){
                        if(board[r][c] == board[r-1][c+1] && board[r-1][c+1] == board[r-2][c+2] && board[r-2][c+2] == board[r-3][c+3]){
                            // return a diagonal win
                            throw new GameEndException("The game was won!", true);
                        }
                    }
                    
                }
            }
        }
    }

    // checks to see if the board is full
    public boolean boardIsFull() throws GameEndException {
        boolean[][] spaceIsEmpty = new boolean[6][7];
        boolean boardIsFull = true;

        // for every row in board
        for(int r = 0; r < board.length; r++){
            // for every column in board
            for(int c = 0; c < board[r].length; c++){
                if(board[r][c] == '-'){ // if there is an empty space
                    // return that the board is not full
                    spaceIsEmpty[r][c] = false;
                } else {
                    spaceIsEmpty[r][c] = true;

                }
            }
        }

        for(int r = 0; r < spaceIsEmpty.length; r++){
            // for every column in board
            for(int c = 0; c < spaceIsEmpty[r].length; c++){
                if(spaceIsEmpty[r][c] == false) { // if there is an empty space
                    boardIsFull = false;
                    break;
                }
            }
        }

        if(boardIsFull == true){
            throw new GameEndException("There are no more places to move!", false);
        }
        // return that the board is empty
        return true;
    }

}
