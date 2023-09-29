package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class GuiTaskleiste {
    private Stage stage;
    private Scene scene;
    private Parent root;

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
    protected void toSceneStartseite(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneArtikel(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneKunden(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneNeueRechnung(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("kundenFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            resultSetRechnungsnummer.next();
            statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp) VALUES (" + resultSetRechnungsnummer.getInt("rechnungsnummer") + ",1)");
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void toSceneVerkauf(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("kundenFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            resultSetRechnungsnummer.next();
            statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp) VALUES (" + resultSetRechnungsnummer.getInt("rechnungsnummer") + ",2)");
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void toSceneLagerbestand(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("lagerbestand.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneMitarbeiter(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("mitarbeiter.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneArchiv(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("archiv.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneBuchhaltung(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("buchhaltung.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
