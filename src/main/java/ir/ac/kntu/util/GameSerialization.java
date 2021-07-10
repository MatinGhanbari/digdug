package ir.ac.kntu.util;

import ir.ac.kntu.items.Player;

import java.io.*;
import java.util.ArrayList;

public class GameSerialization implements Serializable {
    String filename = "src/main/resources/data.ser";

    public void saveUser(Player player) {
        ArrayList<Player> players = getUsers();
        if (players == null) {
            players = new ArrayList<>();
        }
        for (Player p : players) {
            if (p.getPlayerName().equals(player.getPlayerName())) {
                players.remove(p);
                players.add(player);
            }
        }
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            for (Player p : players) {
                out.writeObject(p);
            }
            out.close();
            file.close();
            System.out.println("Object has been serialized");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }

    }

    public ArrayList<Player> getUsers() {
        ArrayList<Player> players = null;
        Player player = null;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            player = (Player) in.readObject();
            while (player != null) {
                players.add(player);
                player = (Player) in.readObject();
            }
            in.close();
            file.close();
            System.out.println("Object has been deserialized ");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        return players;
    }
}
