package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    private ChoiceBox<String> comboboxMenge;

    private boolean booleanArtikelnummer = false;
    private boolean booleanName = false;
    private boolean booleanPreis = false;
    private boolean booleanMenge = false;
    private boolean booleanRabatt = false;
    private boolean booleanLagerbestand = false;

    private boolean tester(String regex, TextField textField) {
        boolean boolean1 = textField.getText().matches(regex);
        if (boolean1) {
            textField.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
        } else {
            textField.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
        return boolean1;
    }


    // Quelle:
    @FXML
    protected void bildAendern() {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("PNG-Dateien", "png");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image(filePath);
            imageView.setImage(image);
        }

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
        String s = "^[0-9]+\\.[0-9]";
        booleanPreis = tester("^[0-9]+\\.[0-9]", textfeldPreis);

    }

    @FXML
    protected void textfieldMengeKey() {
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

    @FXML
    protected void artikelErfassen(ActionEvent event) {

        if (booleanArtikelnummer && booleanName && booleanPreis && booleanMenge && booleanRabatt && booleanLagerbestand) {

            try {
                statement.execute("INSERT INTO artikel (artikelId, name, preis, menge, rabatt, lagerbestand) VALUES (" + textfeldArtikelnummer.getText() + "," + "'" + textfeldName.getText() + "'" + "," + textfeldPreis.getText() + "," + textfeldMenge.getText() + "," + textfeldRabatt.getText() + "," + textfeldLagerbestand.getText() + ")");
                root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
                System.out.println(comboboxMenge.getSelectionModel().getSelectedItem());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Look, an Information Dialog");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.out.println("Fehler");
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            if (booleanArtikelnummer) {
                textfeldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanName) {
                textfeldName.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanPreis) {
                textfeldPreis.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanMenge) {
                textfeldMenge.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanRabatt) {
                textfeldRabatt.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanLagerbestand) {
                textfeldLagerbestand.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> werte = FXCollections.observableArrayList("Stk", "g", "kg", "ml", "l");
        comboboxMenge.setItems(werte);

    }
}
