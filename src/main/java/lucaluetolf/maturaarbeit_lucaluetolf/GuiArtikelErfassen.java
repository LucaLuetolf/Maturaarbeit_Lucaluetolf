package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class GuiArtikelErfassen extends GuiTaskleiste implements Initializable {

    @FXML
    protected void artikelErfassen(ActionEvent event) {
        if (booleanArtikelnummer && booleanName && booleanPreis && booleanMenge && booleanRabatt && booleanLagerbestand && choiceBoxMenge.getValue() != null) {
            try {
                ResultSet resultsetEinheit = statement.executeQuery("SELECT * FROM einheiten WHERE abkuerzung = '" + choiceBoxMenge.getSelectionModel().getSelectedItem()+ "'");
                resultsetEinheit.next();
                statement.execute("INSERT INTO artikel (artikelId, name, preis, menge, einheit_id, rabatt, lagerbestand) VALUES (" + textfeldArtikelnummer.getText() + "," + "'" + textfeldName.getText() + "'" + "," + textfeldPreis.getText() + "," + textfeldMenge.getText() + "," +  resultsetEinheit.getInt("einheitId") + "," + textfeldRabatt.getText() + "," + textfeldLagerbestand.getText() + ")");
                resultsetEinheit.close();
                LocalDate datum = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ResultSet resultsetVerkaufteStueck = statement.executeQuery("SELECT COUNT(artikel_id) FROM verkaufteStueck WHERE datum = '" + formatter.format(datum) + "'");
                resultsetVerkaufteStueck.next();
                int existiert = resultsetVerkaufteStueck.getInt(1);
                if (existiert != 0){
                    statement.execute("INSERT INTO verkaufteStueck (artikel_id, anzahl, datum) VALUES (" + textfeldArtikelnummer.getText() + ",0,'" + formatter.format(datum) + "')");
                }

                if (pfadBildArtikel != ""){
                    AllgemeineMethoden.dateiKopieren(pfadBildArtikel, neuerPfadBildArtikel);
                    File altesBild = new File(pfadBildArtikel);
                    File neuesBild = new File(neuerPfadBildArtikel + altesBild.getName());
                    String dateityp = "";
                    int index = altesBild.getName().lastIndexOf(".");
                    if (index > 0) {
                        dateityp = altesBild.getName().substring(index + 1);
                    }
                    File neuerName = new File(neuerPfadBildArtikel + "/1." + dateityp);
                    if(neuesBild.renameTo(neuerName)){
                        statement.execute("UPDATE artikel SET dateityp = '" + dateityp + "', bildnummer = 1 WHERE artikelId = " + textfeldArtikelnummer.getText());
                    }
                    File ordner1 = new File("Bilder/Benutzer/Artikel/Übergang");
                    File ordner2 = new File("Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText());
                    ordner1.renameTo(ordner2);
                    AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel/Übergang");
                }
                else{
                    AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText());
                }
                root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            if (booleanArtikelnummer == false) {
                textfeldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                if(textfeldArtikelnummer.getText().matches("^[1-9]\\d*$") && textfeldArtikelnummer.getLength() > 0 && textfeldArtikelnummer.getLength() <= 8){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Fehlermeldung");
                    alert.setHeaderText("Diese Artikelnummer ist bereits vergeben.");
                    alert.showAndWait();
                }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        Image image = null;
        try {
            image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Artikel/Artikel.png")));
            imageViewArtikel.setImage(image);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }
}
