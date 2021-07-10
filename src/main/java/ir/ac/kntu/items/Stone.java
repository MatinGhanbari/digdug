package ir.ac.kntu.items;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Stone extends Item {
    private int row;
    private int col;

    public Stone(GridPane pane, Node node, boolean isPassable) {
        super(pane, node, isPassable, true);
    }

    @Override
    public void destroy() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        row = this.getRowIndex();
        col = this.getColumnIndex();
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        setNode(new ImageView(new Image("assets/gameObjects/stone_2.png")));
        Platform.runLater(() -> getPane().add(getNode(), col, row));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        row = this.getRowIndex();
        col = this.getColumnIndex();
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
        setNode(new ImageView(new Image("assets/gameObjects/stone_3.png")));
        Platform.runLater(() -> getPane().add(getNode(), col, row));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> getPane().getChildren().remove(getNode()));
    }
}
