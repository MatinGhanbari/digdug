package ir.ac.kntu.dao;

import ir.ac.kntu.items.Player;

import java.io.*;
import java.util.ArrayList;

public class GameSerialization implements Serializable, PlayerDAO {
    String filename = "src/main/resources/database/GameData.ddd";

    @Override
    public void savePlayer(Player pl) {
        PlayerInfo player = new PlayerInfo(pl.getPlayerName(), pl.getScore());
        ArrayList<PlayerInfo> players = (ArrayList<PlayerInfo>) getAllPlayers().clone();
        if (players == null || players.size() <= 0) {
            players = new ArrayList<>();
        }
        ArrayList<PlayerInfo> finalPlayers = new ArrayList<>();
        for (PlayerInfo p : players) {
            finalPlayers.add(findPlayer(p, player));
        }
        try (FileOutputStream file = new FileOutputStream(filename, false);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            for (PlayerInfo p : finalPlayers) {
                out.writeObject(p);
            }
            out.close();
            file.close();
        } catch (IOException ex) {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return players;
    }

    public PlayerInfo findPlayer(PlayerInfo p1, PlayerInfo p2) {
        if (p1.getPlayerName().equalsIgnoreCase(p2.getPlayerName())) {
            return new PlayerInfo(p1.getPlayerName(), Math.max(p1.getScore(), p2.getScore()));
        }
        return p1;
    }
}
