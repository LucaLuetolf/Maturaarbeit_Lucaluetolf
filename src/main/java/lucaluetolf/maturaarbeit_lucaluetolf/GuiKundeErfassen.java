package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class GuiKundeErfassen extends GuiTaskleiste {
    @FXML
    protected void kundeErfassen(ActionEvent event) {
        if (booleanKundennummer && booleanNachname && booleanVorname && booleanAdresse && booleanPostleitzahl && booleanOrt && booleanEmail && booleanNatelnummer) {
            try {
                statement.execute("INSERT INTO kunden (kundenId, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer) VALUES (" + textfeldKundennummer.getText() + ",'" + textfeldNachname.getText() + "','" + textfeldVorname.getText() + "','" + textfeldAdresse.getText() + "'," + textfeldPostleitzahl.getText() + ",'" + textfeldOrt.getText() + "','" + textfeldEmail.getText() + "','" + textfeldNatelnummer.getText() + "')");
                root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
                AllgemeineMethoden.ordnerErstellen("Kundendateien/" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText());
                AllgemeineMethoden.ordnerErstellen("Kundendateien/" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText() + "/Quittungen");
                AllgemeineMethoden.ordnerErstellen("Kundendateien/" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText() + "/Rechnungen");
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            if (booleanKundennummer == false) {
                textfeldKundennummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                if(textfeldKundennummer.getText().matches("^[1-9]\\d*$") && textfeldKundennummer.getLength() > 0 && textfeldKundennummer.getLength() <= 8){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Fehlermeldung");
                    alert.setHeaderText("Diese Kundennummer ist bereits vergeben.");
                    alert.showAndWait();
                }
            }
            if (booleanNachname == false) {
                textfeldNachname.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanVorname == false) {
                textfeldVorname.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanAdresse == false) {
                textfeldAdresse.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanPostleitzahl == false) {
                textfeldPostleitzahl.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanOrt == false) {
                textfeldOrt.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanEmail == false) {
                textfeldEmail.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanNatelnummer == false) {
                textfeldNatelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
        }

    }

}
