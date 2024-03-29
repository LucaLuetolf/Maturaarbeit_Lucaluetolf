package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiErstanmeldung extends GuiTaskleiste implements Initializable {

    @FXML
    private ImageView imageviewBeschreibung;
    @FXML
    private Label labelBeschreibung;
    @FXML
    private Button buttonZurueck;
    @FXML
    private Button buttonWeiter;


    private String s1 = "Herzlich Willkommen!\nNachfolgend wird Ihnen die ERP-Software erklärt. Auf der Startseite sind alle Funktionen der App zum auswählen bereit.";
    private String s2 = "Beim Starten der App wird das Loginfeld angezeigt. Hier muss der festgelegte Benutzername und das Passwort eingegeben werden. Bei richtiger Eingabe gelangen sie zur Startseite. \nACHTUNG: Das Passwort kann nicht zurückgesetzt werden.";
    private String s3 = "Auf diesem Bild ist die Auflistung der Artikel zu sehen. Via Button \"Artikel hinzufügen\" können neue Artikel erfasst werden. Unter \"mehr Infos\" kann der Artikel bearbeitet werden.";
    private String s4 = "Beim Erfassen der Artikelangaben werden falsch eingegebene Zeichen direkt herausgefiltert. Ist eine Eingabe ungültig, wird das Textfeld rot angezeigt.";
    private String s5 = "Via Button \"mehr Infos\" kann ein Artikel angepasst werden. Das einzige, was nicht geändert werden kann ist die Artikelnummer.";
    private String s6 = "Auf diesem Bild sind sämtliche Kunden aufgelistet. Neue Kunden können via Button \"Kunde hinzufügen\" erfasst werden. Möchten sie einen Kunden bearbeiten, klicken sie in der Tabelle auf die gewünschte Zeile.";
    private String s7 = "Wie beim Artikel auch, werden hier falsche Eingaben direkt herausgefiltert.";
    private String s8 = "Das Fenster \"Kunde bearbeiten\" sieht gleich aus, wie das Fenster \"Kunde erfassen\". Die Angaben in den Textfeldern können beliebig bearbeitet werden.";
    private String s9 = "Um eine Rechnung zu erstellen, wird erstmals der gewünschte Kunde (via klick auf die entsprechende Zeile) ausgewählt.";
    private String s10 = "Im nächsten Schritt kann die Anzahl der Artikel via Button \"+\", \"-\" oder anhand einer Eingabe im Textfeld festgelegt werden.";
    private String s11 = "Anschliessend wird eine Rechnung in Form von einem Pdf generiert. Das Dokument wird automatisch beim Speicherort der App im Ordner \"Kundendateien\" gespeichert.";
    private String s12 = "Die Seiten \"Verkauf\" und \"Rechnung\" sind ähnlich aufgebaut. Der Unterschied liegt darin, dass ohne Kunde fortgefahren werden kann. Beim Abschliessen des Verkaufes kann zudem das Retourgeld berechnet werden. ";
    private String s13 = "Falls eine bestehende Rechnung oder Quittung bearbeitet werden soll, kann sie via Button \"Pdf bearbeiten\" geändert werden. Hierzu wird die Rechnungs- oder Quittungsnummer benötigt.";
    private String s14 = "Die Funktion \"Analyse\" ermöglicht es, die Verkaufszahlen von einem oder mehreren Artikel über den gewünschten Zeitraum in einem Diagramm anzuzeigen.";
    private String s15 = "In den Einstellungen können die Geschäftsdaten wie auch die Einheiten angepasst werden. Beim Button \"Tutorial anzeigen\" kann diese Wegleitung gestartet werden. Ausserdem ist es möglich die App via Button \"App zurücksetzen\" auf die Starteinstellungen zurückzusetzen.";
    private String s16 = "Auf dieser Seite besteht die Möglichkeit Geschäftsdaten anzupassen.";
    private String s17 = "Via Button \"Einheiten\" können die Verkaufseinheiten erfasst, aktiviert oder deaktiviert werden.";

    @FXML
    protected void zurueck(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId()) - 1));
        if (Integer.parseInt(buttonWeiter.getId()) == 1){
            buttonZurueck.setVisible(false);
        }
        switch (Integer.parseInt(buttonWeiter.getId())){
            case 1:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/startseite.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s1);
                break;
            case 2:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/login.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s2);
                break;
            case 3:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/artikelAnzeigen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s3);
                break;
            case 4:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/artikelErfassen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s4);
                break;
            case 5:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/artikelBearbeiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s5);
                break;
            case 6:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/kundenAnzeigen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
                break;
            case 7:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/kundeErfassen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s7);
                break;
            case 8:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/kundeBearbeiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s8);
                break;
            case 9:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/neueRechnungKundeAuswählen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s9);
                break;
            case 10:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/neueRechnungArtikelAuswählen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s10);
                break;
            case 11:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/pdfDokument.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s11);
                break;
            case 12:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/verkauf.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s12);
                break;
            case 13:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/pdfBearbeiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s13);
                break;
            case 14:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/analyse.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s14);
                break;
            case 15:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/einstellungen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s15);
                break;
            case 16:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/konto.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s16);
                break;
            case 17:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/einheiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s17);
                break;
        }
    }

    @FXML
    protected void weiter(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())+1));
        switch (Integer.parseInt(buttonWeiter.getId())){
            case 2:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/login.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s2);
                break;
            case 3:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/artikelAnzeigen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s3);
                break;
            case 4:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/artikelErfassen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s4);
                break;
            case 5:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/artikelBearbeiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s5);
                break;
            case 6:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/kundenAnzeigen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
                break;
            case 7:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/kundeErfassen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s7);
                break;
            case 8:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/kundeBearbeiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s8);
                break;
            case 9:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/neueRechnungKundeAuswählen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s9);
                break;
            case 10:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/neueRechnungArtikelAuswählen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s10);
                break;
            case 11:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/pdfDokument.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s11);
                break;
            case 12:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/verkauf.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s12);
                break;
            case 13:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/pdfBearbeiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s13);
                break;
            case 14:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/analyse.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s14);
                break;
            case 15:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/einstellungen.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s15);
                break;
            case 16:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/konto.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s16);
                break;
            case 17:
                try {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/einheiten.png")));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s17);
                break;
            case 18:
                try {
                    ResultSet resultsetUnternehmen = statement.executeQuery("SELECT COUNT(unternehmensname) FROM unternehmen");
                    resultsetUnternehmen.next();
                    if (resultsetUnternehmen.getInt(1) == 0){
                        root = FXMLLoader.load(getClass().getResource("erstanmeldungDatenErfassen.fxml"));
                    }else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Tutorial");
                        alert.setHeaderText("Das Tutorial ist beendet");
                        alert.showAndWait();
                        root = FXMLLoader.load(getClass().getResource("einstellungen.fxml"));
                    }

                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                stage = (Stage) buttonWeiter.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonWeiter.setId("1");
        buttonZurueck.setVisible(false);
        try {
            Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Erstanmeldung/startseite.png")));
            imageviewBeschreibung.setImage(image);
            labelBeschreibung.setText(s1);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

}

