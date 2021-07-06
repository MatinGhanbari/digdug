package ir.ac.kntu.items;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Dirt extends Item {
    public Dirt(GridPane pane, Node node, boolean isPassable) {
        super(pane, node, isPassable, true);
    }
}
