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
import java.sql.*;
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
            int id = 123445;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM KUNDEN WHERE id = " + id);
            textfeldKundennummer1.setEditable(false);
            textfeldKundennummer1.setText(String.valueOf(statement.execute("SELECT id FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldNachname1.setText(String.valueOf(statement.execute("SELECT nachname FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldVorname1.setText(String.valueOf(statement.execute("SELECT vorname FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldAdresse1.setText(String.valueOf(statement.execute("SELECT adresse FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldPostleitzahl1.setText(String.valueOf(statement.execute("SELECT postleitzahl FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldOrt1.setText(String.valueOf(statement.execute("SELECT ort FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldEmail1.setText(String.valueOf(statement.execute("SELECT email FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            //textfeldNatelnummer1.setText(String.valueOf(statement.execute("SELECT natelnummer FROM KUNDEN,BEARBEITEN WHERE id = kunden")));
            textfeldNatelnummer1.setText(resultSet.getString("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void kundeBearbeiten(ActionEvent event){
        try {
            int id = Integer.parseInt(String.valueOf(statement.execute("SELECT kunden FROM bearbeiten")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
