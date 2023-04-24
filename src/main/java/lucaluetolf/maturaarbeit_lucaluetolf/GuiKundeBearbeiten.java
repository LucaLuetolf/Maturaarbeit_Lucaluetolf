package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class GuiKundeBearbeiten extends GuiLeiste implements Initializable {
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
    private TextField textfeldKundennummer1;
    @FXML
    private TextField textfeldNachname1;
    @FXML
    private TextField textfeldVorname1;
    @FXML
    private TextField textfeldAdresse1;
    @FXML
    private TextField textfeldPostleitzahl1;
    @FXML
    private TextField textfeldOrt1;
    @FXML
    private TextField textfeldEmail1;
    @FXML
    private TextField textfeldNatelnummer1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            textfeldKundennummer1.setEditable(false);
            textfeldKundennummer1.setText(String.valueOf(statement.execute("SELECT id FROM KUNDEN WHERE id=" + getKundennummerBearbeiten())));
            textfeldNachname1.setText(String.valueOf(statement.execute("SELECT nachname FROM kunden WHERE id=" + getKundennummerBearbeiten())));
            textfeldVorname1.setText(String.valueOf(statement.execute("SELECT vorname FROM kunden WHERE id=" + getKundennummerBearbeiten())));
            textfeldAdresse1.setText(String.valueOf(statement.execute("SELECT adresse FROM kunden WHERE id=" + getKundennummerBearbeiten())));
            textfeldPostleitzahl1.setText(String.valueOf(statement.execute("SELECT postleitzahl FROM kunden WHERE id=" + getKundennummerBearbeiten())));
            textfeldOrt1.setText(String.valueOf(statement.execute("SELECT ort FROM kunden WHERE id=" + getKundennummerBearbeiten())));
            textfeldEmail1.setText(String.valueOf(statement.execute("SELECT email FROM kunden WHERE id=" + getKundennummerBearbeiten())));
            textfeldNatelnummer1.setText(String.valueOf(statement.execute("SELECT natelnummer FROM kunden WHERE id=" + getKundennummerBearbeiten())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void kundeBearbeiten(ActionEvent event){
        try {
            statement.execute("UPDATE kunden SET nachname=" + textfeldNachname1.getText() + "WHERE id =" + getKundennummerBearbeiten());
            statement.execute("UPDATE kunden SET vorname=" + textfeldVorname1.getText() + "WHERE id =" + getKundennummerBearbeiten());
            statement.execute("UPDATE kunden SET adresse=" + textfeldAdresse1.getText() + "WHERE id =" + getKundennummerBearbeiten());
            statement.execute("UPDATE kunden SET postleitzahl=" + textfeldPostleitzahl1.getText() + "WHERE id =" + getKundennummerBearbeiten());
            statement.execute("UPDATE kunden SET ort=" + textfeldOrt1.getText() + "WHERE id =" + getKundennummerBearbeiten());
            statement.execute("UPDATE kunden SET email=" + textfeldEmail1.getText() + "WHERE id =" + getKundennummerBearbeiten());
            statement.execute("UPDATE kunden SET natelnummer=" + textfeldNatelnummer1.getText() + "WHERE id =" + getKundennummerBearbeiten());

            root = FXMLLoader.load(getClass().getResource("kunde.fxml"));
        } catch (SQLException e){
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
