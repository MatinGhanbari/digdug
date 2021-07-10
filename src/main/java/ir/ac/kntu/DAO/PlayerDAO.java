package ir.ac.kntu.dao;

import ir.ac.kntu.dao.PlayerInfo;
import ir.ac.kntu.items.Player;

import java.util.ArrayList;

public interface PlayerDAO {
    void savePlayer(Player player);

    ArrayList<PlayerInfo> getAllPlayers();
}
