package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            Statement statement = connection.createStatement();
            //statement.execute("DROP TABLE IF EXISTS bearbeiter");
            //statement.execute("DROP TABLE IF EXISTS bestellung");
            //statement.execute("DROP TABLE IF EXISTS unternehmen");

            statement.execute("CREATE TABLE IF NOT EXISTS kunden (kundenId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS artikel (artikelId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(30), preis DOUBLE, menge DOUBLE, rabatt DOUBLE, lagerbestand INT)" );
            statement.execute("CREATE TABLE IF NOT EXISTS mitarbeiter (mitarbeiterId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer INT, pin INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS bestellung (bestellungId INT NOT NULL, artikel_id INT, anzahl INT, rabatt DOUBLE)");
            statement.execute("CREATE TABLE IF NOT EXISTS bearbeiter (bestellung_id INT NOT NULL, mitarbeiter_id INT, kunden_id INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS unternehmen (rechnungsnummer INT, benutzername VARCHAR(30), passwort VARCHAR(30), lagerbestandOrange INT)");
            //statement.execute("INSERT INTO unternehmen (rechnungsnummer, benutzername, passwort, lagerbestandOrange) VALUES (1, 'Hans','Hans', 10)");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("startseite.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("App von Luca Lütolf");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
