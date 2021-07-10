package ir.ac.kntu.DAO;

import java.io.Serializable;
import java.util.Objects;

public class PlayerInfo implements Serializable {
    private String playerName;
    private int score;

    public PlayerInfo(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerInfo)) {
            return false;
        }
        PlayerInfo that = (PlayerInfo) o;
        return getScore() == that.getScore() && Objects.equals(getPlayerName(), that.getPlayerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerName(), getScore());
    }

    @Override
    public String toString() {
        return "Name=\"" + playerName + '\"' + ", MaxScore=" + score;
    }
}
