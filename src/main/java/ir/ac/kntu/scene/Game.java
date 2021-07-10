package ir.ac.kntu.scene;

import ir.ac.kntu.Constants.Constants;
import ir.ac.kntu.items.*;
import ir.ac.kntu.util.GameMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Application implements Serializable {
    private Player player;
    private ArrayList<Dirt> dirts;
    private ArrayList<Stone> stones;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Heart> hearts;
    private ArrayList<Wall> walls;
    private ArrayList<Balloon> balloons;
    private GameMap gameMap;
    private Scene scene;
    private GridPane pane;
    private boolean isDone;
    private Thread timer;
    private String playerName;

    public Game(GameMap gameMap) {
        this.gameMap = gameMap;
        scene = gameMap.getScene();
        pane = gameMap.getPane();
        player = gameMap.getPlayer();
        dirts = gameMap.getDirts();
    }

    @Override
    public void start(Stage stage) {
        stage.close();
        stage = new Stage();
        startTimer();
        initScene();
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("DigDug - Player : " + playerName);
        System.out.println("Game started!");
        System.out.println("Player Name : \"" + playerName + "\"");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private void startTimer() {
        timer = new Thread(() -> {
            try {
                Thread.sleep(Constants.GAME_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.isDone()) {
                Platform.runLater(() -> {
                    getPane().addRow(getPane().getRowCount() - 1, new Text("TimeUp"));
                });
                handleEndOfGame();
            }
        });
        timer.start();
    }

    public void initScene() {
        player.setGame(this);
        setLists();
        for (int i = 0; i < balloons.size(); i++) {
            balloons.get(i).setPlayer(player);
            balloons.get(i).setGame(this);
            int finalI = i;
//            new Thread(() ->balloons.get(finalI).handleMove()).start();
        }
        scene.setOnKeyReleased(keyEvent -> {
            KeyCode temp = keyEvent.getCode();
            if (player.isAlive() && player.getKeys().contains(temp)) {
                player.handleMove(temp);
                return;
            }
            if (temp == KeyCode.ESCAPE) {
                System.out.println("Exited");
                System.exit(0);
            }
        });
    }

    private void setLists() {
        this.dirts = gameMap.getDirts();
        this.walls = gameMap.getWalls();
        this.stones = gameMap.getStones();
        this.mushrooms = gameMap.getMushrooms();
        this.hearts = gameMap.getHearts();
        this.balloons = gameMap.getBalloons();
    }

    public void handleEndOfGame() {
        isDone = true;
        Platform.runLater(() -> {
            int i = -2;
            if (pane.getChildren().contains(player.getNode())) {
                pane.getChildren().remove(player.getNode());
            }
            pane.addColumn(i += 2, player.getNode());
            Text t = new Text("" + player.getScore());
            pane.addColumn(i + 1, t);
            GridPane.setHalignment(player.getNode(), HPos.CENTER);
            GridPane.setHalignment(t, HPos.CENTER);

        });
    }

    public boolean isDone() {
        return isDone;
    }

    public GridPane getPane() {
        return pane;
    }

    public GameMap getMap() {
        return gameMap;
    }
}
