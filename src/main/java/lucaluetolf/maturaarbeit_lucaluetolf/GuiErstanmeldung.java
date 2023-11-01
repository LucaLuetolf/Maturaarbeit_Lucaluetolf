package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiErstanmeldung extends GuiTaskleiste implements Initializable {

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
    private ImageView imageviewBeschreibung;
    @FXML
    private Label labelBeschreibung;
    @FXML
    private Button buttonZurueck;
    @FXML
    private Button buttonWeiter;


    private String s1 = "Herzlich Willkommen, nachfolgend wird die App erklärt und die wichtigen Inhalte werden erfasst";
    private String s2 = "Login";
    private String s3 = "Startseite";
    private String s4 = "Artikel";
    private String s5 = "Artikel Erfassen";
    private String s6 = "mehr Infos";
    private String s7 = "Kunden";
    private String s8 = "Kunde Erfassen";
    private String s9 = "Kunde bearbeiten";
    private String s10 = "Rechnung erstellen, Kunde";
    private String s11 = "Rechnung erstellen, Artikel";
    private String s12 = "Pdf Dokument";
    private String s13 = "mehr Infos";
    private String s14 = "mehr Infos";


    @FXML
    protected void zurueck(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId()) - 1));
        if (Integer.parseInt(buttonWeiter.getId()) == 1){
            buttonZurueck.setVisible(false);
        }
        switch (Integer.parseInt(buttonWeiter.getId())) {
            case 1:
                try {
                    buttonZurueck.setVisible(false);
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s1);
            case 2:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s2);
            case 3:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s3);
            case 4:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s4);
            case 5:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s5);
            case 6:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) + ".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
        }
    }

    @FXML
    protected void weiter(){
        buttonWeiter.setId(String.valueOf(Integer.parseInt(buttonWeiter.getId())+1));

        switch (Integer.parseInt(buttonWeiter.getId())){
            case 1:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) +".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s1);
            case 2:
                buttonZurueck.setVisible(true);
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) +".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s2);
            case 3:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) +".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s3);
            case 4:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) +".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s4);
            case 5:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) +".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s5);
            case 6:
                try {
                    Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\" + (buttonWeiter.getId()) +".jpg"));
                    imageviewBeschreibung.setImage(image);
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                labelBeschreibung.setText(s6);
            case 7:
                /*try {
                    root = FXMLLoader.load(getClass().getResource("erstanmeldungDatenErfassen.fxml"));
                } catch (IOException e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                ActionEvent event = new ActionEvent();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();*/
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonWeiter.setId("1");
        buttonZurueck.setVisible(false);
        try {
            Image image = new Image(new FileInputStream("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Erstanmeldung\\1.jpg"));
            imageviewBeschreibung.setImage(image);
            labelBeschreibung.setText(s1);
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

}
