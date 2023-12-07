package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Objects;


public class Main extends Application {
    Statement statement;

    Scene scene;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:./DatenbankERPSoftware", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage){
        try {
            Class.forName("org.h2.Driver");

            statement.execute("CREATE TABLE IF NOT EXISTS kunden (kundenId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer VARCHAR(10))");
            statement.execute("CREATE TABLE IF NOT EXISTS artikel (artikelId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(30), preis DOUBLE, menge DOUBLE, einheit_id INT, rabatt INT, lagerbestand INT, dateityp VARCHAR(30), bildnummer INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS bestellung (bestellungId INT NOT NULL, artikel_id INT, anzahl INT, name_bestellung VARCHAR(30), preis_bestellung DOUBLE, menge_bestellung DOUBLE, einheit_id_bestellung INT, rabatt_bestellung INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS bearbeiter (bestellung_id INT NOT NULL, kunden_id INT, dokumenttyp INT, datum DATE)");
            statement.execute("CREATE TABLE IF NOT EXISTS unternehmen (unternehmensname VARCHAR(30), rechnungsnummer INT, benutzername VARCHAR(30), passwort VARCHAR(30), lagerbestandOrange INT, Bank VARCHAR(30), IBAN VARCHAR (30), bearbeiten INT, bildnummer INT, dateityp VARCHAR(30))");
            statement.execute("CREATE TABLE IF NOT EXISTS einheiten (einheitId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, bezeichnung VARCHAR(30), abkuerzung VARCHAR(30), aktiv BOOLEAN)");
            statement.execute("CREATE TABLE IF NOT EXISTS verkaufteStueck (datum DATE, artikel_id INT, anzahl INT)");
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        setup(stage);
    }
    public void setup(Stage stage){
        try {
            ResultSet resultSetUnternehmen = statement.executeQuery("SELECT COUNT(*) FROM unternehmen");
            resultSetUnternehmen.next();
            if (resultSetUnternehmen.getInt(1) == 0) {
                statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (1, 'Stück', 'stk', true)");
                statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (2, 'Gramm', 'gr', true)");
                statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (3, 'Milliliter', 'ml', true)");
                AllgemeineMethoden.ordnerErstellen("Kundendateien");
                AllgemeineMethoden.ordnerErstellen("Kundendateien/übrige Quittungen");
                AllgemeineMethoden.ordnerErstellen("Bilder");
                AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer");
                AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel");
                AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Artikel/Übergang");
                AllgemeineMethoden.ordnerErstellen("Bilder/Benutzer/Unternehmen");

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("erstanmeldung.fxml"));
                scene = new Scene(fxmlLoader.load(), 1200, 800);
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        event.consume();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Neue Nachricht:");
                        alert.setContentText("Die App kann momentan nicht geschlossen werden");
                        alert.showAndWait();
                    }
                });
            } else{
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
                scene = new Scene(fxmlLoader.load(), 1200, 800);
                ResultSet resultSet = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
                resultSet.next();
                int rechnungsnummer = resultSet.getInt("rechnungsnummer");
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                statement.execute("DELETE FROM bestellung WHERE bestellungId = " + rechnungsnummer);
            }
        }catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        stage.setTitle("ERP-Software");
        try {
            Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Icon/AppIcon.png")));
            stage.getIcons().add(image);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
