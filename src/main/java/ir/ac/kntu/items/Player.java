package ir.ac.kntu.items;

import ir.ac.kntu.Constants.Constants;
import ir.ac.kntu.scene.Game;
import ir.ac.kntu.util.Direction;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private GridPane pane;
    private Node node;
    private Game game;
    private int rowIndex;
    private int columnIndex;
    private List<KeyCode> keys = new ArrayList<>();
    private ArrayList<Dirt> dirts;
    private ArrayList<Stone> stones;
    private ArrayList<Wall> walls;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Heart> hearts;
    private String rootAddress = "assets/";
    private String name = "player/player_";
    private String address;
    private String state;
    private int score;
    private boolean isAlive;
    private boolean hasPower = true;
    private int health = 3;

    public Player(GridPane pane, Node node) {
        this.pane = pane;
        this.node = node;
        score = 0;
        isAlive = true;
        state = "right_standing";
        address = rootAddress + name + state + ".png";
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
        this.isAlive = false;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void change(Direction dir) {
        if (!canMove(dir)) {
            return;
        }
        switch (dir) {
            case UP:
                rowIndex -= 1;
                break;
            case DOWN:
                rowIndex += 1;
                break;
            case LEFT:
                columnIndex -= 1;
                break;
            case RIGHT:
                columnIndex += 1;
                break;
            default:
                break;
        }
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
                    break;
                case DOWN:
                    rowIndex += 1;
                    break;
                case LEFT:
                    columnIndex -= 1;
                    break;
                case RIGHT:
                    columnIndex += 1;
                    break;
                default:
                    break;
            }
        }
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
        setState(dir);
        address = rootAddress + name + state + ".png";
        node = new ImageView(new Image(address));
    }

    private boolean canMove(Direction dir) {
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

    private boolean thereIsImpassableItem(int columnIndex, int rowIndex, Direction dir) {
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
                         ArrayList<Wall> walls, ArrayList<Mushroom> mushrooms,
                         ArrayList<Heart> hearts) {
        this.dirts = dirts;
        this.stones = stones;
        this.walls = walls;
        this.mushrooms = mushrooms;
        this.hearts = hearts;
    }

    private void setState(Direction dir) {
        switch (dir) {
            case UP:
                state = "up_moving";
                break;
            case DOWN:
                state = "down_moving";
                break;
            case LEFT:
                state = "left_moving";
                break;
            case RIGHT:
                state = "right_moving";
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

        Runnable setState = () -> {
            switch (state) {
                case "right_moving":
                    state = "right_standing";
                    break;
                case "up_moving":
                    state = "up_standing";
                    break;
                case "down_moving":
                    state = "down_standing";
                    break;
                case "left_moving":
                    state = "left_standing";
                    break;
                default:
                    break;
            }
            address = rootAddress + name + state + ".png";
            pane.getChildren().remove(node);
            node = new ImageView(new Image(address));
            pane.add(node, columnIndex, rowIndex);
        };

        new Thread(() -> {
            try {
                Thread.sleep(200);
                Platform.runLater(setState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        handleMoveOfStone(columnIndex, rowIndex);
        for (Dirt dirt : dirts) {
            if (dirt.getRowIndex() == rowIndex && dirt.getColumnIndex() == columnIndex) {
                dirt.destroy();
                break;
            }
        }
        try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleMoveOfStone(int columnIndex, int rowIndex) {
        for (Stone stone : stones) {
            if (stone.getColumnIndex() == columnIndex - 1 && stone.getRowIndex() == rowIndex) {
                new Thread(() -> {
//                    while (stone.get) {
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }).start();
            }
        }
    }

    private void shoot() {
    }

    private int getMaxScore() {
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
}
