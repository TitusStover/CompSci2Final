import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveGame {

    public SaveGame(){

    }

    public void saveGame(Board board, int player){
        GameState state = new GameState(player, board);

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameState.obj"));
            oos.writeObject(state);
            oos.close();

            System.out.println(state);


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Board retrieveGameBoard(){

        try{
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.obj"));
                GameState readState = (GameState) ois.readObject();
            
                ois.close();
                return readState.getBoard();
    
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return new Board();
            } 

    }

    public int retrieveGamePlayer(){

        try{
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.obj"));
                GameState readState = (GameState) ois.readObject();
                ois.close();

                return readState.getPlayer();
    
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return 1;
            }
    }
    
}
