package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            resultSetRechnungsnummer.next();
            statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp) VALUES (" + resultSetRechnungsnummer.getInt("rechnungsnummer") + ",1);");
            resultSetRechnungsnummer.close();
            root = FXMLLoader.load(getClass().getResource("kundenFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
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
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void toSceneVerkauf(ActionEvent event) {
        try {
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            resultSetRechnungsnummer.next();
            statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp) VALUES (" + resultSetRechnungsnummer.getInt("rechnungsnummer") + ",2);");
            resultSetRechnungsnummer.close();
            root = FXMLLoader.load(getClass().getResource("kundenFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
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
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void toScenePdfBearbeiten(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        TextField textfield = new TextField();
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Bitte geben sie die Rechnungs- oder Quittungsnummer ein:");
        textfield.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event1) {
                textfield.setText(textfield.getText().replaceAll("[^0-9]", ""));
                textfield.positionCaret(textfield.getLength());
            }
        });

        alert.getDialogPane().setContent(textfield);
        alert.showAndWait();
        int rechnungsnummer = Integer.parseInt(textfield.getText());

        try {
            ResultSet resultsetUeberpruefen = statement.executeQuery("SELECT COUNT(bestellung_Id) FROM bearbeiter WHERE bestellung_Id = " + rechnungsnummer);
            resultsetUeberpruefen.next();
            if (resultsetUeberpruefen.getInt(1) == 1){
                statement.execute("UPDATE unternehmen SET bearbeiten = " + rechnungsnummer);
                root = FXMLLoader.load(getClass().getResource("artikelFuerRechnung.fxml"));
            } else{
                Alert alertKeineRechnung = new Alert(Alert.AlertType.INFORMATION);
                alertKeineRechnung.setTitle("Information Dialog");
                alertKeineRechnung.setHeaderText("Neue Information:");
                alertKeineRechnung.setContentText("Leider wurde keine Rechnung oder Quittung mit der Nummer: " + rechnungsnummer + " gefunden");
                alertKeineRechnung.showAndWait();
                root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
            }

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    }

    @FXML
    protected void toSceneAnalyse(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("analyse.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
