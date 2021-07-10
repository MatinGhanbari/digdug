package ir.ac.kntu.DAO;

import ir.ac.kntu.items.Player;

import java.io.*;
import java.util.ArrayList;

public class GameSerialization implements Serializable, PlayerDAO {
    String filename = "src/main/resources/data.txt";

    @Override
    public void savePlayer(Player pl) {
        PlayerInfo player = new PlayerInfo(pl.getPlayerName(), pl.getScore());
        ArrayList<PlayerInfo> players = (ArrayList<PlayerInfo>) getAllPlayers().clone();
        if (players == null || players.size() <= 0) {
            players = new ArrayList<>();
        }
        players.removeIf(p -> p.getPlayerName().equals(player.getPlayerName()));
        players.add(player);
        try (FileOutputStream file = new FileOutputStream(filename, false);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            for (PlayerInfo p : players) {
                out.writeObject(p);
            }
            out.close();
            file.close();
            System.out.println("Object has been serialized");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        }

    }

    @Override
    public ArrayList<PlayerInfo> getAllPlayers() {
        ArrayList<PlayerInfo> players = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(file)) {
            while (true) {
                try {
                    players.add((PlayerInfo) in.readObject());
                } catch (Exception e) {
                    break;
                }
            }
            in.close();
            file.close();
            System.out.println("Object has been deserialized ");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
        return players;
    }
}
