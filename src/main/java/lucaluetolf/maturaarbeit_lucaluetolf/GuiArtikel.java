package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiArtikel extends GuiTaskleiste implements Initializable {
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
    private GridPane gridpaneArtikel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int column = 0;
        int row = 0;
        int prefHeight = 193;
        int prefHeightPerColumn = 193;
        try {
            ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");

            int orange = 0;
            resultSetUnternehmen.next();
            orange = resultSetUnternehmen.getInt("lagerbestandOrange");
            resultSetUnternehmen.close();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM artikel");
            while (resultSet.next()) {
                if (column == 5) {
                    row = row + 1;
                    gridpaneArtikel.addColumn(row);
                    prefHeight = prefHeight + prefHeightPerColumn;
                    gridpaneArtikel.setPrefSize(850, prefHeight);
                    column = 0;
                }
                Pane pane = new Pane();
                JFXButton button = new JFXButton("Mehr Infos");
                button.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");

                Label labelTitelArtikelnummer = new Label("Artikelnr.:");
                Label labelTitelName = new Label("Name:");
                Label labelTitelPreis = new Label("Preis:");
                Label labelArtikelnummer = new Label(resultSet.getString("artikelId"));

                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                Label labelName = new Label(resultSet.getString("name"));
                Label labelPreis = new Label(resultSet.getString("preis"));

                ImageView imageView = new ImageView();
                bildAnzeigen(imageView, resultSet.getInt("artikelId"));

                Line lineLagerbestand = new Line();

                pane.setPrefSize(170, 193);
                pane.getChildren().add(labelArtikelnummer);
                pane.getChildren().add(labelName);
                pane.getChildren().add(labelPreis);
                pane.getChildren().add(labelTitelArtikelnummer);
                pane.getChildren().add(labelTitelName);
                pane.getChildren().add(labelTitelPreis);
                pane.getChildren().add(lineLagerbestand);
                pane.getChildren().add(imageView);
                labelTitelArtikelnummer.setLayoutX(14);
                labelTitelArtikelnummer.setLayoutY(88);
                labelTitelName.setLayoutX(14);
                labelTitelName.setLayoutY(105);
                labelTitelPreis.setLayoutX(14);
                labelTitelPreis.setLayoutY(122);
                labelArtikelnummer.setLayoutX(76);
                labelArtikelnummer.setLayoutY(88);
                labelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelName.setLayoutX(76);
                labelName.setLayoutY(105);
                labelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelPreis.setLayoutX(76);
                labelPreis.setLayoutY(122);
                labelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                lineLagerbestand.setLayoutX(84);
                lineLagerbestand.setLayoutY(179);
                lineLagerbestand.setStartX(-68.125);
                lineLagerbestand.setEndX(71.625);
                imageView.setLayoutX(53);
                imageView.setLayoutY(14);
                imageView.setFitHeight(65);
                imageView.setFitWidth(65);
                pane.getChildren().add(button);

                int bestand = resultSet.getInt("lagerbestand");

                if (bestand == 0) {
                    lineLagerbestand.setStyle("-fx-stroke: #ff172e; -fx-stroke-width: 2px");
                } else if (bestand <= orange && bestand > 0) {
                    lineLagerbestand.setStyle("-fx-stroke: #ffae5c; -fx-stroke-width: 2px");
                } else {
                    lineLagerbestand.setStyle("-fx-stroke: #00dd76; -fx-stroke-width: 2px");
                }

                button.setLayoutX(50);
                button.setLayoutY(145);//152
                button.setId(resultSet.getString("artikelId"));
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            statement.execute("UPDATE unternehmen SET bearbeiten = " + button.getId());
                            root = FXMLLoader.load(getClass().getResource("artikelEinzeln.fxml"));
                        } catch (Exception e) {
                            AllgemeineMethoden.fehlermeldung(e);
                        }
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                });
                pane.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                gridpaneArtikel.add(pane, column, row);
                column++;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        gridpaneArtikel.setStyle("-fx-border-color: #FFFFFF");

    }

    @FXML
    protected void toSceneArtikelErfassen(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("artikelErfassen.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void bildAnzeigen(ImageView imageviewArtikel, int artikelnummer){
        String imagePath = "src/main/resources/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/Benutzer/Artikel/" + artikelnummer;
        try {
            Image image = new Image(new FileInputStream(imagePath + ".png"));
            imageviewArtikel.setImage(image);
        } catch (IOException e) {
            try {
                Image image = new Image(new FileInputStream(imagePath + ".jpg"));
                imageviewArtikel.setImage(image);
            } catch (IOException ex) {
                try {
                    Image image = new Image(new FileInputStream(imagePath + ".jpeg"));
                    imageviewArtikel.setImage(image);
                } catch (FileNotFoundException exc) {
                    try {
                        Image image = new Image(new FileInputStream(imagePath + ".gif"));
                        imageviewArtikel.setImage(image);
                    } catch (FileNotFoundException exce) {
                        Image image = null;
                        try {
                            image = new Image(new FileInputStream(imagePath + ".svg"));
                            imageviewArtikel.setImage(image);
                        } catch (FileNotFoundException excep) {
                            try {
                                image = new Image(new FileInputStream("src/main/resources/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Artikel/Artikel.png"));
                                imageviewArtikel.setImage(image);
                            } catch (FileNotFoundException except){
                                AllgemeineMethoden.fehlermeldung(except);
                            }
                        }

                    }
                }
            }
        }
    }
}
