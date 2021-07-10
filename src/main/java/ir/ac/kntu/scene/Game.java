package ir.ac.kntu.scene;

import ir.ac.kntu.constants.Constants;
import ir.ac.kntu.JavaFxApplication;
import ir.ac.kntu.items.*;
import ir.ac.kntu.util.GameMap;
import ir.ac.kntu.util.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Application implements Serializable {
    private Player player;
    private ArrayList<Dirt> dirts;
    private ArrayList<Balloon> balloons;
    private GameMap gameMap;
    private final Scene scene;
    private final GridPane pane;
    private boolean isDone = false;
    private final Timer timer = new Timer(0, 0, 0);
    private String playerName = "Player";
    private Stage stage;
    private final Label time = new Label();
    private final Label health = new Label();
    private final Label score = new Label();
    private Thread timerThread;

    public Game(GameMap gameMap) {
        this.gameMap = gameMap;
        scene = gameMap.getScene();
        pane = gameMap.getPane();
        player = gameMap.getPlayer();
        dirts = gameMap.getDirts();
    }

    @Override
    public void start(Stage stage) {
        final Stage[] finalStage = {stage};
        Platform.runLater(() -> {
            finalStage[0].close();
            finalStage[0] = new Stage();
            this.stage = finalStage[0];
            initLabels();
            initScene();
            initBalloons();
            finalStage[0].initStyle(StageStyle.UTILITY);
            finalStage[0].setScene(scene);
            finalStage[0].setResizable(false);
            finalStage[0].setTitle("DigDug - Player : " + playerName);
            System.out.println("Game started!");
            System.out.println("Player Name : \"" + playerName + "\"");
            finalStage[0].setOnCloseRequest(event -> System.exit(0));
            finalStage[0].show();
        });
    }

    public void initBalloons() {
        setLists();
        for (Balloon balloon : balloons) {
            balloon.setPlayer(player);
            balloon.setGame(this);
            try {
                Thread balloonThread = new Thread(balloon::handleMove);
                balloonThread.setPriority(Thread.NORM_PRIORITY);
                balloonThread.start();
            } catch (NullPointerException ignore) {
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setLists() {
        player = gameMap.getPlayer();
        balloons = gameMap.getBalloons();
        dirts = gameMap.getDirts();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        player.setPlayerName(playerName);
    }

    private void initLabels() {
        startTimer();
        gameMap.setHealth(health);
        gameMap.setScore(score);
        timerThread = new Thread(() -> {
            while (timer.getValue() * 1000 != Constants.GAME_TIME) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> health.setText("Hearts: " + player.getHealth()));
                Platform.runLater(() -> score.setText("Score: " + player.getScore()));
            }
            handleEndOfGame();
        });
        timerThread.start();
    }

    private void startTimer() {
        gameMap.setTimer(time);
        timerThread = new Thread(() -> {
            while (timer.getValue() * 1000 != Constants.GAME_TIME) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer.next();
                Platform.runLater(() -> time.setText(timer.toString()));
            }
            handleEndOfGame();
        });
        timerThread.start();
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
            stage.close();
            stage = new Stage();
            AnchorPane pane = new AnchorPane();
            Label end = new Label("You died - Score:" + player.getScore());
            end.setFont(Font.font(50));
            end.setTextAlignment(TextAlignment.CENTER);
            end.setLayoutX(50);
            end.setLayoutY(50);
            pane.getChildren().add(end);
            Scene scene = new Scene(pane, 700, 150, Color.WHITESMOKE);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.show();
        });
    }

    public void handleWin() {
        isDone = true;
        Platform.runLater(() -> {
            stage.close();
            stage = new Stage();
            AnchorPane pane = new AnchorPane();
            Label end = new Label("You are win - Score:" + player.getScore());
            end.setFont(Font.font(50));
            end.setTextAlignment(TextAlignment.CENTER);
            end.setLayoutX(50);
            end.setLayoutY(50);
            pane.getChildren().add(end);
            Scene scene = new Scene(pane, 700, 150, Color.WHITESMOKE);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.show();
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

    public void checkSmash(int columnIndex, int rowIndex) {
        for (Balloon balloon : (ArrayList<Balloon>) balloons.clone()) {
            if (balloon.getColumnIndex() == columnIndex && balloon.getRowIndex() == rowIndex) {
                player.setScore(player.getScore() + (int) (Math.random() * 500));
                smashBalloon(balloon);
            }
        }
        if (player.getRowIndex() == rowIndex && player.getColumnIndex() == columnIndex) {
            smashPlayer(player);
        }
    }

    public void smashPlayer(Player player) {
        int col = player.getColumnIndex();
        int row = player.getRowIndex();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> pane.getChildren().remove(player));
        player.setNode(new ImageView(new Image("assets/player/smashed.png")));
        Platform.runLater(() -> pane.add(player.getNode(), col, row));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.die();
    }

    public void smashBalloon(Balloon balloon) {
        balloon.setAlive(false);
        balloons.remove(balloon);
        int col = balloon.getColumnIndex();
        int row = balloon.getRowIndex();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> pane.getChildren().remove(balloon));
        balloon.setNode(new ImageView(new Image("assets/balloon/" +
                balloon.getBalloonType().toString().toLowerCase() + "/smashed.png")));
        Platform.runLater(() -> pane.add(balloon.getNode(), col, row));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> pane.getChildren().remove(balloon));
    }

    public Label getHealth() {
        return health;
    }

    public Label getScore() {
        return score;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public Player getPlayer() {
        return player;
    }

    public void setDone() {
        this.isDone = true;
    }

    public Balloon hasEnemyInRange(String name, int i) {
        switch (name) {
            case "right/":
                return checkEnemyRight(i);
            case "up/":
                return checkEnemyUp(i);
            case "down/":
                return checkEnemyDown(i);
            case "left/":
                return checkEnemyLeft(i);
            default:
                break;
        }
        return null;
    }

    private Balloon checkEnemyLeft(int i) {
        for (Balloon balloon : balloons) {
            if (balloon.getRowIndex() == player.getRowIndex() &&
                    balloon.getColumnIndex() == player.getColumnIndex() - i) {
                return balloon;
            }
        }
        return null;
    }

    private Balloon checkEnemyDown(int i) {
        for (Balloon balloon : balloons) {
            if (balloon.getRowIndex() == player.getRowIndex() + i &&
                    balloon.getColumnIndex() == player.getColumnIndex()) {
                return balloon;
            }
        }
        return null;
    }

    private Balloon checkEnemyUp(int i) {
        for (Balloon balloon : balloons) {
            if (balloon.getRowIndex() == player.getRowIndex() - i &&
                    balloon.getColumnIndex() == player.getColumnIndex()) {
                return balloon;
            }
        }
        return null;
    }

    private Balloon checkEnemyRight(int i) {
        for (Balloon balloon : balloons) {
            if (balloon.getRowIndex() == player.getRowIndex() &&
                    balloon.getColumnIndex() == player.getColumnIndex() + i) {
                return balloon;
            }
        }
        return null;
    }
}
