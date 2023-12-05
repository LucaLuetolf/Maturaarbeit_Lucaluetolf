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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiArtikelErfassen extends GuiTaskleiste implements Initializable {

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
    /*@FXML
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
    private boolean booleanLagerbestand = false;*/

    private String filePath = "";
    private String newPath = "Bilder/Benutzer/Artikel/Übergang/";




    @FXML
    protected void bildAendern() {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Bilddateien", "*.png;*.jpeg;*.jpg;*.gif;*.svg");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            Image image = new Image(filePath);
            imageView.setImage(image);
        }

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
                    File neuerName = new File(newPath + "/1." + dateityp);
                    neuesBild.renameTo(neuerName);
                    File ordner1 = new File("Bilder/Benutzer/Artikel/Übergang");
                    File ordner2 = new File("Bilder/Benutzer/Artikel/" + textfeldArtikelnummer.getText());
                    ordner1.renameTo(ordner2);
                    AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel/Übergang");
                    statement.execute("UPDATE artikel SET dateityp = '" + dateityp + "', bildnummer = 1 WHERE artikelId = " + textfeldArtikelnummer.getText());
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
            imageView.setImage(image);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }
}
