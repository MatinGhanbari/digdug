package ir.ac.kntu.items;

import ir.ac.kntu.scene.Game;
import ir.ac.kntu.util.BalloonType;
import ir.ac.kntu.util.Direction;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Balloon extends Item {
    private BalloonType balloonType;
    private String rootAddress = "assets/";
    private String name = "balloon/";
    private String address;
    private String state = "right_standing";
    private int rowIndex;
    private int columnIndex;
    private Game game;
    private Player player;
    private int row;
    private int col;

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

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BalloonType getBalloonType() {
        return balloonType;
    }

    public void setBalloonType(BalloonType balloonType) {
        this.balloonType = balloonType;
    }

    public String getRootAddress() {
        return rootAddress;
    }

    public void setRootAddress(String rootAddress) {
        this.rootAddress = rootAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public void handleMove() {
        while (this.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getNode() == null) {
                address = rootAddress + name + "right_standing" + ".png";
                setNode(new ImageView(new Image(address)));
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                rowIndex = getPane().getRowIndex(getNode());
                columnIndex = getPane().getColumnIndex(getNode());
            } catch (NullPointerException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                rowIndex = getPane().getRowIndex(getNode());
                columnIndex = getPane().getColumnIndex(getNode());
            }
            Node oldNode = getNode();
            moveAI();
            Platform.runLater(() -> {
                try {
                    getPane().getChildren().remove(oldNode);
                    getPane().add(getNode(), columnIndex, rowIndex);
                } catch (IllegalArgumentException ignore) {
                }
            });
            Runnable setState = () -> {
                switch (state) {
                    case "up_moving":
                    case "right_moving":
                        state = "right_standing";
                        break;
                    case "left_moving":
                    case "down_moving":
                        state = "left_standing";
                    default:
                        break;
                }
                address = rootAddress + name + state + ".png";
                getPane().getChildren().remove(getNode());
                setNode(new ImageView(new Image(address)));
                getPane().add(getNode(), columnIndex, rowIndex);
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
    }

    public void moveAI() {
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
    }

    private boolean thereIsImpassableItem(int columnIndex, int rowIndex, Direction dir) {
        for (Dirt w : player.getDirts()) {
            if (w.getRowIndex() == rowIndex && w.getColumnIndex() == columnIndex) {
                return true;
            }
        }
        return player.thereIsImpassableItem(columnIndex, rowIndex, dir);
    }

    private boolean canMove(Direction dir) {
        switch (dir) {
            case UP:
                if (rowIndex == 0 || thereIsImpassableItem(columnIndex, rowIndex - 1, dir)) {
                    return false;
                }
                break;
            case DOWN:
                if (rowIndex == getPane().getRowCount() - 1 || thereIsImpassableItem(columnIndex, rowIndex + 1, dir)) {
                    return false;
                }
                break;
            case LEFT:
                if (columnIndex == 0 || thereIsImpassableItem(columnIndex - 1, rowIndex, dir)) {
                    return false;
                }
                break;
            case RIGHT:
                if (columnIndex == getPane().getColumnCount() - 1 ||
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
        if (game.getMap().hasPlayer(rowIndex, columnIndex)) {
            player.die();
        }
        setState(dir);
        address = rootAddress + name + state + ".png";
        setNode(new ImageView(new Image(address)));
    }

    private void setState(Direction dir) {
        switch (dir) {
            case UP:
            case RIGHT:
                state = "right_moving";
                break;
            case DOWN:
            case LEFT:
                state = "left_moving";
                break;
            default:
                break;
        }
    }

    @Override
    public void destroy() {
        setAlive(false);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        row = this.getRowIndex();
        col = this.getColumnIndex();
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        setNode(new ImageView(new Image("assets/balloon/" + balloonType.toString().toLowerCase() + "/die/die_1.png")));
        Platform.runLater(() -> getPane().add(getNode(), col, row));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        row = this.getRowIndex();
        col = this.getColumnIndex();
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        setNode(new ImageView(new Image("assets/balloon/" + balloonType.toString().toLowerCase() + "/die/die_2.png")));
        Platform.runLater(() -> getPane().add(getNode(), col, row));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        row = this.getRowIndex();
        col = this.getColumnIndex();
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        setNode(new ImageView(new Image("assets/balloon/" + balloonType.toString().toLowerCase() + "/die/die_3.png")));
        Platform.runLater(() -> getPane().add(getNode(), col, row));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        row = this.getRowIndex();
        col = this.getColumnIndex();
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        setNode(new ImageView(new Image("assets/balloon/" + balloonType.toString().toLowerCase() + "/die/die_4.png")));
        Platform.runLater(() -> getPane().add(getNode(), col, row));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        game.getMap().getBalloons().remove(this);
        game.setLists();
        if (game.getMap().getBalloons().size() <= 0) {
            player.win();
        }
    }
}
