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
    boolean booleanNachname = true;
    boolean booleanVorname = true;
    boolean booleanAdresse = true;
    boolean booleanPostleitzahl = true;
    boolean booleanOrt = true;
    boolean booleanEmail = true;
    boolean booleanNatelnummer = true;

    private int kundennummer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            textfeldNatelnummer.setText(String.valueOf(resultSetKunde.getInt("natelnummer")));
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

    private boolean tester(String regex, TextField textField) {
        boolean boolean1 = textField.getText().matches(regex);
        if (boolean1) {
            textField.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
        } else {
            textField.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
        return boolean1;
    }

    @FXML
    protected void textfieldNachnameKey() {
        textfeldNachname.setText(textfeldNachname.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ ]", ""));
        textfeldNachname.positionCaret(textfeldNachname.getLength());
        booleanNachname = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldNachname);
    }

    @FXML
    protected void textfieldVornameKey() {
        textfeldVorname.setText(textfeldVorname.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ ]", ""));
        textfeldVorname.positionCaret(textfeldVorname.getLength());
        booleanVorname = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldVorname);
    }

    @FXML
    protected void textfieldAdresseKey() {
        textfeldAdresse.setText(textfeldAdresse.getText().replaceAll("^[^A-za-z]+ [^0-9]$", ""));
        textfeldAdresse.positionCaret(textfeldAdresse.getLength());
        booleanAdresse = tester("^[A-Za-z]+(\\s\\d*)?$", textfeldAdresse);
    }

    @FXML
    protected void textfieldPostleitzahlKey() {
        textfeldPostleitzahl.setText(textfeldPostleitzahl.getText().replaceAll("[^0-9]", ""));
        textfeldPostleitzahl.positionCaret(textfeldPostleitzahl.getLength());
        booleanPostleitzahl = tester("^[0-9]{4}$", textfeldPostleitzahl);
    }

    @FXML
    protected void textfieldOrtKey() {
        textfeldOrt.setText(textfeldOrt.getText().replaceAll("[^A-Za-z]", ""));
        textfeldOrt.positionCaret(textfeldOrt.getLength());
        booleanOrt = tester("[A-Z][a-z]+$", textfeldOrt);
    }

    @FXML
    protected void textfieldEmailKey() {
        booleanEmail = tester("^[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+\\.[A-Za-z]{2,}$", textfeldEmail);
    }

    @FXML
    protected void textfieldNatelnummerKey() {
        textfeldNatelnummer.setText(textfeldNatelnummer.getText().replaceAll("[^0-9]", ""));
        textfeldNatelnummer.positionCaret(textfeldNatelnummer.getLength());
        if (textfeldNatelnummer.getLength() == 10){
            booleanNatelnummer = tester("^[0-9]\\d*$", textfeldNatelnummer);
        } else{
            textfeldNatelnummer.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
    }

}
