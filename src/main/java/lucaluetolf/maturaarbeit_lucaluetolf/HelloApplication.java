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

            statement.execute("CREATE TABLE IF NOT EXISTS kunden (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS artikel (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(30), preis DOUBLE, menge DOUBLE, rabatt DOUBLE, lagerbestand INT)" );
            statement.execute("CREATE TABLE IF NOT EXISTS mitarbeiter (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nachname VARCHAR(30), vorname VARCHAR(30), adresse VARCHAR(30), postleitzahl INT, ort VARCHAR(30), email VARCHAR(30), natelnummer INT, pin INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS bestellung (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, artikel_id INT, anzahl INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS bearbeiter (bestellung_id INT NOT NULL PRIMARY KEY, mitarbeiter_id INT, kunden_id INT)");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden");
            while (resultSet.next()){
                System.out.println(resultSet.getString("vorname") + ", " + resultSet.getString("nachname"));
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
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
