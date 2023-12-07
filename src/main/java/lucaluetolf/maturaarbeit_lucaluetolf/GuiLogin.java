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
import java.sql.*;

public class GuiLogin extends GuiTaskleiste {

    String benutzername1 = null;
    String passwort1 = null;

    {
      try {
          ResultSet resultset = statement.executeQuery("SELECT * FROM UNTERNEHMEN");
          if (resultset.next()){
              benutzername1 = resultset.getString("benutzername");
              passwort1 = resultset.getString("passwort");
          }
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
    }

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