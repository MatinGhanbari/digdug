package ir.ac.kntu.DAO;

import ir.ac.kntu.DAO.PlayerInfo;
import ir.ac.kntu.items.Player;

import java.util.ArrayList;

public interface PlayerDAO {
    void savePlayer(Player player);

    ArrayList<PlayerInfo> getAllPlayers();
}
