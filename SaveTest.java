import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class SaveTest  {

    public static void main(String[] args) {
        SaveGame saver = new SaveGame();

    //System.out.println(saver.retrieveGameBoard().toString());
    System.out.println(saver.retrieveGamePlayer());
    }
    

}

