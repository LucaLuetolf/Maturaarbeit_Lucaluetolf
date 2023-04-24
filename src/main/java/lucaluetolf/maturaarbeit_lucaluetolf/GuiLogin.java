package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiLogin {
    private String passwort1 = "Hans";
    private String benutzername1 = "Hans";

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField benutzername;
    @FXML
    private PasswordField passwort;
    @FXML
    private Label falscheEingabe;

    @FXML
    public void login(ActionEvent event) {
        if (benutzername.getText().equals(benutzername1) && benutzername.getText().length() == benutzername1.length() && passwort.getText().equals(passwort1) && passwort.getText().length() == passwort1.length()) {
            try {
                root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            falscheEingabe.setText("Falscher Benutzername oder Passwort");
        }
    }
}
