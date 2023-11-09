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
    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField textfeldKundennummer;
    @FXML
    private TextField textfeldNachname;
    @FXML
    private TextField textfeldVorname;
    @FXML
    private TextField textfeldAdresse;
    @FXML
    private TextField textfeldPostleitzahl;
    @FXML
    private TextField textfeldOrt;
    @FXML
    private TextField textfeldEmail;
    @FXML
    private TextField textfeldNatelnummer;

    private int kundennummer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
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
            textfeldNatelnummer.setText(String.valueOf(resultSetKunde.getInt("natelnummer")));
            resultSetKunde.close();

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    @FXML
    protected void speichern(ActionEvent event) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden WHERE kundenId = " + kundennummer);
            resultSet.next();
            File file = new File("Kundendateien\\" + kundennummer + ", " + resultSet.getString("nachname") + " " + resultSet.getString("vorname"));
            file.renameTo(new File("Kundendateien\\" + kundennummer + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText()));
            statement.execute("UPDATE kunden SET nachname = '" + textfeldNachname.getText() + "', vorname = '" + textfeldVorname.getText() + "', adresse = '" + textfeldAdresse.getText() + "', postleitzahl = " + textfeldPostleitzahl.getText() + ", ort = '" + textfeldOrt.getText() + "', email = '" + textfeldEmail.getText() + "', natelnummer = " + textfeldNatelnummer.getText() + "WHERE kundenId = " + kundennummer);
            root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
