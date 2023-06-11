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
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiArtikel extends GuiLeiste implements Initializable {
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
            ResultSet resultSet  = statement.executeQuery("SELECT * FROM artikel");
            while(resultSet.next()) {
                if(column == 5){
                    row = row + 1;
                    gridpaneArtikel.addColumn(row);
                    prefHeight = prefHeight + prefHeightPerColumn;
                    gridpaneArtikel.setPrefSize(850, prefHeight);
                    column = 0;
                }
                Pane pane = new Pane();
                JFXButton button = new JFXButton("Mehr Infos");
                button.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");

                Label labelTitelArtikelnummer = new Label ("Artikelnr.:");
                Label labelTitelName = new Label ("Name:");
                Label labelTitelPreis = new Label ("Preis:");
                Label labelArtikelnummer = new Label(resultSet.getString("id"));

                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                Label labelName = new Label(resultSet.getString("name"));
                Label labelPreis = new Label(resultSet.getString("preis"));
                Image image = new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//System//Artikel//Artikel.png");
                ImageView imageView = new ImageView(image);
                /*if (new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//Benutzer//Artikel//"+ resultSet.getString("id")) == null){
                    Image image = new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//System//Artikel//Artikel.png"); //TODO Bild festlegen
                    imageView.setImage(image);
                }else{
                    Image image = new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//Benutzer//Artikel//"+ resultSet.getString("id"));
                    imageView.setImage(image);
                }*/

                pane.setPrefSize(170,193);
                pane.getChildren().add(labelArtikelnummer);
                pane.getChildren().add(labelName);
                pane.getChildren().add(labelPreis);
                pane.getChildren().add(labelTitelArtikelnummer);
                pane.getChildren().add(labelTitelName);
                pane.getChildren().add(labelTitelPreis);
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

                pane.getChildren().add(imageView);
                imageView.setLayoutX(54);
                imageView.setLayoutY(14);
                imageView.setFitWidth(65);
                imageView.setFitHeight(65);

                pane.getChildren().add(button);
                button.setLayoutX(50);
                button.setLayoutY(152);
                button.setId(resultSet.getString("id"));
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(button.getId());
                    }
                });
                pane.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                gridpaneArtikel.add(pane,column,row);
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
}
