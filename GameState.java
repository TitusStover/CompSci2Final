import java.io.Serializable;

public class GameState implements Serializable{
    private int player;
    private Board board;

    public GameState(int player, Board board){
        this.player = player;
        this.board = board;
    }

    public int getPlayer() { return player; }
    public Board getBoard() { return board; }
}