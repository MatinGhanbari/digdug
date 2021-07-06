package ir.ac.kntu.util;


import ir.ac.kntu.items.*;
import ir.ac.kntu.util.Direction;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameMap {
    private int[][] items;
    private GridPane pane;
    private Player player;
    private ArrayList<Dirt> dirts;
    private ArrayList<Stone> stones;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Heart> hearts;
    private Scene scene;

    public GameMap() {
        dirts = new ArrayList<>();
        stones = new ArrayList<>();
        mushrooms = new ArrayList<>();
        hearts = new ArrayList<>();
        pane = new GridPane();
        initMap();
        pane.setVgap(1);
        pane.setHgap(1);
        scene = new Scene(pane, pane.getColumnCount() * 50 + 85, pane.getRowCount() * 50 + 70, Color.GREEN);
        startRandomObjects();
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
                    case 0:
                        temp = new ImageView(new Image("assets/map/dirt.png"));
                        dirts.add(new Dirt(pane, temp, false));
                        break;
                    case 7:
                        temp = new ImageView(new Image("assets/map/heart.png"));
                        hearts.add(new Heart(pane, temp, true));
                        break;
                    case 8:
                        temp = new ImageView(new Image("assets/map/mushroom.png"));
                        mushrooms.add(new Mushroom(pane, temp, false));
                        break;
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

                temp = new ImageView(new Image("assets/player/player_down_standing.png"));
                player = new Player(pane, temp);

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

    public void startRandomObjects() {
        Random random = new Random();
        new Thread(() -> {
            while (true) {
                Node temp = null;
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int objtype = random.nextInt(5) + 1;
                int row = random.nextInt(pane.getRowCount() - 2) + 1;
                int col = random.nextInt(pane.getColumnCount() - 2) + 1;
                while (!isValidCoordinates(row, col)) {
                    row = random.nextInt(pane.getRowCount() - 2) + 1;
                    col = random.nextInt(pane.getColumnCount() - 2) + 1;
                }
                switch (objtype) {
                    case 1: // powerU
                        temp = new ImageView(new Image("assets/map/mushroom.png"));
                        mushrooms.add(new Mushroom(pane, temp, true));
                        break;
                    case 2:
                        temp = new ImageView(new Image("assets/map/heart.png"));
                        hearts.add(new Heart(pane, temp, true));
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
        }).start();
    }

    private boolean isValidCoordinates(int row, int column) {
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

}
