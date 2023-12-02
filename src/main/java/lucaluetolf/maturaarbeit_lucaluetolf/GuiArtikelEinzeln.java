package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiArtikelEinzeln extends GuiTaskleiste implements Initializable {

    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextField textfeldArtikelnummer;
    @FXML
    private TextField textfeldName;
    @FXML
    private TextField textfeldPreis;
    @FXML
    private TextField textfeldMenge;
    @FXML
    private TextField textfeldRabatt;
    @FXML
    private TextField textfeldLagerbestand;
    @FXML
    private ChoiceBox<String> choiceBoxMenge;
    @FXML
    private JFXButton buttonArtikelBearbeiten;
    @FXML
    private JFXButton buttonBildAendern;
    @FXML
    private JFXButton buttonZurueck;
    @FXML
    private ImageView imageviewArtikel;
    @FXML
    private Label labelArtikelnummer;
    @FXML
    private Label labelName;
    @FXML
    private Label labelPreis;
    @FXML
    private Label labelMenge;
    @FXML
    private Label labelRabatt;
    @FXML
    private Label labelLagerbestand;


    private boolean booleanArtikelnummer = true;
    private boolean booleanName = true;
    private boolean booleanPreis = true;
    private boolean booleanMenge = true;
    private boolean booleanRabatt = true;
    private boolean booleanLagerbestand = true;
    private Stage stage;
    private Scene scene;
    private Parent root;

    int artikelnummer;
    private String filePath = "";
    private String newPath = "Bilder/Benutzer/Artikel/";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unternehmen");
            resultSet.next();
            artikelnummer = resultSet.getInt("bearbeiten");
            resultSet.close();
            statement.execute("UPDATE unternehmen SET bearbeiten = null");
            option1();
            buttonArtikelBearbeiten.setId("1");
            buttonZurueck.setId("1");
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        ObservableList<String> werte = FXCollections.observableArrayList();
        try {
            ResultSet resultSetEinheiten = statement.executeQuery("SELECT * FROM einheiten WHERE aktiv = true");
            while (resultSetEinheiten.next()){
                werte.add(resultSetEinheiten.getString("abkuerzung"));
            }
            resultSetEinheiten.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        choiceBoxMenge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (choiceBoxMenge.getSelectionModel().getSelectedIndex() >= 0){
                    choiceBoxMenge.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
                }
            }
        });
        choiceBoxMenge.setItems(werte);
    }

    @FXML
    protected void artikelBearbeiten() {
        if (buttonArtikelBearbeiten.getId() == "1") {
            option2();
            buttonArtikelBearbeiten.setId("2");
            buttonArtikelBearbeiten.setText("speichern");
            buttonZurueck.setId("2");
            buttonZurueck.setText("abbrechen");
        } else {
            if (booleanName && booleanPreis && booleanMenge && booleanRabatt && booleanLagerbestand) {
                try {
                    ResultSet resultsetEinheit = statement.executeQuery("SELECT * FROM einheiten WHERE abkuerzung = '" + choiceBoxMenge.getSelectionModel().getSelectedItem()+ "'");
                    resultsetEinheit.next();
                    statement.execute("UPDATE artikel SET " + "name = '" + textfeldName.getText() + "', " + "preis = " + textfeldPreis.getText() + ", " + "menge = " + textfeldMenge.getText() + ", " + "einheit_id = " + resultsetEinheit.getInt("einheitId") + ", " + "rabatt = " + textfeldRabatt.getText() + ", " + "lagerbestand = " + textfeldLagerbestand.getText() + " WHERE artikelId = " + textfeldArtikelnummer.getText());
                    resultsetEinheit.close();

                    if (filePath != ""){
                        newPath = newPath + textfeldArtikelnummer.getText() + "/";
                        AllgemeineMethoden.dateiKopieren(filePath, newPath);
                        ResultSet resultSetDateityp = statement.executeQuery("SELECT * FROM artikel WHERE artikelId = " + artikelnummer);
                        resultSetDateityp.next();
                        String dateityp = resultSetDateityp.getString("dateityp");
                        int bildnummer = resultSetDateityp.getInt("bildnummer");
                        resultSetDateityp.close();
                        /*if (bildnummer != 0){
                            statement.execute("INSERT INTO loeschen (artikel_Id, bildnummer, dateityp) VALUES (" + textfeldArtikelnummer.getText() + "," + bildnummer + ",'" + dateityp + "')");
                        }*/

                        File neuesBildAlterPfad = new File(filePath);
                        File neuesBildAlterName = new File(newPath + "/" + neuesBildAlterPfad.getName());
                        System.out.println(neuesBildAlterName.toPath());
                        String dateitypNeu = "";
                        int index = neuesBildAlterPfad.getName().lastIndexOf(".");
                        if (index > 0) {
                            dateitypNeu = neuesBildAlterPfad.getName().substring(index + 1);
                        }
                        File neuesBildNeuerName = new File(String.valueOf(getClass().getResource("/Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText() + "/" + (bildnummer+1) + "." + dateitypNeu)));
                        String neuesBildNeuerNamePfad = String.valueOf("Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText() + "/" + (bildnummer+1) + "." + dateitypNeu);
                        System.out.println(neuesBildNeuerNamePfad);
                        if (neuesBildAlterName.renameTo(new File(neuesBildNeuerNamePfad))){
                            statement.execute("UPDATE artikel SET dateityp = '" + dateitypNeu + "', bildnummer = " + (bildnummer+1) + "WHERE artikelId = " + textfeldArtikelnummer.getText());
                            System.out.println("true");
                        } else {
                            System.out.println("false");
                        }
                    }

                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                option1();
                buttonArtikelBearbeiten.setId("1");
                buttonArtikelBearbeiten.setText("bearbeiten");
                buttonZurueck.setId("1");
                buttonZurueck.setText("zurück");

            } else {
                if (booleanArtikelnummer == false) {
                    textfeldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanName == false) {
                    textfeldName.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanPreis == false) {
                    textfeldPreis.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanMenge == false) {
                    textfeldMenge.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanRabatt == false) {
                    textfeldRabatt.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanLagerbestand == false) {
                    textfeldLagerbestand.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
            }

        }
    }

    @FXML
    protected void abbrechen(ActionEvent event) {
        if (buttonZurueck.getId() == "1") {
            try {
                root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
            } catch (IOException e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            option1();
            buttonZurueck.setId("1");
            buttonZurueck.setText("zurück");
            buttonArtikelBearbeiten.setId("1");
            buttonArtikelBearbeiten.setText("bearbeiten");
        }
    }


    @FXML
    protected void bildAendern() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilddateien", "*.png;*.jpeg;*.jpg;*.gif;*.svg");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            Image image = new Image(filePath);
            imageviewArtikel.setImage(image);
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
    protected void textfieldNameKey() {
        textfeldName.setText(textfeldName.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ0-9]", ""));
        textfeldName.positionCaret(textfeldName.getLength());
        booleanName = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldName);
    }

    //TODO Regex Preis anpassen, momentan keine Stelle vor dem Punkt. Unendlich viele Punkte möglich
    @FXML
    protected void textfieldPreisKey() {
        textfeldPreis.setText(textfeldPreis.getText().replaceAll("[^0-9.]", ""));
        textfeldPreis.positionCaret(textfeldPreis.getLength());
        booleanPreis = tester("^\\d+(\\.\\d{1}(0|5)?)?$", textfeldPreis);
    }

    @FXML
    protected void textfieldMengeKey() {
        textfeldMenge.setText(textfeldMenge.getText().replaceAll("[^0-9.]",""));
        textfeldMenge.positionCaret(textfeldMenge.getLength());
        booleanMenge = tester("^\\d+(\\.\\d{1}(0|5)?)?$", textfeldMenge);
    }

    @FXML
    protected void textfieldRabattKey() {
        textfeldRabatt.setText(textfeldRabatt.getText().replaceAll("[^0-9]", ""));
        if (100 < Integer.parseInt(textfeldRabatt.getText())) {
            textfeldRabatt.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Fehlermeldung");
            alert.setContentText("Der gewünschte Rabatt beträgt mehr als 100%");
            alert.showAndWait();
            booleanRabatt = tester("[0-9]", textfeldRabatt);
        }
        textfeldRabatt.positionCaret(textfeldRabatt.getLength());

    }

    @FXML
    protected void textfieldLagerbestandKey() {
        textfeldLagerbestand.setText(textfeldLagerbestand.getText().replaceAll("[^0-9]", ""));
        textfeldLagerbestand.positionCaret(textfeldLagerbestand.getLength());
        booleanLagerbestand = tester("^[0-9]\\d*$", textfeldLagerbestand);
    }

    private void option1() {
        try {
            ResultSet resultSetArtikel = statement.executeQuery("SELECT * FROM artikel, einheiten WHERE artikelId = " + artikelnummer + " AND einheitId = einheit_id");
            resultSetArtikel.next();
            labelArtikelnummer.setText(String.valueOf(resultSetArtikel.getInt("artikelId")));
            labelName.setText(resultSetArtikel.getString("name"));
            labelPreis.setText(String.valueOf(resultSetArtikel.getDouble("preis")));
            labelMenge.setText(String.valueOf(resultSetArtikel.getDouble("menge") + " " + resultSetArtikel.getString("abkuerzung")));
            labelRabatt.setText(String.valueOf(resultSetArtikel.getDouble("rabatt")));
            labelLagerbestand.setText(String.valueOf(resultSetArtikel.getInt("lagerbestand")));
            if (resultSetArtikel.getString("dateityp") == null){
                Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Artikel/Artikel.png")));
                imageviewArtikel.setImage(image);
            }else{
                Image image = new Image(new FileInputStream("Bilder/Benutzer/Artikel/" + resultSetArtikel.getInt("artikelId") + "/" + resultSetArtikel.getInt("bildnummer") + "." + resultSetArtikel.getString("dateityp")));
                imageviewArtikel.setImage(image);
            }
            resultSetArtikel.close();
            textfeldArtikelnummer.setVisible(false);
            textfeldName.setVisible(false);
            textfeldPreis.setVisible(false);
            textfeldMenge.setVisible(false);
            choiceBoxMenge.setVisible(false);
            textfeldRabatt.setVisible(false);
            textfeldLagerbestand.setVisible(false);
            buttonBildAendern.setVisible(false);
            labelArtikelnummer.setVisible(true);
            labelName.setVisible(true);
            labelPreis.setVisible(true);
            labelMenge.setVisible(true);
            labelRabatt.setVisible(true);
            labelLagerbestand.setVisible(true);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    private void option2() {
        try {
            ResultSet resultSetArtikel = statement.executeQuery("SELECT * FROM artikel, einheiten WHERE artikelId = " + artikelnummer + " AND einheitId = einheit_id");
            resultSetArtikel.next();
            textfeldArtikelnummer.setText(String.valueOf(resultSetArtikel.getInt("artikelId")));
            textfeldName.setText(resultSetArtikel.getString("name"));
            textfeldPreis.setText(String.valueOf(resultSetArtikel.getDouble("preis")));
            textfeldMenge.setText(String.valueOf(resultSetArtikel.getDouble("menge")));
            textfeldRabatt.setText(String.valueOf(resultSetArtikel.getDouble("rabatt")));
            textfeldLagerbestand.setText(String.valueOf(resultSetArtikel.getInt("lagerbestand")));
            choiceBoxMenge.setValue(resultSetArtikel.getString("abkuerzung"));
            if (resultSetArtikel.getString("dateityp") == null){
                Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Artikel/Artikel.png")));
                imageviewArtikel.setImage(image);
            }else{
                Image image = new Image(new FileInputStream("Bilder/Benutzer/Artikel/" + resultSetArtikel.getInt("artikelId") + "/" + resultSetArtikel.getInt("bildnummer") + "." + resultSetArtikel.getString("dateityp")));
                imageviewArtikel.setImage(image);
            }
            resultSetArtikel.close();
            textfeldArtikelnummer.setVisible(true);
            textfeldArtikelnummer.setEditable(false);
            textfeldArtikelnummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldName.setVisible(true);
            textfeldName.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldPreis.setVisible(true);
            textfeldPreis.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldMenge.setVisible(true);
            textfeldMenge.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            choiceBoxMenge.setVisible(true);
            choiceBoxMenge.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldRabatt.setVisible(true);
            textfeldRabatt.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldLagerbestand.setVisible(true);
            textfeldLagerbestand.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            buttonBildAendern.setVisible(true);
            labelArtikelnummer.setVisible(false);
            labelName.setVisible(false);
            labelPreis.setVisible(false);
            labelMenge.setVisible(false);
            labelRabatt.setVisible(false);
            labelLagerbestand.setVisible(false);
            resultSetArtikel.close();

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

}
