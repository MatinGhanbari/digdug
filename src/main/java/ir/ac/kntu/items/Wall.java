package ir.ac.kntu.items;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Wall extends Item {
    public Wall(GridPane pane, Node node, boolean isPassable, boolean destroyable) {
        super(pane, node, isPassable, destroyable);
    }
}
