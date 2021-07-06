package ir.ac.kntu.items;

import ir.ac.kntu.Constants.Constants;
import ir.ac.kntu.scene.Game;
import ir.ac.kntu.util.BalloonType;
import ir.ac.kntu.util.Direction;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Balloon extends Item {
    private BalloonType balloonType;
    private String rootAddress = "assets/";
    private String name = "balloon/";
    private String address;
    private String state;
    private GridPane pane;
    private Node node;
    private int rowIndex;
    private int columnIndex;
    private ArrayList<Stone> stones;
    private ArrayList<Wall> walls;
    private ArrayList<Dirt> dirts;
    private Game game;
    private Player player;

    public Balloon(GridPane pane, Node node, boolean isPassable, boolean destroyable, BalloonType balloonType) {
        super(pane, node, isPassable, destroyable);
        this.balloonType = balloonType;
        switch (balloonType) {
            case DRAGON:
                name += "dragon/balloon_dragon_";
                break;
            case SIMPLE:
            default:
                name += "simple/balloon_simple_";
                break;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLists(ArrayList<Dirt> dirts, ArrayList<Stone> stones, ArrayList<Wall> walls) {
        this.dirts = dirts;
        this.stones = stones;
        this.walls = walls;
    }

    public void handleMove() {
        rowIndex = pane.getRowIndex(node);
        columnIndex = pane.getColumnIndex(node);
        pane.getChildren().remove(node);
        switch ((int) (Math.random() * 4)) {
            case 0:
                change(Direction.UP);
                break;
            case 1:
                change(Direction.RIGHT);
                break;
            case 2:
                change(Direction.DOWN);
                break;
            case 3:
                change(Direction.LEFT);
                break;
            default:
                break;
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
        for (Dirt w : dirts) {
            if (w.getRowIndex() == rowIndex && w.getColumnIndex() == columnIndex) {
                return true;
            }
        }
        return false;
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

    private void change(Direction dir) {
        int i = 1;
        if (!canMove(dir)) {
            return;
        }
        switch (dir) {
            case UP:
                rowIndex -= i;
                break;
            case DOWN:
                rowIndex += i;
                break;
            case LEFT:
                columnIndex -= i;
                break;
            case RIGHT:
                columnIndex += i;
                break;
            default:
                break;
        }
        if (game.getMap().hasPlayer(rowIndex, columnIndex)) {
            player.die();
        }
        setState(dir);
        address = rootAddress + name + state + ".png";
        node = new ImageView(new Image(address));
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
}
