package ir.ac.kntu.items;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private GridPane pane;
    private Node node;
    private boolean isAlive = true;
    private boolean isPassable;
    private boolean destroyable;

    public Item(GridPane pane, Node node, boolean isPassable, boolean destroyable) {
        this.pane = pane;
        this.node = node;
        this.isPassable = isPassable;
        this.destroyable = destroyable;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    public int getRowIndex() {
        return pane.getRowIndex(node);
    }

    public int getColumnIndex() {
        return pane.getColumnIndex(node);
    }

    public void destroy() {
        Platform.runLater(() -> pane.getChildren().remove(node));
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        if (this.isAlive()) {
            this.node = node;
        } else {
            pane.getChildren().remove(node);
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public GridPane getPane() {
        return pane;
    }

    public void setPane(GridPane pane) {
        this.pane = pane;
    }
}
