package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiArtikel extends GuiLeiste implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private GridPane gridpaneArtikel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int i = 0; i < 7; i++) {
            Pane pane = new Pane();
            Button button = new Button("Button");
            pane.setPrefSize(170,193);
            pane.getChildren().add(button);
            pane.setStyle("-fx-background-color: #E8CFB0");
            gridpaneArtikel.setPrefSize(850, 1351);
            gridpaneArtikel.addColumn(4);
            gridpaneArtikel.add(pane,0,i);
        }

        gridpaneArtikel.setGridLinesVisible(true);
        gridpaneArtikel.setStyle("-fx-border-color: #ffffff");

    }

    @FXML
    protected void toSceneArtikelErfassen(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("artikelErfassen.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
