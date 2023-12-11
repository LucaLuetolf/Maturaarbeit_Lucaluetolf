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

    int artikelnummer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booleanArtikelnummer = true;
        booleanName = true;
        booleanPreis = true;
        booleanMenge = true;
        booleanRabatt = true;
        booleanLagerbestand = true;
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
            if (booleanName && booleanPreis && booleanMenge && booleanRabatt && booleanLagerbestand && choiceBoxMenge.getValue() != null) {
                try {
                    ResultSet resultsetEinheit = statement.executeQuery("SELECT * FROM einheiten WHERE abkuerzung = '" + choiceBoxMenge.getSelectionModel().getSelectedItem()+ "'");
                    resultsetEinheit.next();
                    statement.execute("UPDATE artikel SET " + "name = '" + textfeldName.getText() + "', " + "preis = " + textfeldPreis.getText() + ", " + "menge = " + textfeldMenge.getText() + ", " + "einheit_id = " + resultsetEinheit.getInt("einheitId") + ", " + "rabatt = " + textfeldRabatt.getText() + ", " + "lagerbestand = " + textfeldLagerbestand.getText() + " WHERE artikelId = " + textfeldArtikelnummer.getText());
                    resultsetEinheit.close();

                    if (pfadBildArtikel != ""){
                        AllgemeineMethoden.dateiKopieren(pfadBildArtikel, neuerPfadBildArtikel);
                        ResultSet resultSetDateityp = statement.executeQuery("SELECT * FROM artikel WHERE artikelId = " + artikelnummer);
                        resultSetDateityp.next();
                        int bildnummer = resultSetDateityp.getInt("bildnummer");
                        resultSetDateityp.close();
                        File neuesBildAlterPfad = new File(pfadBildArtikel);
                        File neuesBildAlterName = new File(neuerPfadBildArtikel + "/" + neuesBildAlterPfad.getName());
                        String dateitypNeu = "";
                        int index = neuesBildAlterPfad.getName().lastIndexOf(".");
                        if (index > 0) {
                            dateitypNeu = neuesBildAlterPfad.getName().substring(index + 1);
                        }
                        File neuesBildNeuerName = new File(String.valueOf(getClass().getResource("/Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText() + "/" + (bildnummer+1) + "." + dateitypNeu)));
                        String neuesBildNeuerNamePfad = "Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText() + "/" + (bildnummer+1) + "." + dateitypNeu;
                        if (neuesBildAlterName.renameTo(new File(neuesBildNeuerNamePfad))) {
                            statement.execute("UPDATE artikel SET dateityp = '" + dateitypNeu + "', bildnummer = " + (bildnummer + 1) + "WHERE artikelId = " + textfeldArtikelnummer.getText());
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
                if (choiceBoxMenge.getValue() == null){
                    choiceBoxMenge.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
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
                imageViewArtikel.setImage(image);
            }else{
                Image image = new Image(new FileInputStream("Bilder/Benutzer/Artikel/" + resultSetArtikel.getInt("artikelId") + "/" + resultSetArtikel.getInt("bildnummer") + "." + resultSetArtikel.getString("dateityp")));
                imageViewArtikel.setImage(image);
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
                imageViewArtikel.setImage(image);
            }else{
                Image image = new Image(new FileInputStream("Bilder/Benutzer/Artikel/" + resultSetArtikel.getInt("artikelId") + "/" + resultSetArtikel.getInt("bildnummer") + "." + resultSetArtikel.getString("dateityp")));
                imageViewArtikel.setImage(image);
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
