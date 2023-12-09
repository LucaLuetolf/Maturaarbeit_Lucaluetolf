package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class GuiArtikel extends GuiTaskleiste implements Initializable {
    @FXML
    private GridPane gridpaneArtikel;
    @FXML
    private Label labelWillkommen;
    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int time1 = Integer.parseInt(formatter.format(time));
        if (time1 >= 6 && time1 < 11) {
            labelWillkommen.setText("Good Morning");
        }
        if (time1 >= 11 && time1 < 13) {
            labelWillkommen.setText("Good Noon");
        }
        if (time1 >= 13 && time1 < 17) {
            labelWillkommen.setText("Good Afternoon");
        }
        if (time1 >= 17 && time1 < 22) {
            labelWillkommen.setText("Good Evening");
        }
        if (time1 >= 22 || time1 < 6) {
            labelWillkommen.setText("Good Night");
        }
        int column = 0;
        int row = 0;
        int prefHeight = 220;
        int prefHeightPerColumn = 220;
        try {
            ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");
            int orange;
            resultSetUnternehmen.next();
            orange = resultSetUnternehmen.getInt("lagerbestandOrange");
            resultSetUnternehmen.close();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM artikel, einheiten WHERE einheitId = einheit_id");
            while (resultSet.next()) {
                if (column == 5) {
                    row = row + 1;
                    gridpaneArtikel.addColumn(row);
                    prefHeight = prefHeight + prefHeightPerColumn;
                    gridpaneArtikel.setPrefSize(850, prefHeight);
                    column = 0;
                }
                Pane pane = new Pane();
                JFXButton button = new JFXButton("mehr Infos");
                button.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");

                Label labelTitelArtikelnummer = new Label("Artikelnr.:");
                Label labelTitelName = new Label("Name:");
                Label labelTitelPreis = new Label("Preis:");
                Label labelTitelMenge = new Label("Inhalt: ");
                Label labelArtikelnummer = new Label(resultSet.getString("artikelId"));


                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelMenge.setStyle("-fx-text-fill: #FFFFFF ");
                Label labelName = new Label(resultSet.getString("name"));
                Label labelPreis = new Label(resultSet.getString("preis"));
                Label labelMenge = new Label(resultSet.getInt("menge") + " " + resultSet.getString("abkuerzung"));

                ImageView imageView = null;
                if (resultSet.getString("dateityp") == null){
                    String imagePath = String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Artikel/Artikel.png"));
                    Image image = new Image(imagePath);
                    imageView = new ImageView();
                    imageView.setImage(image);
                }else{
                    String imagePath = String.valueOf("Bilder/Benutzer/Artikel/" + resultSet.getInt("artikelId") + "/" + resultSet.getInt("bildnummer") + "." + resultSet.getString("dateityp"));
                    Image image = new Image(new FileInputStream(imagePath));
                    imageView = new ImageView();
                    imageView.setImage(image);
                }
                imageView.setPreserveRatio(true);
                //54
                //imageView.setLayoutX(10);
                //imageView.setLayoutY(14);
                imageView.setFitWidth(150);
                imageView.setFitHeight(65);

                StackPane stackPaneImage = new StackPane();
                stackPaneImage.getChildren().add(imageView);

                stackPaneImage.setLayoutX(10);
                stackPaneImage.setLayoutY(14);
                stackPaneImage.setPrefSize(150,65);
                StackPane.setAlignment(imageView, javafx.geometry.Pos.CENTER);


                Line lineLagerbestand = new Line();
                pane.setPrefSize(170, 220);
                pane.getChildren().add(labelArtikelnummer);
                pane.getChildren().add(labelName);
                pane.getChildren().add(labelPreis);
                pane.getChildren().add(labelMenge);
                pane.getChildren().add(labelTitelArtikelnummer);
                pane.getChildren().add(labelTitelName);
                pane.getChildren().add(labelTitelPreis);
                pane.getChildren().add(labelTitelMenge);
                pane.getChildren().add(lineLagerbestand);
                pane.getChildren().add(stackPaneImage);
                labelTitelArtikelnummer.setLayoutX(14);
                labelTitelArtikelnummer.setLayoutY(88);
                labelTitelName.setLayoutX(14);
                labelTitelName.setLayoutY(105);
                labelTitelPreis.setLayoutX(14);
                labelTitelPreis.setLayoutY(122);
                labelTitelMenge.setLayoutX(14);
                labelTitelMenge.setLayoutY(139);
                labelArtikelnummer.setLayoutX(76);
                labelArtikelnummer.setLayoutY(88);
                labelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelArtikelnummer.setMaxWidth(90);
                labelName.setLayoutX(76);
                labelName.setLayoutY(105);
                labelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelName.setMaxWidth(90);
                labelPreis.setLayoutX(76);
                labelPreis.setLayoutY(122);
                labelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                labelPreis.setMaxWidth(90);
                labelMenge.setLayoutX(76);
                labelMenge.setLayoutY(139);
                labelMenge.setStyle("-fx-text-fill: #FFFFFF ");
                labelMenge.setMaxWidth(90);

                lineLagerbestand.setLayoutX(84);
                lineLagerbestand.setLayoutY(196);
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
                button.setLayoutY(162);//152
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
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
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
}
