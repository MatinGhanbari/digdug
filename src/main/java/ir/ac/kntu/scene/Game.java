package ir.ac.kntu.scene;

import ir.ac.kntu.Constants.Constants;
import ir.ac.kntu.JavaFxApplication;
import ir.ac.kntu.items.*;
import ir.ac.kntu.util.GameMap;
import ir.ac.kntu.util.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    private Stage stage;

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
        this.stage = stage;
        startTimer();
        initScene();
        initBalloons();
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("DigDug - Player : " + playerName);
        System.out.println("Game started!");
        System.out.println("Player Name : \"" + playerName + "\"");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    public void initBalloons() {
        setLists();
        for (Balloon balloon : balloons) {
            balloon.setPlayer(player);
            balloon.setGame(this);
            Thread balloonThread = new Thread(balloon::handleMove);
            balloonThread.setPriority(Thread.NORM_PRIORITY);
            balloonThread.start();
        }
    }

    public void setLists() {
        player = gameMap.getPlayer();
        balloons = gameMap.getBalloons();
        dirts = gameMap.getDirts();
        walls = gameMap.getWalls();
        mushrooms = gameMap.getMushrooms();
        hearts = gameMap.getHearts();
        stones = gameMap.getStones();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private void startTimer() {
        timer = new Thread(() -> {
            Timer timer = new Timer(0, 0, 0);
            while (timer.getValue() != Constants.GAME_TIME) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer.next();
                System.out.println(timer);
            }
            handleEndOfGame();
        });
        timer.start();
    }

    public void initScene() {
        player.setGame(this);
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

    public void handleEndOfGame() {
        isDone = true;
        Platform.runLater(() -> {
            Stage secondStage = new Stage();
            AnchorPane pane = new AnchorPane();
            Label end = new Label("Time is Up - Score:" + player.getScore());
            end.setFont(Font.font(50));
            end.setTextAlignment(TextAlignment.CENTER);
            end.setLayoutX(50);
            end.setLayoutY(50);
            pane.getChildren().add(end);
            Scene scene = new Scene(pane, 700, 150, Color.WHITESMOKE);
            secondStage.setScene(scene);
            secondStage.setOnCloseRequest(event -> {
                Menu menu = new Menu();
                menu.start(stage);
                JavaFxApplication.handleMenu(stage, menu);
            });
            stage.close();
            secondStage.show();
        });
    }

    public void handleDie() {
        isDone = true;
        Platform.runLater(() -> {
            Stage secondStage = new Stage();
            AnchorPane pane = new AnchorPane();
            Label end = new Label("You died - Score:" + player.getScore());
            end.setFont(Font.font(50));
            end.setTextAlignment(TextAlignment.CENTER);
            end.setLayoutX(50);
            end.setLayoutY(50);
            pane.getChildren().add(end);
            Scene scene = new Scene(pane, 700, 150, Color.WHITESMOKE);
            secondStage.setScene(scene);
            secondStage.setOnCloseRequest(event -> {
                Menu menu = new Menu();
                menu.start(stage);
                JavaFxApplication.handleMenu(stage, menu);
            });
            stage.close();
            secondStage.show();
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
