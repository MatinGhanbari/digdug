package ir.ac.kntu.util;


import ir.ac.kntu.constants.Constants;
import ir.ac.kntu.items.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameMap {
    private int[][] items;
    private GridPane pane = new GridPane();
    private AnchorPane topPane = new AnchorPane();
    private Player player;
    private final ArrayList<Dirt> dirts = new ArrayList<>();
    private final ArrayList<Stone> stones = new ArrayList<>();
    private final ArrayList<Mushroom> mushrooms = new ArrayList<>();
    private final ArrayList<Heart> hearts = new ArrayList<>();
    private final ArrayList<Gun> guns = new ArrayList<>();
    private final ArrayList<Balloon> simpleBalloons = new ArrayList<>();
    private final ArrayList<Balloon> dragonBalloons = new ArrayList<>();
    private final ArrayList<Flower> flowers = new ArrayList<>();
    private final ArrayList<Wall> walls = new ArrayList<>();
    private Scene scene;
    private final BorderPane mainPane = new BorderPane();

    public GameMap() {
        initMap();
        pane.setVgap(0.5);
        pane.setHgap(0.5);
        ImageView nightSky = new ImageView(new Image("assets/nightSky.png", pane.getColumnCount() * 30 + 18,
                pane.getRowCount() * 30 + 150, true, false));
        Label label = new Label();
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font(100));
        topPane.getChildren().addAll(nightSky, label);
        mainPane.setTop(topPane);
        mainPane.setBottom(pane);
        scene = new Scene(mainPane, pane.getColumnCount() * 30 + 18, pane.getRowCount() * 30 + 217, Color.BLACK);
        new Thread(this::startRandomObjects).start();
    }

    private void initMap() {
        loadMapFromFile();
        initBackGround();
        initOtherItems();
        initPlayers();
    }

    public Scene getScene() {
        return scene;
    }

    public void initBackGround() {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                pane.add(new ImageView(new Image("assets/map/normal.png")), j, i);
            }
        }
    }

    private void initOtherItems() {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                Node temp = null;
                switch (items[i][j]) {
                    case 9:
                        temp = new ImageView(new Image("assets/map/wall.png"));
                        walls.add(new Wall(pane, temp, false, false));
                        break;
                    case 1:
                    case 11:
                        temp = new ImageView(new Image("assets/dirts/dirt1.png"));
                        dirts.add(new Dirt(pane, temp, false));
                        break;
                    case 12:
                        temp = new ImageView(new Image("assets/dirts/dirt2.png"));
                        dirts.add(new Dirt(pane, temp, false));
                        break;
                    case 13:
                        temp = new ImageView(new Image("assets/dirts/dirt3.png"));
                        dirts.add(new Dirt(pane, temp, false));
                        break;
                    case 14:
                        temp = new ImageView(new Image("assets/dirts/dirt4.png"));
                        dirts.add(new Dirt(pane, temp, false));
                        break;
                    case 3:
                        temp = new ImageView(new Image("assets/balloon/simple/balloon_simple_right_standing.png"));
                        simpleBalloons.add(new Balloon(pane, temp, true, true, BalloonType.SIMPLE));
                        break;
                    case 5:
                        temp = new ImageView(new Image("assets/gameObjects/stone.png"));
                        stones.add(new Stone(pane, temp, true));
                        break;
                    case 6:
                        temp = new ImageView(new Image("assets/gameObjects/flower.png"));
                        flowers.add(new Flower(pane, temp, true));
                        break;
                    case 4:
                        temp = new ImageView(new Image("assets/balloon/dragon/balloon_dragon_right_standing.png"));
                        dragonBalloons.add(new Balloon(pane, temp, true, true, BalloonType.DRAGON));
                    default:
                        break;
                }
                if (temp != null) {
                    pane.add(temp, j, i);
                }
            }
        }
    }

    public void initPlayers() {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                Node temp = null;
                if (items[i][j] == 2) {
                    temp = new ImageView(new Image("assets/player/right/standing.png"));
                    player = new Player(pane, temp);
                    player.setLists(dirts, stones, walls);
                    player.setItems(mushrooms, hearts);
                }
                if (temp != null) {
                    pane.add(temp, j, i);
                }
            }
        }
    }

    public void loadMapFromFile() {
        File file = null;
        file = new File("src/main/resources/maps/map1.txt");
        if (file.exists()) {
            try (Scanner in = new Scanner(file)) {
                items = new int[in.nextInt()][in.nextInt()];
                for (int i = 0; i < items.length; i++) {
                    for (int j = 0; j < items[i].length; j++) {
                        items[i][j] = in.nextInt();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Dirt> getDirts() {
        return dirts;
    }

    public ArrayList<Stone> getStones() {
        return stones;
    }

    public ArrayList<Mushroom> getMushrooms() {
        return mushrooms;
    }

    public ArrayList<Heart> getHearts() {
        return hearts;
    }

    public GridPane getPane() {
        return pane;
    }

    public boolean hasPower(int rowIndex, int columnIndex) {
        for (Mushroom p : mushrooms) {
            if (p.getRowIndex() == rowIndex && p.getColumnIndex() == columnIndex) {
                p.destroy();
                mushrooms.remove(p);
                return true;
            }
        }
        return false;
    }

    public boolean hasHeart(int rowIndex, int columnIndex) {
        for (Heart p : hearts) {
            if (p.getRowIndex() == rowIndex && p.getColumnIndex() == columnIndex) {
                p.destroy();
                hearts.remove(p);
                return true;
            }
        }
        return false;
    }

    public boolean hasGun(int rowIndex, int columnIndex) {
        for (Gun p : guns) {
            if (p.getRowIndex() == rowIndex && p.getColumnIndex() == columnIndex) {
                p.destroy();
                guns.remove(p);
                return true;
            }
        }
        return false;
    }

    private void startRandomObjects() {
        Random random = new Random();
        while (true) {
            Node temp = null;
            try {
                Thread.sleep(Constants.NEW_ITEM_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int objType = random.nextInt(3) + 1;
            int row = random.nextInt(pane.getRowCount() - 2) + 1;
            int col = random.nextInt(pane.getColumnCount() - 2) + 1;
            while (!isValidCoordinates(row, col)) {
                row = random.nextInt(pane.getRowCount() - 2) + 1;
                col = random.nextInt(pane.getColumnCount() - 2) + 1;
            }
            switch (objType) {
                case 1: // mushroom
                    temp = new ImageView(new Image("assets/gameObjects/mushroom.png"));
                    mushrooms.add(new Mushroom(pane, temp, true));
                    break;
                case 2: // heart
                    temp = new ImageView(new Image("assets/gameObjects/heart.png"));
                    hearts.add(new Heart(pane, temp, true));
                    break;
                case 3: // gun
                    temp = new ImageView(new Image("assets/gameObjects/gun.png"));
                    guns.add(new Gun(pane, temp, true));
                default:
                    break;
            }
            if (temp != null) {
                Node finalTemp = temp;
                int finalRow = row;
                int finalCol = col;
                Platform.runLater(() -> pane.add(finalTemp, finalCol, finalRow));
            }
        }
    }

    private boolean isItem(int row, int col) {
        for (Mushroom w : mushrooms) {
            if (w.getRowIndex() == row && w.getColumnIndex() == col) {
                return true;
            }
        }
        for (Heart w : hearts) {
            if (w.getRowIndex() == row && w.getColumnIndex() == col) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCoordinates(int row, int column) {
        for (Wall w : walls) {
            if (w.getRowIndex() == row && w.getColumnIndex() == column) {
                return false;
            }
        }
        for (Stone w : stones) {
            if (w.getRowIndex() == row && w.getColumnIndex() == column) {
                return false;
            }
        }
        for (Dirt w : dirts) {
            if (w.getRowIndex() == row && w.getColumnIndex() == column) {
                return false;
            }
        }
        if (player.getRowIndex() == row && player.getColumnIndex() == column) {
            return false;
        }
        for (Mushroom w : mushrooms) {
            if (w.getRowIndex() == row && w.getColumnIndex() == column) {
                return false;
            }
        }
        for (Heart w : hearts) {
            if (w.getRowIndex() == row && w.getColumnIndex() == column) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPlayer(int rowIndex, int columnIndex) {
        return player.getRowIndex() == rowIndex && player.getColumnIndex() == columnIndex;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Balloon> getBalloons() {
        ArrayList<Balloon> balloons = new ArrayList<>();
        balloons.addAll(simpleBalloons);
        balloons.addAll(dragonBalloons);
        return balloons;
    }

    public void setTimer(Label time) {
        time.setTextFill(Color.WHITESMOKE);
        time.setFont(Font.font(20));
        time.setLayoutX(5);
        time.setLayoutY(5);
        topPane.getChildren().add(time);
    }

    public void setHealth(Label health) {
        health.setText("Hearts: " + player.getHealth());
        health.setTextFill(Color.WHITESMOKE);
        health.setFont(Font.font(20));
        health.setLayoutX(250);
        health.setLayoutY(5);
        topPane.getChildren().add(health);
    }

    public void setScore(Label score) {
        score.setText("Score: " + player.getScore());
        score.setTextFill(Color.WHITESMOKE);
        score.setFont(Font.font(20));
        score.setLayoutX(400);
        score.setLayoutY(5);
        topPane.getChildren().add(score);
    }
}
