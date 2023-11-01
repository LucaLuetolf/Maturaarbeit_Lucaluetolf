package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiErstanmeldungDatenErfassen implements Initializable {

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
    private Label labelBeschreibung;
    @FXML
    private Button buttonZurueck;
    @FXML
    private Button buttonWeiter;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private String s1 = "Herzlich Willkommen, nachfolgend wird die App erklärt und die wichtigen Inhalte werden erfasst";
    private String s2 = "Login";
    private String s3 = "Startseite";
    private String s4 = "Artikel";
    private String s5 = "Artikel Erfassen";
    private String s6 = "mehr Infos";
    private String s7 = "Kunden";
    private String s8 = "Kunde Erfassen";
    private String s9 = "Kunde bearbeiten";
    private String s10 = "Rechnung erstellen, Kunde";
    private String s11 = "Rechnung erstellen, Artikel";
    private String s12 = "Pdf Dokument";
    private String s13 = "mehr Infos";
    private String s14 = "mehr Infos";


    @FXML
    protected void zurueck(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId()) - 1));
        switch (Integer.parseInt(buttonWeiter.getId())) {
            case 1:
                labelBeschreibung.setText(s1);
            case 2:
                labelBeschreibung.setText(s2);
            case 3:
                labelBeschreibung.setText(s3);
            case 4:
                labelBeschreibung.setText(s4);
            case 5:
                labelBeschreibung.setText(s5);
            case 6:
                labelBeschreibung.setText(s6);
        }
    }

    @FXML
    protected void weiter(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())+1));

        switch (Integer.parseInt(buttonWeiter.getId())){
            case 1:
                labelBeschreibung.setText(s1);
            case 2:
                labelBeschreibung.setText(s2);
            case 3:
                labelBeschreibung.setText(s3);
            case 4:
                labelBeschreibung.setText(s4);
            case 5:
                labelBeschreibung.setText(s5);
            case 6:
                labelBeschreibung.setText(s6);
        }
    }

    // Unternehmensdaten Erfassen





    //Artikel Erfassen

    @FXML
    private ImageView imageView;
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

    private boolean booleanArtikelnummer = false;
    private boolean booleanName = false;
    private boolean booleanPreis = false;
    private boolean booleanMenge = false;
    private boolean booleanRabatt = false;
    private boolean booleanLagerbestand = false;

    private String filePath = "";
    private String newPath = "src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\Übergang\\";

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
    protected void bildAendern() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilddateien", "*.png;*.jpeg;*.jpg;*.gif;*.svg");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            Image image = new Image(filePath);
            imageView.setImage(image);
        }

    }

    @FXML
    protected void textfieldArtikelnummerKey() {
        textfeldArtikelnummer.setText(textfeldArtikelnummer.getText().replaceAll("[^0-9]", ""));
        textfeldArtikelnummer.positionCaret(textfeldArtikelnummer.getLength());
        booleanArtikelnummer = tester("^[1-9]\\d*$", textfeldArtikelnummer);
    }

    @FXML
    protected void textfieldNameKey() {
        textfeldName.setText(textfeldName.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ0-9]", ""));
        textfeldName.positionCaret(textfeldName.getLength());
        booleanName = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldName);
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
        if (100 < Integer.parseInt(textfeldRabatt.getText())) {
            textfeldRabatt.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Fehlermeldung");
            alert.setContentText("Der gewünschte Rabatt beträgt mehr als 100%");
            alert.showAndWait();

        }
        textfeldRabatt.positionCaret(textfeldRabatt.getLength());

    }

    @FXML
    protected void textfieldLagerbestandKey() {
        textfeldLagerbestand.setText(textfeldLagerbestand.getText().replaceAll("[^0-9]", ""));
        textfeldLagerbestand.positionCaret(textfeldLagerbestand.getLength());
        booleanLagerbestand = tester("^[0-9]\\d*$", textfeldLagerbestand);
    }

    @FXML
    protected void artikelErfassen(ActionEvent event) {
        if (booleanArtikelnummer && booleanName && booleanPreis && booleanMenge && booleanRabatt && booleanLagerbestand) {
            try {
                ResultSet resultsetEinheit = statement.executeQuery("SELECT * FROM einheiten WHERE abkuerzung = '" + choiceBoxMenge.getSelectionModel().getSelectedItem()+ "'");
                resultsetEinheit.next();
                statement.execute("INSERT INTO artikel (artikelId, name, preis, menge, einheit_id, rabatt, lagerbestand) VALUES (" + textfeldArtikelnummer.getText() + "," + "'" + textfeldName.getText() + "'" + "," + textfeldPreis.getText() + "," + textfeldMenge.getText() + "," +  resultsetEinheit.getInt("einheitId") + "," + textfeldRabatt.getText() + "," + textfeldLagerbestand.getText() + ")");
                resultsetEinheit.close();

                if (filePath != ""){
                    AllgemeineMethoden.dateiKopieren(filePath, newPath);
                    File altesBild = new File(filePath);
                    File neuesBild = new File(newPath + altesBild.getName());
                    String dateityp = "";
                    int index = altesBild.getName().lastIndexOf(".");
                    if (index > 0) {
                        dateityp = altesBild.getName().substring(index + 1);
                    }
                    File neuerName = new File(newPath + "\\1." + dateityp);
                    System.out.println(newPath);
                    neuesBild.renameTo(neuerName);
                    File ordner1 = new File("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\test");
                    File ordner2 = new File("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\" + textfeldArtikelnummer.getText());
                    ordner1.renameTo(ordner2);
                    AllgemeineMethoden.ordnerErstellen("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\Übergang");
                    statement.execute("UPDATE artikel SET dateityp = '" + dateityp + "', bildnummer = 1 WHERE artikelId = " + textfeldArtikelnummer.getText());
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

   /* @Override
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
            image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Artikel\\Artikel.png"));
            imageView.setImage(image);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }*/


    //Kunde Erfassen

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
        textfeldNachname.setText(textfeldNachname.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ]", ""));
        textfeldNachname.positionCaret(textfeldNachname.getLength());
        booleanNachname = tester("^[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+(\\s[A-ZÉÀÈÖÄÜ][a-zéàèöäü]+)?$", textfeldNachname);
    }

    @FXML
    protected void textfieldVornameKey() {
        textfeldVorname.setText(textfeldVorname.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ]", ""));
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
        booleanNatelnummer = tester("^[1-9]\\d*$", textfeldNatelnummer);
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
