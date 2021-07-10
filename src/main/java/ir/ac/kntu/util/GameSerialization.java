package ir.ac.kntu.util;

import ir.ac.kntu.items.Player;

import java.io.*;

public class GameSerialization implements Serializable {
    String filename = "src/main/resources/data.ser";

    public void saveUser(Player player) {
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(player);
            out.close();
            file.close();
            System.out.println("Object has been serialized");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }

    }

    public Player getUser() {
        Player player = null;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            player = (Player) in.readObject();
            in.close();
            file.close();
            System.out.println("Object has been deserialized ");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        return player;
    }
}
