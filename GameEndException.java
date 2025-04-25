public class GameEndException extends Exception{

    private boolean gameWon;

    public GameEndException(String message, boolean gameIsWon){
        super(message);
        gameWon = gameIsWon;
    }

    public boolean gameWon() {
        return gameWon;
    }
}
