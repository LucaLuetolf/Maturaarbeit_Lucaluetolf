package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class GuiKundeErfassen extends GuiTaskleiste {

    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
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
    boolean booleanKundennummer = false;
    boolean booleanNachname = false;
    boolean booleanVorname = false;
    boolean booleanAdresse = false;
    boolean booleanPostleitzahl = false;
    boolean booleanOrt = false;
    boolean booleanEmail = false;
    boolean booleanNatelnummer = false;

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
    protected void textfieldKundennummerKey() {
        textfeldKundennummer.setText(textfeldKundennummer.getText().replaceAll("[^0-9]", ""));
        textfeldKundennummer.positionCaret(textfeldKundennummer.getLength());
        booleanKundennummer = tester("^[1-9]\\d*$", textfeldKundennummer);
        if (textfeldKundennummer.getText() != ""){
            try {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT COUNT(kundenId) AS summe FROM kunden WHERE kundenId = " + textfeldKundennummer.getText());
                resultsetArtikel.next();
                int res = resultsetArtikel.getInt("summe");
                if (res == 0){
                    textfeldKundennummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
                }
                else{
                    textfeldKundennummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                resultsetArtikel.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
        }
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
            booleanNatelnummer = tester("^[1-9]\\d*$", textfeldNatelnummer);
        }
    }

    @FXML
    protected void kundeErfassen(ActionEvent event) {
        if (booleanKundennummer && booleanNachname && booleanVorname && booleanAdresse && booleanPostleitzahl && booleanOrt && booleanEmail && booleanNatelnummer) {
            try {
                statement.execute("INSERT INTO kunden (kundenId, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer) VALUES (" + textfeldKundennummer.getText() + ",'" + textfeldNachname.getText() + "','" + textfeldVorname.getText() + "','" + textfeldAdresse.getText() + "'," + textfeldPostleitzahl.getText() + ",'" + textfeldOrt.getText() + "','" + textfeldEmail.getText() + "'," + textfeldNatelnummer.getText() + ")");
                root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
                AllgemeineMethoden.ordnerErstellen("Kundendateien\\" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText());
                AllgemeineMethoden.ordnerErstellen("Kundendateien\\" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText() + "\\Quittungen");
                AllgemeineMethoden.ordnerErstellen("Kundendateien\\" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText() + "\\Rechnungen");
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
