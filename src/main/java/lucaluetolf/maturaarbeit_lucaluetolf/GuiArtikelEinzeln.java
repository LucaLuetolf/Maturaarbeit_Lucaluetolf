package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
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
    private ChoiceBox<String> choiceboxMenge;
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


    private boolean booleanArtikelnummer = false;
    private boolean booleanName = false;
    private boolean booleanPreis = false;
    private boolean booleanMenge = false;
    private boolean booleanRabatt = false;
    private boolean booleanLagerbestand = false;
    private Stage stage;
    private Scene scene;
    private Parent root;

    int artikelnummer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unternehmen");
            resultSet.next();
            artikelnummer = resultSet.getInt("bearbeiten");
            resultSet.close();
            //TODO Wieso nicht delete-Statement?
            statement.execute("UPDATE unternehmen SET bearbeiten = null");
            option1();
            buttonArtikelBearbeiten.setId("1");
            buttonZurueck.setId("1");
            GuiArtikel.bildAnzeigen(imageviewArtikel, artikelnummer);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }


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
            option1();
            //TODO Update statement Artikel
            buttonArtikelBearbeiten.setId("1");
            buttonArtikelBearbeiten.setText("bearbeiten");
            buttonZurueck.setId("1");
            buttonZurueck.setText("zurück");
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

        /*File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            String ort = "src/main/resources/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/Benutzer/Artikel/";
            AllgemeineMethoden.dateiKopieren(filePath, ort);
            String name = selectedFile.getName();
            int indexPunkt = name.lastIndexOf(".");
            String dateityp = name.substring(indexPunkt + 1);
            File file = new File(ort + name);
            File folder = new File(ort);
            String filePath1 = "";

            if (folder.exists() && folder.isDirectory()) {
                // Erstellen Sie einen FilenameFilter, um Dateien mit passender Nummer zu filtern
                FilenameFilter filter1 = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        // Extrahieren Sie die Nummer aus dem Dateinamen (ohne Erweiterung)
                        String[] parts = name.split("\\.");
                        if (parts.length == 2) {
                            try {
                                int fileNumber = Integer.parseInt(parts[0]);
                                return fileNumber == artikelnummer;
                            } catch (NumberFormatException e) {
                                // Wenn die Nummer nicht geparst werden kann, ignorieren Sie die Datei
                                return false;
                            }
                        }
                        return false;
                    }
                };

                // Durchsuchen Sie den Ordner nach passenden Dateien
                File[] matchingFiles = folder.listFiles(filter1);

                if (matchingFiles != null && matchingFiles.length > 0) {
                    // Der Pfad zur gefundenen Datei
                    filePath1 = matchingFiles[0].getAbsolutePath();
                    System.out.println("Gefundene Datei: " + filePath1);
                } else {
                    System.out.println("Keine Datei mit der Nummer " + ort + " gefunden.");
                }
            } else {
                System.out.println("Der angegebene Ordner existiert nicht oder ist kein Verzeichnis.");
            }
            selectedFile.renameTo(new File(ort + artikelnummer + "." + dateityp));
            File fileToDelete = new File(filePath1);
            file.renameTo(new File(ort + "fileToDelete." + dateityp));

            imageviewArtikel.setImage(null);

            fileToDelete.deleteOnExit();
            Image image = new Image(filePath);
            imageviewArtikel.setImage(image);
            file.renameTo(new File(ort + artikelnummer + "." + dateityp));
        }*/
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String pfadAusgewaehltesBild = selectedFile.getAbsolutePath();
            String pfadOrdner = "src/main/resources/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/Benutzer/Artikel/";
            String dateinameAusgewaehltesBild = selectedFile.getName();
            int indexPunkt = dateinameAusgewaehltesBild.lastIndexOf(".");
            String dateitypAusgewaehltesBild = dateinameAusgewaehltesBild.substring(indexPunkt + 1);
            String pfadAltesBild = "";
            String dateitypAltesBild = "";
            File ordner = new File(pfadOrdner);

            if(ordner.exists() && ordner.isDirectory()){
                FilenameFilter filenameFilter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        // Extrahieren Sie die Nummer aus dem Dateinamen (ohne Erweiterung)
                        String[] parts = name.split("\\.");
                        if (parts.length == 2) {
                            try {
                                int fileNumber = Integer.parseInt(parts[0]);
                                return fileNumber == artikelnummer;
                            } catch (NumberFormatException e) {
                                // Wenn die Nummer nicht geparst werden kann, ignorieren Sie die Datei
                                return false;
                            }
                        }
                        return false;
                    }
                };

                // Durchsuchen Sie den Ordner nach passenden Dateien
                File[] matchingFiles = ordner.listFiles(filenameFilter);

                if (matchingFiles != null && matchingFiles.length > 0) {
                    // Der Pfad zur gefundenen Datei
                    pfadAltesBild = matchingFiles[0].getAbsolutePath();
                    System.out.println("Gefundene Datei: " + pfadAltesBild);
                } else {
                    System.out.println("Keine Datei mit der Nummer " + artikelnummer + " gefunden.");
                }
            } else {
                System.out.println("Der angegebene Ordner existiert nicht oder ist kein Verzeichnis.");
            }
            File altesBild = new File(pfadAltesBild);
            altesBild.renameTo(new File(pfadOrdner + "fileToDelete"));
            File fileToDelete = new File(pfadOrdner + "fileToDelete");
            //fileToDelete.delete();
            selectedFile.renameTo(new File(pfadOrdner + artikelnummer + "." + dateitypAusgewaehltesBild));
            Image image = new Image(selectedFile.getAbsolutePath());
            imageviewArtikel.setImage(image);
            /*if (altesBild.exists()){
                altesBild.delete();
            }*/
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
    protected void textfieldArtikelnummerKey() {
        textfeldArtikelnummer.setText(textfeldArtikelnummer.getText().replaceAll("[^0-9]", ""));
        textfeldArtikelnummer.positionCaret(textfeldArtikelnummer.getLength());
        booleanArtikelnummer = tester("[0-9]", textfeldArtikelnummer);
    }

    @FXML
    protected void textfieldNameKey() {
        booleanName = tester("", textfeldName);
    }

    //TODO Regex Preis anpassen, momentan keine Stelle vor dem Punkt. Unendlich viele Punkte möglich
    @FXML
    protected void textfieldPreisKey() {
        //textfeldPreis.setText(textfeldPreis.getText().replaceAll("[^0-9]+\\.[^0-9]", ""));
        textfeldPreis.setText(textfeldPreis.getText().replaceAll("^(?!.*\\..*\\.)\\d+(\\.\\d{1,2})?$", ""));
        textfeldPreis.positionCaret(textfeldPreis.getLength());
        booleanPreis = tester("^[0-9]+\\.[0-9]", textfeldPreis);

    }

    @FXML
    protected void textfieldMengeKey() {
        //TODO replaceAll
        booleanMenge = tester("^[0-9]+\\.[0-9]", textfeldMenge);
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
            tester("[0-9]", textfeldRabatt);
        }
        textfeldRabatt.positionCaret(textfeldRabatt.getLength());

    }

    @FXML
    protected void textfieldLagerbestandKey() {
        textfeldLagerbestand.setText(textfeldLagerbestand.getText().replaceAll("[^0-9]", ""));
        textfeldLagerbestand.positionCaret(textfeldLagerbestand.getLength());
        booleanLagerbestand = tester("[0-9]", textfeldLagerbestand);
    }

    private void option1() {
        try {
            ResultSet resultSetArtikel = statement.executeQuery("SELECT * FROM ARTIKEL WHERE artikelId = " + artikelnummer);
            resultSetArtikel.next();
            labelArtikelnummer.setText(String.valueOf(resultSetArtikel.getInt("artikelId")));
            labelName.setText(resultSetArtikel.getString("name"));
            labelPreis.setText(String.valueOf(resultSetArtikel.getDouble("preis")));
            labelMenge.setText(String.valueOf(resultSetArtikel.getDouble("menge")));
            labelRabatt.setText(String.valueOf(resultSetArtikel.getDouble("rabatt")));
            labelLagerbestand.setText(String.valueOf(resultSetArtikel.getInt("lagerbestand")));
            resultSetArtikel.close();
            textfeldArtikelnummer.setVisible(false);
            textfeldName.setVisible(false);
            textfeldPreis.setVisible(false);
            textfeldMenge.setVisible(false);
            choiceboxMenge.setVisible(false);
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
            ResultSet resultSetArtikel = statement.executeQuery("SELECT * FROM ARTIKEL WHERE artikelId = " + artikelnummer);
            resultSetArtikel.next();
            textfeldArtikelnummer.setText(String.valueOf(resultSetArtikel.getInt("artikelId")));
            textfeldName.setText(resultSetArtikel.getString("name"));
            textfeldPreis.setText(String.valueOf(resultSetArtikel.getDouble("preis")));
            textfeldMenge.setText(String.valueOf(resultSetArtikel.getDouble("menge")));
            textfeldRabatt.setText(String.valueOf(resultSetArtikel.getDouble("rabatt")));
            textfeldLagerbestand.setText(String.valueOf(resultSetArtikel.getInt("lagerbestand")));
            resultSetArtikel.close();
            textfeldArtikelnummer.setVisible(true);
            textfeldArtikelnummer.setEditable(false);
            textfeldName.setVisible(true);
            textfeldPreis.setVisible(true);
            textfeldMenge.setVisible(true);
            choiceboxMenge.setVisible(true);
            textfeldRabatt.setVisible(true);
            textfeldLagerbestand.setVisible(true);
            buttonBildAendern.setVisible(true);
            labelArtikelnummer.setVisible(false);
            labelName.setVisible(false);
            labelPreis.setVisible(false);
            labelMenge.setVisible(false);
            labelRabatt.setVisible(false);
            labelLagerbestand.setVisible(false);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

}
