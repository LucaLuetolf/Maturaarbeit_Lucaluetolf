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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiErstanmeldung extends GuiTaskleiste implements Initializable {

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
    private ImageView imageviewBeschreibung;
    @FXML
    private Label labelBeschreibung;
    @FXML
    private Button buttonZurueck;
    @FXML
    private Button buttonWeiter;


    private String s1 = "Herzlich Willkommen, nachfolgend wird die ERP-Software erklärt. Auf dem Bild ist die Startseite zu sehen. Hier sind alle Funktionen der App zum auswählen bereit.";
    private String s2 = "Beim starten der App wird das Loginfeld angezeigt. Hier muss der festgelegte Benutzername und das Passwort eingegeben werden. Bei richtiger Eingabe gelangen sie zur Startseite";
    private String s3 = "Startseite";
    private String s4 = "Auf dem Bild ist die Auflistung der Artikel zu sehen. Via Button Artikel erfassen können neue Artikel erfasst und bei mehr Infos bearbeitet, werden. ";
    private String s5 = "Beim Erfassen der Daten werden falsch eingegeben Zeichen direkt herausgefiltert. Ist eine Eingabe ungültig, wird das Textfeld rot angezeigt.";
    private String s6 = "Auf dieser Seite kann ein Artikel angepasst werden. Das einzige, was nicht geändert werden kann ist die Artikelnummer.";
    private String s7 = "Auf diesem Bild ist die Seite Kunden zu sehen. Neue Kunden können via Kunde erfassen erfasst werden. Möchten sie einen Kunden bearbeiten, klicken sie in der Tabelle auf die gewünschte Zeile";
    private String s8 = "Wie bei den Artikel auch, werden hier die falschen Eingaben direkt herausgefiltert.";
    private String s9 = "Kunde bearbeiten";
    private String s10 = "Rechnung erstellen, Kunde";
    private String s11 = "Rechnung erstellen, Artikel";
    private String s12 = "Pdf Dokument";
    private String s13 = "mehr Infos";
    private String s14 = "mehr Infos";


    @FXML
    protected void zurueck(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId()) - 1));
        if (Integer.parseInt(buttonWeiter.getId()) == 1){
            buttonZurueck.setVisible(false);
        }
        switch (Integer.parseInt(buttonWeiter.getId())) {
            case 1:
                try {
                    buttonZurueck.setVisible(false);
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s1);
                break;
            case 2:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s2);
                break;
            case 3:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s3);
                break;
            case 4:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s4);
                break;
            case 5:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s5);
                break;
            case 6:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
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
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\artikelAnzeigen.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s2);
                break;
            case 3:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\artikelErfassen.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s3);
                break;
            case 4:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\artikelBearbeiten.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s4);
                break;
            case 5:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\kundenAnzeigen.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s5);
                break;
            case 6:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\kundeErfassen.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
                break;
            case 7:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\kundeBearbeiten.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
                break;
            case 8:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\neueRechnungKundeAuswählen.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
                break;
            case 9:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\neueRechnungArtikelAuswählen.png"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
                break;
            case 100:
                try {
                    root = FXMLLoader.load(getClass().getResource("erstanmeldungDatenErfassen.fxml"));
                } catch (IOException e) {
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
            Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\1.jpg"));
            imageviewBeschreibung.setImage(image);
            labelBeschreibung.setText(s1);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

}
