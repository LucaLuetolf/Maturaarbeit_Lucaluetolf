package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiLeiste {
    private int kundennummerBearbeiten;

    public int getKundennummerBearbeiten() {
        return kundennummerBearbeiten;
    }

    public void setKundennummerBearbeiten(int kundennummerBearbeiten) {
        this.kundennummerBearbeiten = kundennummerBearbeiten;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    protected void toSceneStartseite(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneVerkauf(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("Verkauf.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneLagerbestand(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("lagerbestand.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
