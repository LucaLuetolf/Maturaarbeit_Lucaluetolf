package lucaluetolf.maturaarbeit_lucaluetolf;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiErstanmeldungDatenErfassen extends GuiTaskleiste implements Initializable {

    @FXML
    private Label labelBeschreibung;
    @FXML
    private Button buttonWeiter;

    @FXML
    private Pane paneUnternehmensdatenErfassen;
    @FXML
    private Pane paneArtikelErfassen;
    @FXML
    private Pane paneKundeErfassen;
    @FXML
    private Pane paneLagerbestand;

    //Strings
    private String s1 = "Der Benutzername muss am Anfang gross geschrieben sein. \nDas Passwort muss mindestens eine Zahl, einen Gross- und Kleinbuchstaben enthalten. Die Länge beträgt mindestens 8 Zeichen.";
    private String s2 = "Die Artikelnummer darf nicht mit 0 starten. Der Name muss am Anfang gross geschrieben werden. Bei der Menge ist die Menge pro Einheit gemeint. Hinweis: Die Artikelnummer kann nachträglich nicht geändert werden";
    private String s3 = "Die Kundennummer darf nicht mit 0 starten";

    // Unternehmensdaten Erfassen
    @FXML
    protected void imageViewLagerbestandEntered(){
        paneLagerbestand.setVisible(true);
    }
    @FXML
    protected void imageViewLagerbestandExited(){
        paneLagerbestand.setVisible(false);
    }


    private boolean unternehmensdatenErfassen(){
        boolean booleanUnternehmensdatenErfassen = false;
        if (booleanUnternehmen && booleanBenutzername && booleanPasswort && booleanLagerbestandAnzeige && booleanBank && booleanIban) {
            try {
                statement.execute("INSERT INTO unternehmen (unternehmensname, rechnungsnummer, benutzername, passwort, lagerbestandOrange, bank, iban) VALUES ('" + textfeldUnternehmen.getText() + "', 1, '" + textfeldBenutzername.getText() + "','" + textfeldPasswort.getText() + "'," + textfeldLagerbestandAnzeige.getText() + ",'" + textfeldBank.getText() + "','" + textfeldIban.getText() + "')");

                if (pfadBildLogo != ""){
                    AllgemeineMethoden.dateiKopieren(pfadBildLogo, neuerPfadBildLogo);
                    File altesBild = new File(pfadBildLogo);
                    File neuesBild = new File(neuerPfadBildLogo + altesBild.getName());
                    String dateityp = "";
                    int index = altesBild.getName().lastIndexOf(".");
                    if (index > 0) {
                        dateityp = altesBild.getName().substring(index + 1);
                    }
                    File neuerName = new File(neuerPfadBildLogo + "1." + dateityp);
                    neuesBild.renameTo(neuerName);
                    statement.execute("UPDATE unternehmen SET dateityp = '" + dateityp + "', bildnummer = 1");
                }

            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            booleanUnternehmensdatenErfassen = true;
        } else {
            if (booleanUnternehmen == false) {
                textfeldUnternehmen.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanBenutzername == false) {
                textfeldBenutzername.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanPasswort == false) {
                textfeldPasswort.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanLagerbestandAnzeige == false) {
                textfeldLagerbestandAnzeige.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanBank == false) {
                textfeldBank.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanIban == false) {
                textfeldIban.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
        }
        return booleanUnternehmensdatenErfassen;
    }


    //Artikel Erfassen
    private boolean artikelErfassen(int artikelnummer) {
        boolean booleanArtikelErfassen = false;
        if (booleanArtikelnummer && booleanName && booleanPreis && booleanMenge && booleanRabatt && booleanLagerbestand) {
            try {
                ResultSet resultsetEinheit = statement.executeQuery("SELECT * FROM einheiten WHERE abkuerzung = '" + choiceBoxMenge.getSelectionModel().getSelectedItem()+ "'");
                resultsetEinheit.next();
                statement.execute("INSERT INTO artikel (artikelId, name, preis, menge, einheit_id, rabatt, lagerbestand) VALUES (" + textfeldArtikelnummer.getText() + "," + "'" + textfeldName.getText() + "'" + "," + textfeldPreis.getText() + "," + textfeldMenge.getText() + "," +  resultsetEinheit.getInt("einheitId") + "," + textfeldRabatt.getText() + "," + textfeldLagerbestand.getText() + ")");
                resultsetEinheit.close();

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
                    neuesBild.renameTo(neuerName);
                    File ordner1 = new File("Bilder/Benutzer/Artikel/Übergang");
                    File ordner2 = new File("Bilder/Benutzer/Artikel/" + artikelnummer);
                    ordner1.renameTo(ordner2);
                    AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel/Übergang");
                    statement.execute("UPDATE artikel SET dateityp = '" + dateityp + "', bildnummer = 1 WHERE artikelId = " + artikelnummer);
                }
                else{
                    AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText());
                }
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            booleanArtikelErfassen = true;
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
        return booleanArtikelErfassen;
    }

    //Kunde Erfassen


    private boolean kundeErfassen() {
        boolean booleanKundeErfassen = false;
        if (booleanKundennummer && booleanNachname && booleanVorname && booleanAdresse && booleanPostleitzahl && booleanOrt && booleanEmail && booleanNatelnummer) {
            try {
                statement.execute("INSERT INTO kunden (kundenId, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer) VALUES (" + textfeldKundennummer.getText() + ",'" + textfeldNachname.getText() + "','" + textfeldVorname.getText() + "','" + textfeldAdresse.getText() + "'," + textfeldPostleitzahl.getText() + ",'" + textfeldOrt.getText() + "','" + textfeldEmail.getText() + "','" + textfeldNatelnummer.getText() + "')");
                AllgemeineMethoden.ordnerErstellen("Kundendateien/" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText());
                AllgemeineMethoden.ordnerErstellen("Kundendateien/" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText() + "/Quittungen");
                AllgemeineMethoden.ordnerErstellen("Kundendateien/" + textfeldKundennummer.getText() + ", " + textfeldNachname.getText() + " " + textfeldVorname.getText() + "/Rechnungen");
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            booleanKundeErfassen = true;
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
        return booleanKundeErfassen;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonWeiter.setId("1");
        labelBeschreibung.setText(s1);
        paneArtikelErfassen.setVisible(false);
        paneKundeErfassen.setVisible(false);
        paneLagerbestand.setVisible(false);
    }
    @FXML
    protected void weiter(){
        int artikelnummer = 0;
        if (Integer.parseInt(buttonWeiter.getId()) == 2 && textfeldArtikelnummer.getText() != "" && booleanArtikelnummer){
            artikelnummer = Integer.parseInt(textfeldArtikelnummer.getText());
        }
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())+1));
        switch (Integer.parseInt(buttonWeiter.getId())){
            case 2:
                boolean booleanTest = unternehmensdatenErfassen();
                if (booleanTest == false){
                    buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())-1));
                    break;
                }
                paneUnternehmensdatenErfassen.setVisible(false);
                paneArtikelErfassen.setVisible(true);
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
                    labelBeschreibung.setText(s2);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                break;
            case 3:
                boolean booleanTestArtikel = artikelErfassen(artikelnummer);
                if (booleanTestArtikel == false){
                    buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())-1));
                    break;
                }
                paneArtikelErfassen.setVisible(false);
                paneKundeErfassen.setVisible(true);
                labelBeschreibung.setText(s3);
                break;
            case 4:
                boolean booleanTestKunde = kundeErfassen();
                if (booleanTestKunde == false){
                    buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())-1));
                    break;
                }
                try {
                    root = FXMLLoader.load(getClass().getResource("login.fxml"));
                } catch (IOException e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                stage = (Stage) buttonWeiter.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest(event1 -> {
                    stage.close();
                });
                break;
        }
    }

    @FXML
    protected void ohneKundeFortfahren(ActionEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event1 -> {
            stage.close();
        });
    }
}
