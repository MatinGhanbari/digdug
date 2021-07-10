package ir.ac.kntu;

import ir.ac.kntu.scene.Game;
import ir.ac.kntu.scene.Menu;
import ir.ac.kntu.util.GameMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Menu menu = new Menu();
        menu.start(stage);
        handleMenu(stage, menu);
    }

    public void handleMenu(Stage stage, Menu menu) {
        Game game = new Game(new GameMap());
        menu.getTextField().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                game.setPlayerName(menu.getTextField().getText());
                game.start(stage);
                return;
            }
        });
        menu.getPlayButton().setOnMouseClicked(e -> {
            game.setPlayerName(menu.getTextField().getText());
            game.start(stage);
        });
    }

}
