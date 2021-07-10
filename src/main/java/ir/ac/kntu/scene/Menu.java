package ir.ac.kntu.scene;

import ir.ac.kntu.Constants.Constants;
import ir.ac.kntu.DAO.PlayerInfo;
import ir.ac.kntu.util.GameMap;
import ir.ac.kntu.DAO.GameSerialization;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class Menu extends Application {
    private Scene scene;
    private BorderPane pane;
    private TextField textField;
    private Button btn;
    private ListView<PlayerInfo> listView = new ListView<>();

    @Override
    public void start(Stage stage) {
        pane = new BorderPane();
        scene = new Scene(pane, Color.GREEN);
        stage.setScene(scene);
        stage.setTitle(Constants.GAME_TITLE);
        btn = new Button("Play");
        textField = new TextField("Enter Your Name");
        HBox hBox = new HBox();
        ArrayList<PlayerInfo> players = new GameSerialization().getAllPlayers();
        if (players != null) {
            listView.getItems().addAll(players);
            System.out.println(players);
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        AtomicReference<PlayerInfo> result = new AtomicReference<>(null);
        listView.setOnMouseClicked(e -> {
            try {
                result.set((PlayerInfo) listView.getSelectionModel().getSelectedItem());
            } catch (NullPointerException ignore) {
            }
            if (result.get() != null) {
                Game game = new Game(new GameMap());
                game.setPlayerName(result.get().getPlayerName());
                game.start(stage);
                return;
            }
        });
        hBox.getChildren().add(listView);
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
