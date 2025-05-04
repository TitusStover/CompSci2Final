import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveGame {

    public void saveGame(Board board, int player) {
        GameState state = new GameState(player, board);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameState.obj"))) {
            System.out.println("Saving game...");
            System.out.println("Player: " + player);
            System.out.println("Board:\n" + board);
            oos.writeObject(state); // Save the GameState object
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameState loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.obj"))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null; // Return null if loading fails
        }
    }

    /*
    public Board retrieveGameBoard(){

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.obj"))) {
            GameState readState = (GameState) ois.readObject();
            System.out.println("Game loaded successfully!");
            System.out.println("Player: " + readState.getPlayer());
            System.out.println("Board:\n" + readState.getBoard());
            return readState.getBoard();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Board(); // Return a new Board if loading fails
        }

    }

    public int retrieveGamePlayer(){

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.obj"))) {
            GameState readState = (GameState) ois.readObject();
            System.out.println("Game loaded successfully!");
            System.out.println("Player: " + readState.getPlayer());
            return readState.getPlayer();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return 1; // Default player if loading fails
        }
    }
    */
    
}
