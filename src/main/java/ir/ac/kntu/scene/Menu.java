package ir.ac.kntu.scene;

import ir.ac.kntu.Constants.Constants;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Menu extends Application {
    private Scene scene;
    private BorderPane pane;
    private TextField textField;
    private Button btn;

    @Override
    public void start(Stage stage) {
        pane = new BorderPane();
        scene = new Scene(pane, Color.GREEN);
        stage.setScene(scene);
        stage.setTitle(Constants.GAME_TITLE);
        btn = new Button("Play");
        textField = new TextField("Enter Your Name");
        HBox hBox = new HBox();
        hBox.getChildren().add(textField);
        hBox.getChildren().add(btn);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        Node menuBack = new ImageView(new Image("assets/map/menu_back.png"));
        GridPane gridPane = new GridPane();
        gridPane.add(menuBack, 0, 0);
        gridPane.add(hBox, 0, 0);
        GridPane.setHalignment(hBox, HPos.CENTER);
        pane.setCenter(gridPane);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    public TextField getTextField() {
        return textField;
    }

    public Button getPlayButton() {
        return btn;
    }

}
