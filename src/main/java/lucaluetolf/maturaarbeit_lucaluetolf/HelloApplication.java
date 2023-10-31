package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Pattern;

public class HelloApplication extends Application {
    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Class.forName("org.h2.Driver");
            statement.execute("DROP TABLE IF EXISTS bearbeiter");
            statement.execute("DROP TABLE IF EXISTS bestellung");
            //statement.execute("DROP TABLE IF EXISTS loeschen");
            //statement.execute("DROP TABLE IF EXISTS unternehmen");
            //statement.execute("DROP TABLE IF EXISTS kunden");
            //statement.execute("DROP TABLE IF EXISTS artikel");

            statement.execute("CREATE TABLE IF NOT EXISTS kunden (kundenId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS artikel (artikelId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(30), preis DOUBLE, menge DOUBLE, einheit_id INT, rabatt DOUBLE, lagerbestand INT, dateityp VARCHAR(30), bildnummer INT)" );
            statement.execute("CREATE TABLE IF NOT EXISTS mitarbeiter (mitarbeiterId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer INT, pin INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS bestellung (bestellungId INT NOT NULL, artikel_id INT, anzahl INT, rabatt DOUBLE)");
            statement.execute("CREATE TABLE IF NOT EXISTS bearbeiter (bestellung_id INT NOT NULL, mitarbeiter_id INT, kunden_id INT, dokumenttyp INT, datum DATE)");
            statement.execute("CREATE TABLE IF NOT EXISTS unternehmen (rechnungsnummer INT, benutzername VARCHAR(30), passwort VARCHAR(30), lagerbestandOrange INT, bearbeiten INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS einheiten (einheitId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, bezeichnung VARCHAR(30), abkuerzung VARCHAR(30), aktiv BOOLEAN)");
            statement.execute("CREATE TABLE IF NOT EXISTS loeschen (artikel_Id INT, bildnummer INT, dateityp VARCHAR(30))");
            /*statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (1, 'Stück', 'stk', true)");
            statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (2, 'Gramm', 'gr', true)");
            statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (3, 'Mililiter', 'ml', true)");
            statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (4, 'Flaschen', 'fl', true)");*/
            //statement.execute("INSERT INTO unternehmen (rechnungsnummer, benutzername, passwort, lagerbestandOrange) VALUES (1, 'Hans','Hans', 10)");
            //statement.execute("INSERT INTO artikel(artikelId, name, menge, preis, rabatt, lagerbestand, einheit_id) VALUES (1, 'test', 12, 1, 12, 12, 1)");
            //statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, rabatt) VALUES (1,1,1,1)");
            setup();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("erstanmeldung.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("ERP-Software");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            handleWindowCloseRequest(stage);
        });


    }
    private void handleWindowCloseRequest(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("Möchten sie ohne Speichern fortfahren?");
        ButtonType buttonTypeFortfahren = new ButtonType("fortfahren");
        ButtonType buttonTypeCancel = new ButtonType("Abbrechen");
        alert.getButtonTypes().setAll(buttonTypeFortfahren, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeFortfahren) {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
                resultSet.next();
                int rechnungsnummer = resultSet.getInt("rechnungsnummer");
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                statement.execute("DELETE FROM bestellung WHERE bestellungId = " + rechnungsnummer);

            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            stage.close();
        } else {
            alert.close();
        }


    }
    public void setup(){
        //TODO Bilder löschen
        try {
            ResultSet resultsetLoeschen = statement.executeQuery("SELECT * FROM loeschen");
            while (resultsetLoeschen.next()){
                //File file = new File("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\" + resultsetLoeschen.getInt("artikel_Id") + "\\" + resultsetLoeschen.getInt("bildnummer") + "." + resultsetLoeschen.getString("dateityp"));
                Files.delete(Path.of("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\" + resultsetLoeschen.getInt("artikel_Id") + "\\" + resultsetLoeschen.getInt("bildnummer") + "." + resultsetLoeschen.getString("dateityp")));
            }
            resultsetLoeschen.close();
            statement.execute("DROP TABLE IF EXISTS loeschen");
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
