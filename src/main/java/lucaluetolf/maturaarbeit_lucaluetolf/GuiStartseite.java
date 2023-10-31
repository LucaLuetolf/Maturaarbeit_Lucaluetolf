package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class GuiStartseite extends GuiTaskleiste implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

    @FXML
    private Label labelWillkommen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int time1 = Integer.parseInt(formatter.format(time));
        /*if (time1 >= 6 && time1 < 11) {
            labelWillkommen.setText("Good Morning");
        }
        if (time1 >= 11 && time1 < 13) {
            labelWillkommen.setText("Good Noon");
        }
        if (time1 >= 13 && time1 < 17) {
            labelWillkommen.setText("Good Afternoon");
        }
        if (time1 >= 17 && time1 < 22) {
            labelWillkommen.setText("Good Evening");
        }
        if (time1 >= 22 && time1 < 6) {
            labelWillkommen.setText("Good Night");
        }*/
    }

    @FXML
    protected void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Möchten sie sich ausloggen?");
        ButtonType ja = new ButtonType("ja");
        ButtonType nein = new ButtonType("nein");
        alert.getButtonTypes().setAll(ja, nein);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ja) {
            try {
                root = FXMLLoader.load(getClass().getResource("login.fxml"));
            } catch (IOException e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            alert.close();
        }
    }


    @FXML
    protected void toSceneEinstellungen(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("einstellungen.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
