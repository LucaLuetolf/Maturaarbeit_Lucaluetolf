package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.stage.FileChooser.ExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GuiArtikelErfassen extends GuiLeiste{

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
    private String pfad;
    @FXML
    protected void bildAendern(){
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("PNG-Dateien", "png");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            pfad = filePath;
            Image image = new Image(filePath);
            imageView.setImage(image);
        }

    }

    @FXML
    protected void artikelErfassen(ActionEvent event) {

        int artikelnummer = Integer.parseInt(textfeldArtikelnummer.getText());
        String name = String.valueOf(textfeldName.getText());
        double preis = Double.parseDouble(textfeldPreis.getText());
        double menge = Double.parseDouble(textfeldMenge.getText());
        double rabatt = Double.parseDouble(textfeldRabatt.getText());
        int lagerbestand = Integer.parseInt(textfeldLagerbestand.getText());

        try {
            statement.execute("INSERT INTO artikel (artikelId, name, preis, menge, rabatt, lagerbestand) VALUES (" +artikelnummer + "," + "'" + textfeldName.getText() + "'" + "," + preis + "," + menge + "," + rabatt + "," + lagerbestand + ")");
            root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
        } catch (SQLException e) {
            e.getMessage();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
