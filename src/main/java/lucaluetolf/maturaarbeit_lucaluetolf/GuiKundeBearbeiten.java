package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiKundeBearbeiten extends GuiTaskleiste implements Initializable {
    private int kundennummer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        booleanNachname = true;
        booleanVorname = true;
        booleanAdresse = true;
        booleanPostleitzahl = true;
        booleanOrt = true;
        booleanEmail = true;
        booleanNatelnummer = true;

        try {
            textfeldKundennummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldNachname.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldVorname.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldAdresse.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldPostleitzahl.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldOrt.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldEmail.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldNatelnummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unternehmen");
            resultSet.next();
            kundennummer = resultSet.getInt("bearbeiten");
            resultSet.close();
            statement.execute("UPDATE unternehmen SET bearbeiten = null");
            ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden WHERE kundenId = " + kundennummer);
            resultSetKunde.next();
            textfeldKundennummer.setText(resultSetKunde.getString("kundenId"));
            textfeldKundennummer.setEditable(false);
            textfeldNachname.setText(resultSetKunde.getString("nachname"));
            textfeldVorname.setText(resultSetKunde.getString("vorname"));
            textfeldAdresse.setText(resultSetKunde.getString("adresse"));
            textfeldPostleitzahl.setText(String.valueOf(resultSetKunde.getInt("postleitzahl")));
            textfeldOrt.setText(resultSetKunde.getString("ort"));
            textfeldEmail.setText(resultSetKunde.getString("email"));
            textfeldNatelnummer.setText(resultSetKunde.getString("natelnummer"));
            resultSetKunde.close();

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    @FXML
    protected void speichern(ActionEvent event) {

        if (booleanNachname && booleanVorname && booleanAdresse && booleanPostleitzahl && booleanOrt && booleanEmail && booleanNatelnummer) {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden WHERE kundenId = " + kundennummer);
                resultSet.next();
                File file = new File(String.valueOf("Kundendateien/" + kundennummer + ", " + resultSet.getString("nachname") + " " + resultSet.getString("vorname")));
                file.renameTo(new File(String.valueOf("Kundendateien/" + kundennummer + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText())));
                statement.execute("UPDATE kunden SET nachname = '" + textfeldNachname.getText() + "', vorname = '" + textfeldVorname.getText() + "', adresse = '" + textfeldAdresse.getText() + "', postleitzahl = " + textfeldPostleitzahl.getText() + ", ort = '" + textfeldOrt.getText() + "', email = '" + textfeldEmail.getText() + "', natelnummer = '" + textfeldNatelnummer.getText() + "' WHERE kundenId = " + kundennummer);
                root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
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
