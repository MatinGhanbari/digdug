package ir.ac.kntu.items;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Stone extends Item {
    public Stone(GridPane pane, Node node, boolean isPassable) {
        super(pane, node, isPassable, true);
    }
}
