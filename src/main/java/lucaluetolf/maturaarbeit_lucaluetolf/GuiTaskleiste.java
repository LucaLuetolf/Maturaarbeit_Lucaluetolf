package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class GuiTaskleiste {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    protected Statement statement;
    protected Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:h2:./DatenbankERPSoftware", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void toSceneStartseite(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneArtikel(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneKunden(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneNeueRechnung(ActionEvent event) {
        try {
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            resultSetRechnungsnummer.next();
            statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp) VALUES (" + resultSetRechnungsnummer.getInt("rechnungsnummer") + ",1);");
            resultSetRechnungsnummer.close();
            root = FXMLLoader.load(getClass().getResource("kundenFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Neue Nachricht:");
                    alert.setContentText("Die App kann momentan nicht geschlossen werden");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void toSceneVerkauf(ActionEvent event) {
        try {
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            resultSetRechnungsnummer.next();
            statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp) VALUES (" + resultSetRechnungsnummer.getInt("rechnungsnummer") + ",2);");
            resultSetRechnungsnummer.close();
            root = FXMLLoader.load(getClass().getResource("kundenFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Neue Nachricht:");
                    alert.setContentText("Die App kann momentan nicht geschlossen werden");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void toScenePdfBearbeiten(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        TextField textfield = new TextField();
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Bitte geben sie die Rechnungs- oder Quittungsnummer ein:");
        textfield.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event1) {
                textfield.setText(textfield.getText().replaceAll("[^0-9]", ""));
                textfield.positionCaret(textfield.getLength());
            }
        });

        alert.getDialogPane().setContent(textfield);
        alert.showAndWait();
        int rechnungsnummer = Integer.parseInt(textfield.getText());

        try {
            ResultSet resultsetUeberpruefen = statement.executeQuery("SELECT COUNT(bestellung_Id) FROM bearbeiter WHERE bestellung_Id = " + rechnungsnummer);
            resultsetUeberpruefen.next();
            if (resultsetUeberpruefen.getInt(1) == 1){
                statement.execute("UPDATE unternehmen SET bearbeiten = " + rechnungsnummer);
                root = FXMLLoader.load(getClass().getResource("artikelFuerRechnung.fxml"));
            } else{
                Alert alertKeineRechnung = new Alert(Alert.AlertType.INFORMATION);
                alertKeineRechnung.setTitle("Information Dialog");
                alertKeineRechnung.setHeaderText("Neue Information:");
                alertKeineRechnung.setContentText("Leider wurde keine Rechnung oder Quittung mit der Nummer: " + rechnungsnummer + " gefunden");
                alertKeineRechnung.showAndWait();
                root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
            }

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Neue Nachricht:");
                alert.setContentText("Die App kann momentan nicht geschlossen werden");
                alert.showAndWait();
            }
        });

    }

    @FXML
    protected void toSceneAnalyse(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("analyse.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    //Erfassen
    protected boolean tester(String regex, TextField textField) {
        boolean boolean1 = textField.getText().matches(regex);
        if (boolean1) {
            textField.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
        } else {
            textField.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
        return boolean1;
    }

    //Unternehmensdaten erfassen
    @FXML
    protected TextField textfeldUnternehmen;
    @FXML
    protected TextField textfeldBenutzername;
    @FXML
    protected TextField textfeldPasswort;
    @FXML
    protected TextField textfeldLagerbestandAnzeige;
    @FXML
    protected TextField textfeldBank;
    @FXML
    protected TextField textfeldIban;

    @FXML
    protected ImageView imageviewUnternehmen;

    protected boolean booleanUnternehmen = false;
    protected boolean booleanBenutzername = false;
    protected boolean booleanPasswort = false;
    protected boolean booleanLagerbestandAnzeige = false;
    protected boolean booleanBank = true;
    protected boolean booleanIban = true;

    protected String pfadBildLogo = "";
    protected String neuerPfadBildLogo = "Bilder/Benutzer/Unternehmen/";


    @FXML
    void bildAendernLogo(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilddateien", "*.png;*.jpeg;*.jpg;*.gif;*.svg");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            pfadBildLogo = selectedFile.getAbsolutePath();
            Image image = new Image(pfadBildLogo);
            imageviewUnternehmen.setImage(image);
        }
    }

    @FXML
    protected void textfieldUnternehmenKey(){
        textfeldUnternehmen.setText(textfeldUnternehmen.getText().replaceAll("[\\\\]", ""));
        textfeldUnternehmen.positionCaret(textfeldUnternehmen.getLength());
        booleanUnternehmen = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldUnternehmen);
        if (textfeldUnternehmen.getLength() != 0){
            booleanUnternehmen = true;
            textfeldUnternehmen.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
        } else{
            booleanUnternehmen = false;
            textfeldUnternehmen.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
    }
    @FXML
    protected void textfieldBenutzernameKey(){
        textfeldBenutzername.setText(textfeldBenutzername.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ ]", ""));
        textfeldBenutzername.positionCaret(textfeldBenutzername.getLength());
        booleanBenutzername = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldBenutzername);
    }
    @FXML
    protected void textfieldPasswortKey(){
        textfeldPasswort.setText(textfeldPasswort.getText().replaceAll("[^A-Za-z0-9éàèöäüÉÀÈÖÄÜ,.;:-_?!]", ""));
        textfeldPasswort.positionCaret(textfeldPasswort.getLength());
        booleanPasswort = tester("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}$", textfeldPasswort);
    }
    @FXML
    protected void textfieldLagerbestandAnzeigeKey(){
        textfeldLagerbestandAnzeige.setText(textfeldLagerbestandAnzeige.getText().replaceAll("[^0-9]", ""));
        textfeldLagerbestandAnzeige.positionCaret(textfeldLagerbestandAnzeige.getLength());
        booleanLagerbestandAnzeige = tester("^[1-9]\\d*$", textfeldLagerbestandAnzeige);
    }
    @FXML
    protected void textfieldBankKey(){
        textfeldBank.setText(textfeldBank.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ 0-9&.,'()/-]", ""));
        textfeldBank.positionCaret(textfeldBank.getLength());
        //booleanBank = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?(?:\\sAG)?$", textfeldBank);
        booleanBank = tester("^[A-Za-zéàèöäüÉÀÈÖÄÜ0-9&.,'()/-]+(?: [A-Za-zéàèöäüÉÀÈÖÄÜ0-9&.,'()/-]+)*$", textfeldBank);
        if(textfeldIban.getText() == "" && textfeldBank.getText() == ""){
            booleanIban = true;
            booleanBank = true;
        }
    }
    @FXML
    protected void textfieldIbanKey(){
        textfeldIban.setText(textfeldIban.getText().replaceAll("[^CH0-9 ]", ""));
        textfeldIban.positionCaret(textfeldIban.getLength());
        //booleanIban = tester("^CH[0-9]{19}$", textfeldIban);
        booleanIban = tester("^CH\\d{2} \\d{4} \\d{4} \\d{4} \\d{4} \\d{1}$", textfeldIban);
        if(textfeldIban.getText() == "" && textfeldBank.getText() == ""){
            booleanIban = true;
            booleanBank = true;
        }
    }


    //Artikel erfassen
    @FXML
    protected ImageView imageViewArtikel;
    @FXML
    protected TextField textfeldArtikelnummer;
    @FXML
    protected TextField textfeldName;
    @FXML
    protected TextField textfeldPreis;
    @FXML
    protected TextField textfeldMenge;
    @FXML
    protected TextField textfeldRabatt;
    @FXML
    protected TextField textfeldLagerbestand;
    @FXML
    protected ChoiceBox<String> choiceBoxMenge;

    protected boolean booleanArtikelnummer = false;
    protected boolean booleanName = false;
    protected boolean booleanPreis = false;
    protected boolean booleanMenge = false;
    protected boolean booleanRabatt = false;
    protected boolean booleanLagerbestand = false;

    protected String pfadBildArtikel = "";
    protected String neuerPfadBildArtikel = "Bilder/Benutzer/Artikel/Übergang/";


    @FXML
    protected void bildAendern() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilddateien", "*.png;*.jpeg;*.jpg;*.gif;*.svg");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            pfadBildArtikel = selectedFile.getAbsolutePath();
            Image image = new Image(pfadBildArtikel);
            imageViewArtikel.setImage(image);
        }
    }

    @FXML
    protected void textfieldArtikelnummerKey() {
        textfeldArtikelnummer.setText(textfeldArtikelnummer.getText().replaceAll("[^0-9]", ""));
        textfeldArtikelnummer.positionCaret(textfeldArtikelnummer.getLength());
        booleanArtikelnummer = tester("^[1-9]\\d*$", textfeldArtikelnummer);
        if (textfeldArtikelnummer.getText() != "" && booleanArtikelnummer){
            try {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT COUNT(artikelId) AS summe FROM artikel WHERE artikelId = " + textfeldArtikelnummer.getText());
                resultsetArtikel.next();
                int res = resultsetArtikel.getInt("summe");
                if (res == 0){
                    textfeldArtikelnummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
                }
                else{
                    textfeldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                    booleanArtikelnummer = false;
                }
                resultsetArtikel.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
        }
        if (textfeldArtikelnummer.getLength() > 8){
            booleanArtikelnummer = false;
            textfeldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
        }

    }

    @FXML
    protected void textfieldNameKey() {
        textfeldName.setText(textfeldName.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ0-9 -_.,!']", ""));
        textfeldName.positionCaret(textfeldName.getLength());
        booleanName = tester("^[A-Za-zéàèöäüÉÀÈÖÄÜ0-9 -_.,!]+$", textfeldName);
    }

    @FXML
    protected void textfieldPreisKey() {
        textfeldPreis.setText(textfeldPreis.getText().replaceAll("[^0-9.]", ""));
        textfeldPreis.positionCaret(textfeldPreis.getLength());
        booleanPreis = tester("^\\d+(\\.\\d{1}(0|5)?)?$", textfeldPreis);

    }

    @FXML
    protected void textfieldMengeKey() {
        textfeldMenge.setText(textfeldMenge.getText().replaceAll("[^0-9.]", ""));
        textfeldMenge.positionCaret(textfeldMenge.getLength());
        booleanMenge = tester("^\\d+(\\.\\d{1}(0|5)?)?$", textfeldMenge);
    }

    @FXML
    protected void textfieldRabattKey() {
        textfeldRabatt.setText(textfeldRabatt.getText().replaceAll("[^0-9]", ""));
        booleanRabatt = tester("^[0-9]\\d*$", textfeldRabatt);
        if (textfeldRabatt.getLength() != 0 && 100 < Integer.parseInt(textfeldRabatt.getText())) {
            textfeldRabatt.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Fehlermeldung");
            alert.setContentText("Der gewünschte Rabatt beträgt mehr als 100%");
            alert.showAndWait();
            textfeldRabatt.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
        textfeldRabatt.positionCaret(textfeldRabatt.getLength());

    }

    @FXML
    protected void textfieldLagerbestandKey() {
        textfeldLagerbestand.setText(textfeldLagerbestand.getText().replaceAll("[^0-9]", ""));
        textfeldLagerbestand.positionCaret(textfeldLagerbestand.getLength());
        booleanLagerbestand = tester("^[0-9]\\d*$", textfeldLagerbestand);
    }



    //Kunde erfassen

    @FXML
    protected TextField textfeldKundennummer;
    @FXML
    protected TextField textfeldNachname;
    @FXML
    protected TextField textfeldVorname;
    @FXML
    protected TextField textfeldAdresse;
    @FXML
    protected TextField textfeldPostleitzahl;
    @FXML
    protected TextField textfeldOrt;
    @FXML
    protected TextField textfeldEmail;
    @FXML
    protected TextField textfeldNatelnummer;
    protected boolean booleanKundennummer = false;
    protected boolean booleanNachname = false;
    protected boolean booleanVorname = false;
    protected boolean booleanAdresse = false;
    protected boolean booleanPostleitzahl = false;
    protected boolean booleanOrt = false;
    protected boolean booleanEmail = true;
    protected boolean booleanNatelnummer = true;

    @FXML
    protected void textfieldKundennummerKey() {
        textfeldKundennummer.setText(textfeldKundennummer.getText().replaceAll("[^0-9]", ""));
        textfeldKundennummer.positionCaret(textfeldKundennummer.getLength());
        booleanKundennummer = tester("^[1-9]\\d*$", textfeldKundennummer);
        if (textfeldKundennummer.getText() != "" && booleanKundennummer){
            try {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT COUNT(kundenId) AS summe FROM kunden WHERE kundenId = " + textfeldKundennummer.getText());
                resultsetArtikel.next();
                int res = resultsetArtikel.getInt("summe");
                if (res == 0){
                    textfeldKundennummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
                }
                else{
                    textfeldKundennummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                    booleanKundennummer = false;
                }
                resultsetArtikel.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
        }
    }

    @FXML
    protected void textfieldNachnameKey() {
        textfeldNachname.setText(textfeldNachname.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ '-]", ""));
        textfeldNachname.positionCaret(textfeldNachname.getLength());
        booleanNachname = tester("^[A-Za-zéàèöäüÉÀÈÖÄÜ'-]+(?: [A-Za-zéàèöäüÉÀÈÖÄÜ'-]+)*$", textfeldNachname);
    }

    @FXML
    protected void textfieldVornameKey() {
        textfeldVorname.setText(textfeldVorname.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ '-]", ""));
        textfeldVorname.positionCaret(textfeldVorname.getLength());
        booleanVorname = tester("^[A-Za-zéàèöäüÉÀÈÖÄÜ'-]+(?: [A-Za-zéàèöäüÉÀÈÖÄÜ'-]+)*$", textfeldVorname);
    }

    @FXML
    protected void textfieldAdresseKey() {
        textfeldAdresse.setText(textfeldAdresse.getText().replaceAll("[^A-za-zzéàèöäüÉÀÈÖÄÜ '0-9-]", ""));
        textfeldAdresse.positionCaret(textfeldAdresse.getLength());
        booleanAdresse = tester("^[A-Za-zzéàèöäüÉÀÈÖÄÜ'-]+(\\s\\d*)?$", textfeldAdresse);
    }

    @FXML
    protected void textfieldPostleitzahlKey() {
        textfeldPostleitzahl.setText(textfeldPostleitzahl.getText().replaceAll("[^0-9]", ""));
        textfeldPostleitzahl.positionCaret(textfeldPostleitzahl.getLength());
        booleanPostleitzahl = tester("^[0-9]{4}$", textfeldPostleitzahl);
    }

    @FXML
    protected void textfieldOrtKey() {
        textfeldOrt.setText(textfeldOrt.getText().replaceAll("[^A-Za-zzéàèöäüÉÀÈÖÄÜ '0-9-]", ""));
        textfeldOrt.positionCaret(textfeldOrt.getLength());
        booleanOrt = tester("^[A-Za-zéàèöäüÉÀÈÖÄÜ0-9'-]+(?: [A-Za-zéàèöäüÉÀÈÖÄÜ0-9'-]+)*$", textfeldOrt);
    }

    @FXML
    protected void textfieldEmailKey() {
        textfeldEmail.setText(textfeldEmail.getText().replaceAll(" ", ""));
        textfeldEmail.positionCaret(textfeldEmail.getLength());
        booleanEmail = tester("^[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+\\.[A-Za-z]{0,}$", textfeldEmail);
        if(textfeldEmail.getText().isEmpty()){
            booleanEmail = true;
        }
    }

    @FXML
    protected void textfieldNatelnummerKey() {
        textfeldNatelnummer.setText(textfeldNatelnummer.getText().replaceAll("[^0-9]", ""));
        textfeldNatelnummer.positionCaret(textfeldNatelnummer.getLength());
        booleanNatelnummer = tester("^(?:[0-9]{10}|)$", textfeldNatelnummer);
    }
}
