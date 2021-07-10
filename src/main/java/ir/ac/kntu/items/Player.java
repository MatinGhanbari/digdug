package ir.ac.kntu.items;

import ir.ac.kntu.Constants.Constants;
import ir.ac.kntu.scene.Game;
import ir.ac.kntu.util.Direction;
import ir.ac.kntu.util.GameSerialization;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Serializable {
    private String playerName;
    private final GridPane pane;
    private Node node;
    private Game game;
    private int rowIndex;
    private int columnIndex;
    private final List<KeyCode> keys = new ArrayList<>();
    private ArrayList<Dirt> dirts;
    private ArrayList<Stone> stones;
    private ArrayList<Wall> walls;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Heart> hearts;
    private final String rootAddress = "assets/player/";
    private String name = "right/";
    private String address;
    private String state = "standing";
    private int score = 0;
    private boolean isAlive = true;
    private boolean hasPower = false;
    private int health = 3;
    private boolean isDirtHere = false;
    private Dirt hereDirt = null;
    private Thread destroy = new Thread(() -> {
    });

    public Player(GridPane pane, Node node) {
        this.pane = pane;
        this.node = node;
        initKeys();
    }

    private void initKeys() {
        keys.add(KeyCode.W);
        keys.add(KeyCode.D);
        keys.add(KeyCode.S);
        keys.add(KeyCode.A);
        keys.add(KeyCode.SPACE);
    }

    public int getRowIndex() {
        return pane.getRowIndex(node);
    }

    public int getColumnIndex() {
        return pane.getColumnIndex(node);
    }

    public void die() {
        if (!game.isDone()) {
            GameSerialization gameSerialization = new GameSerialization();
            gameSerialization.saveUser(this);
            this.isAlive = false;
            game.handleDie();
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void change(Direction dir) {
        setNameOrdinary(dir);
        for (Dirt dirt : dirts) {
            if (dirt.getRowIndex() == rowIndex && dirt.getColumnIndex() == columnIndex) {
                dirt.destroy();
                break;
            }
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setNameHasPower(dir);
        if (game.getMap().hasPower(rowIndex, columnIndex)) {
            new Thread(() -> {
                hasPower = true;
                try {
                    Thread.sleep(Constants.REMOVE_MUSHROOM_EFFECT_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hasPower = false;
            }).start();
        } else if (game.getMap().hasHeart(rowIndex, columnIndex)) {
            this.health++;
        }
        for (Dirt dirt : dirts) {
            if (dirt.getRowIndex() == rowIndex && dirt.getColumnIndex() == columnIndex) {
                isDirtHere = true;
                hereDirt = dirt;
                break;
            }
        }
        setState(dir);
        address = rootAddress + name + state + ".png";
        node = new ImageView(new Image(address));
    }

    private void setNameOrdinary(Direction dir) {
        if (!canMove(dir)) {
            return;
        }
        switch (dir) {
            case UP:
                rowIndex -= 1;
                name = "up/";
                break;
            case DOWN:
                rowIndex += 1;
                name = "down/";
                break;
            case LEFT:
                columnIndex -= 1;
                name = "left/";
                break;
            case RIGHT:
                columnIndex += 1;
                name = "right/";
                break;
            default:
                break;
        }
    }

    private void setNameHasPower(Direction dir) {
        if (this.hasPower) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!canMove(dir)) {
                return;
            }
            switch (dir) {
                case UP:
                    rowIndex -= 1;
                    name = "up/";
                    break;
                case DOWN:
                    rowIndex += 1;
                    name = "down/";
                    break;
                case LEFT:
                    columnIndex -= 1;
                    name = "left/";
                    break;
                case RIGHT:
                    columnIndex += 1;
                    name = "right/";
                    break;
                default:
                    break;
            }
        }
    }

    public boolean canMove(Direction dir) {
        switch (dir) {
            case UP:
                if (rowIndex == 0 || thereIsImpassableItem(columnIndex, rowIndex - 1, dir)) {
                    return false;
                }
                break;
            case DOWN:
                if (rowIndex == pane.getRowCount() - 1 || thereIsImpassableItem(columnIndex, rowIndex + 1, dir)) {
                    return false;
                }
                break;
            case LEFT:
                if (columnIndex == 0 || thereIsImpassableItem(columnIndex - 1, rowIndex, dir)) {
                    return false;
                }
                break;
            case RIGHT:
                if (columnIndex == pane.getColumnCount() - 1 ||
                        thereIsImpassableItem(columnIndex + 1, rowIndex, dir)) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public ArrayList<Dirt> getDirts() {
        return dirts;
    }

    public boolean thereIsImpassableItem(int columnIndex, int rowIndex, Direction dir) {
        for (Wall w : walls) {
            if (w.getRowIndex() == rowIndex && w.getColumnIndex() == columnIndex) {
                return true;
            }
        }
        for (Stone w : stones) {
            if (w.getRowIndex() == rowIndex && w.getColumnIndex() == columnIndex) {
                return true;
            }
        }
        return false;
    }

    public void setLists(ArrayList<Dirt> dirts, ArrayList<Stone> stones,
                         ArrayList<Wall> walls) {
        this.dirts = dirts;
        this.stones = stones;
        this.walls = walls;
    }

    public void setItems(ArrayList<Mushroom> mushrooms, ArrayList<Heart> hearts) {
        this.mushrooms = mushrooms;
        this.hearts = hearts;
    }

    public void setState(Direction dir) {
        String dig = "";
        if (isDirtHere) {
            hereDirt.destroy();
            dirts.remove(hereDirt);
            dig += "_dig";
        }
        switch (dir) {
            case UP:
                state = "moving" + dig;
                name = "up/";
                break;
            case DOWN:
                state = "moving" + dig;
                name = "down/";
                break;
            case LEFT:
                state = "moving" + dig;
                name = "left/";
                break;
            case RIGHT:
                state = "moving" + dig;
                name = "right/";
                break;
            default:
                break;
        }
    }

    public void handleMove(KeyCode temp) {
        rowIndex = pane.getRowIndex(node);
        columnIndex = pane.getColumnIndex(node);
        pane.getChildren().remove(node);
        if (temp.equals(keys.get(0))) {
            change(Direction.UP);
        } else if (temp.equals(keys.get(1))) {
            change(Direction.RIGHT);
        } else if (temp.equals(keys.get(2))) {
            change(Direction.DOWN);
        } else if (temp.equals(keys.get(3))) {
            change(Direction.LEFT);
        } else if (temp.equals(keys.get(4))) {
            shoot();
        }
        pane.add(node, columnIndex, rowIndex);

        Runnable setState = generateSetStateRunnable();

        new Thread(() -> {
            try {
                Thread.sleep(120);
                Platform.runLater(setState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                handleMoveOfStone(columnIndex, rowIndex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(this::checkDied).start();
    }

    private void checkDied() {
        for (Balloon balloon : (ArrayList<Balloon>) game.getMap().getBalloons().clone()) {
            if (this.getColumnIndex() == balloon.getColumnIndex()
                    && this.getRowIndex() == balloon.getRowIndex()) {
                die();
            }
        }
    }

    private Runnable generateSetStateRunnable() {
        Runnable setState = () -> {
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String dig = "";
            if (isDirtHere) {
                dig += "_dig";
            }
            if (state.equalsIgnoreCase("moving_dig") || state.equals("moving")) {
                state = "standing" + dig;
            }

            address = rootAddress + name + state + ".png";
            pane.getChildren().remove(node);
            node = new ImageView(new Image(address));
            pane.add(node, columnIndex, rowIndex);

            if (isDirtHere) {
                try {
                    Thread.sleep(120);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (state.equalsIgnoreCase("standing_dig")) {
                    state = "standing";
                }
                isDirtHere = false;
            }

            address = rootAddress + name + state + ".png";
            pane.getChildren().remove(node);
            node = new ImageView(new Image(address));
            pane.add(node, columnIndex, rowIndex);
        };
        return setState;
    }

    public void handleMoveOfStone(int columnIndex, int rowIndex) throws InterruptedException {
        Thread.sleep(50);
        for (Stone stone : (ArrayList<Stone>) stones.clone()) {
            if (stone.getColumnIndex() == columnIndex && stone.getRowIndex() + 1 == rowIndex) {
                while (true) {
                    Thread.sleep(50);
                    if (this.rowIndex != rowIndex || this.columnIndex != columnIndex) {
                        int fallCount = 0;
                        int stoneRow = stone.getRowIndex();
                        while (true) {
                            fallCount++;
                            int finalFallCount = fallCount;
                            Thread.sleep(50);
                            Platform.runLater(() -> {
                                pane.getChildren().remove(stone.getNode());
                                pane.add(stone.getNode(), columnIndex, stone.getRowIndex() + 1);
                            });
                            new Thread(() -> game.checkSmash(stone.getColumnIndex(), stone.getRowIndex())).start();
                            Thread.sleep(50);
                            if (stone.getRowIndex() >= getMinRowOfDirts(stoneRow, columnIndex) - 1) {
                                if (finalFallCount >= 2) {
                                    stones.remove(stone);
                                    destroy = new Thread(stone::destroy);
                                    destroy.start();
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void setNode(Node node) {
        this.node = node;
    }

    private int getMinRowOfDirts(int index, int columnIndex) {
        int min = 100000;
        for (Dirt dirt : dirts) {
            if (dirt.getColumnIndex() == columnIndex && index < dirt.getRowIndex() && dirt.getRowIndex() < min) {
                min = dirt.getRowIndex();
            }
        }
        if (min == 100000) {
            return pane.getRowCount();
        }
        return min;
    }

    public void shoot() {
    }

    public int getMaxScore() {
        return score;
    }

    public ArrayList<KeyCode> getKeys() {
        return (ArrayList<KeyCode>) keys;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getScore() {
        return score;
    }

    public void setGame(Game g) {
        game = g;
    }

    public Node getNode() {
        return node;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
