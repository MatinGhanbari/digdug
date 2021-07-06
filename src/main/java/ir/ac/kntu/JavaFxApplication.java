package ir.ac.kntu;

import ir.ac.kntu.scene.Game;
import ir.ac.kntu.util.GameMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        root.setStyle("-fx-border-width: 0 0 5 0;");
        Scene scene = new Scene(root, 800, 600, Color.rgb(240, 240, 240));

        Game game = new Game(new GameMap());
        game.start(stage);

        // Setting stage properties
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("DigDig");

        stage.setScene(scene);
        stage.show();
    }
}
