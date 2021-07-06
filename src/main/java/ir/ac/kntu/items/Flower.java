package ir.ac.kntu.items;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Flower extends Item {
    public Flower(GridPane pane, Node node, boolean isPassable) {
        super(pane, node, isPassable, true);
    }
}
