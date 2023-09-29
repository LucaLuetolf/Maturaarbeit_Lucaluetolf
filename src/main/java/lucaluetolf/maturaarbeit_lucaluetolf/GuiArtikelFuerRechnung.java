package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiArtikelFuerRechnung extends GuiTaskleiste implements Initializable {
    Statement statement;
    Connection connection;
    private int modus;

    {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
            ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE rechnungsnummer = bestellung_id");
            resultSetModus.next();
            modus = resultSetModus.getInt("dokumenttyp");
            resultSetModus.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private GridPane gridpaneArtikel;
    @FXML
    private GridPane gridpaneWarenkorb;
    @FXML
    private Pane paneWarenkorbTotal;
    @FXML
    private Label labelAdressatKundennummer;
    @FXML
    private Label labelAdressatNachname;
    @FXML
    private Label labelAdressatVorname;
    @FXML
    private Label labelAdressatAdresse;
    @FXML
    private JFXButton buttonAdressatAendern;
    @FXML
    private Label labelTotal;
    @FXML
    private Label labelRabatte;
    @FXML
    private Label labelSumme;
    @FXML
    private JFXButton dokumentAbschliessen;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (modus == 1){
            dokumentAbschliessen.setText("Rechnung erstellen");
        } else if (modus == 2) {
            dokumentAbschliessen.setText("Verkauf abschliessen");

        }
        int column = 0;
        int row = 0;
        int prefHeight = 193;
        int prefHeightPerColumn = 193;
        try {
            ResultSet resultSetArtikel = statement.executeQuery("SELECT * FROM artikel");
            while(resultSetArtikel.next()) {
                if(column == 5){
                    row = row + 1;
                    gridpaneArtikel.addColumn(row);
                    prefHeight = prefHeight + prefHeightPerColumn;
                    gridpaneArtikel.setPrefSize(850, prefHeight);
                    column = 0;
                }
                Pane paneArtikel = new Pane();
                JFXButton buttonMinus = new JFXButton("-");
                JFXButton buttonPlus = new JFXButton("+");
                buttonMinus.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");
                buttonPlus.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");

                JFXTextField textFieldAnzahl = new JFXTextField("0");

                Label labelTitelArtikelnummer = new Label ("Artikelnr.:");
                Label labelTitelName = new Label ("Name:");
                Label labelTitelPreis = new Label ("Preis:");
                Label labelArtikelnummer = new Label(resultSetArtikel.getString("artikelId"));

                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                Label labelName = new Label(resultSetArtikel.getString("name"));
                Label labelPreis = new Label(resultSetArtikel.getString("preis"));
                /*Image image = new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//System//Artikel//Artikel.png");
                ImageView imageView = new ImageView(image);
                if (new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//Benutzer//Artikel//"+ resultSetArtikel.getString("id")) == null){
                    image = new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//System//Artikel//Artikel.png"); //TODO Bild festlegen
                    imageView.setImage(image);
                }else{
                    image = new Image("C://Users//Luca Schule//Maturaarbeit_LucaLuetolf//Bilder//Benutzer//Artikel//"+ resultSetArtikel.getString("id") + ".png");
                    imageView.setImage(image);
                }*/

                paneArtikel.setPrefSize(170,193);
                paneArtikel.getChildren().add(labelArtikelnummer);
                paneArtikel.getChildren().add(labelName);
                paneArtikel.getChildren().add(labelPreis);
                paneArtikel.getChildren().add(labelTitelArtikelnummer);
                paneArtikel.getChildren().add(labelTitelName);
                paneArtikel.getChildren().add(labelTitelPreis);
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

                /*pane.getChildren().add(imageView);
                imageView.setLayoutX(54);
                imageView.setLayoutY(14);
                imageView.setFitWidth(65);
                imageView.setFitHeight(65);*/

                paneArtikel.getChildren().add(buttonMinus);
                paneArtikel.getChildren().add(buttonPlus);
                //TODO x und y festlegen
                buttonMinus.setLayoutX(20);
                buttonMinus.setLayoutY(152);
                buttonMinus.setId(resultSetArtikel.getString("artikelId"));
                buttonPlus.setLayoutX(70);
                buttonPlus.setLayoutY(152);
                buttonPlus.setId(resultSetArtikel.getString("artikelId"));

                paneArtikel.getChildren().add(textFieldAnzahl);
                textFieldAnzahl.setLayoutX(50);
                textFieldAnzahl.setLayoutY(152);
                textFieldAnzahl.setId(resultSetArtikel.getString("artikelId"));
                textFieldAnzahl.setPrefSize(20,20);

                Pane paneWarenkorb = new Pane();
                paneWarenkorb.setPrefSize(200,82);

                Label labelWarenkorbArtikelnummer = new Label();
                Label labelWarenkorbName = new Label();
                Label labelWarenkorbPreis = new Label();
                Label labelWarenkorbAnzahl = new Label();
                Label labelWarenkorbTotal = new Label();

                paneWarenkorb.getChildren().addAll(labelWarenkorbArtikelnummer, labelWarenkorbName, labelWarenkorbPreis, labelWarenkorbAnzahl, labelWarenkorbTotal);
                labelWarenkorbArtikelnummer.setLayoutX(11);
                labelWarenkorbArtikelnummer.setLayoutY(16);
                labelWarenkorbName.setLayoutX(71);
                labelWarenkorbName.setLayoutY(16);
                labelWarenkorbPreis.setLayoutX(11);
                labelWarenkorbPreis.setLayoutY(49);
                labelWarenkorbAnzahl.setLayoutX(71);
                labelWarenkorbAnzahl.setLayoutY(49);
                labelWarenkorbTotal.setLayoutX(151);
                labelWarenkorbTotal.setLayoutY(49);

                buttonMinus.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            int anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            if (anzahl != 0){
                                anzahl = anzahl -1;
                            }
                            textFieldAnzahl.setText(String.valueOf(anzahl));
                            paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + buttonMinus.getId()), anzahl);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                buttonPlus.setOnAction(new EventHandler <ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int lagerbestand = 0;
                        try {
                            ResultSet resultSet = statement.executeQuery("SELECT lagerbestand FROM artikel WHERE artikelId = " + buttonPlus.getId());
                            if (resultSet.next()){
                                lagerbestand = resultSet.getInt("lagerbestand");
                            }
                            int anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            if (anzahl < lagerbestand){
                                anzahl = anzahl +1;
                            }
                            resultSet.close();
                            textFieldAnzahl.setText(String.valueOf(anzahl));
                            paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId =" + buttonPlus.getId()), anzahl);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });



                paneArtikel.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                gridpaneArtikel.add(paneArtikel,column,row);
                column++;
                textFieldAnzahl.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        try {
                            int lagerbestand = 0;
                            ResultSet resultSet = statement.executeQuery("SELECT lagerbestand FROM artikel WHERE artikelId = " + buttonPlus.getId());
                            if (resultSet.next()){
                                lagerbestand = resultSet.getInt("lagerbestand");
                            }
                            int anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            if(anzahl > lagerbestand){
                                textFieldAnzahl.setText(String.valueOf(lagerbestand));
                                anzahl = lagerbestand;
                            }else{
                                textFieldAnzahl.setText(String.valueOf(anzahl));
                            }
                            resultSet.close();
                            paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + textFieldAnzahl.getId()), anzahl);

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
            //TODO
            resultSetArtikel.close();

            ResultSet resultSetBearbeiter = statement.executeQuery("SELECT * FROM kunden, bearbeiter, unternehmen WHERE bestellung_id = rechnungsnummer AND kunden_id = kundenId ");
            if(resultSetBearbeiter.next()){
                labelAdressatKundennummer.setText(resultSetBearbeiter.getString("kundenId"));
                labelAdressatNachname.setText(resultSetBearbeiter.getString("nachname"));
                labelAdressatVorname.setText(resultSetBearbeiter.getString("vorname"));
                labelAdressatAdresse.setText(resultSetBearbeiter.getString("adresse"));
            }
            //TODO
            resultSetBearbeiter.close();
            buttonAdressatAendern.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
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
            });
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }


        gridpaneArtikel.setStyle("-fx-border-color: #FFFFFF");

    }

    private void paneFuerWarenkorb(ResultSet resultSet, int anzahl){
        Label labelWarenkorbArtikelnummer;
        Label labelWarenkorbName;
        Label labelWarenkorbPreis;
        Label labelWarenkorbAnzahl;
        Label labelWarenkorbTotal;

        gridpaneWarenkorb.getChildren().clear();
        int id = 0;
        int artikelnummer = 0;
        double rabatt = 0;
        int rechnungsnr = 0;
        try{
            if (resultSet.next()){
                id = resultSet.getInt("artikelId");
                artikelnummer = resultSet.getInt("artikelId");
                rabatt = resultSet.getDouble("rabatt");
            }
            resultSet.close();
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            if (resultSetRechnungsnummer.next()){
                rechnungsnr = resultSetRechnungsnummer.getInt("rechnungsnummer");
            }
            resultSetRechnungsnummer.close();

            if(anzahl != 0){
                statement.execute("DELETE FROM bestellung WHERE artikel_id = " + id );
                statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, rabatt) VALUES (" + rechnungsnr+ "," + artikelnummer + "," + anzahl + "," + rabatt + ")");
            }else{
                statement.execute("DELETE FROM bestellung WHERE artikel_id = " + id );
            }


            ResultSet resultSetWarenkorb = statement.executeQuery("SELECT * FROM unternehmen, bestellung, artikel WHERE rechnungsnummer = bestellungId AND artikelId = artikel_id");
            while (resultSetWarenkorb.next()){
                Pane paneWarenkorb = new Pane();
                paneWarenkorb.setPrefSize(200,82);

                labelWarenkorbArtikelnummer = new Label(resultSetWarenkorb.getString("artikelId"));
                labelWarenkorbName = new Label(resultSetWarenkorb.getString("name"));
                labelWarenkorbPreis = new Label(resultSetWarenkorb.getString("preis"));
                labelWarenkorbAnzahl = new Label(String.valueOf(resultSetWarenkorb.getInt("anzahl")));
                labelWarenkorbTotal = new Label(String.valueOf(resultSetWarenkorb.getInt("anzahl") * Double.parseDouble(labelWarenkorbPreis.getText())));

                paneWarenkorb.getChildren().addAll(labelWarenkorbArtikelnummer, labelWarenkorbName, labelWarenkorbPreis, labelWarenkorbAnzahl, labelWarenkorbTotal);
                labelWarenkorbArtikelnummer.setLayoutX(11);
                labelWarenkorbArtikelnummer.setLayoutY(16);
                labelWarenkorbName.setLayoutX(71);
                labelWarenkorbName.setLayoutY(16);
                labelWarenkorbPreis.setLayoutX(11);
                labelWarenkorbPreis.setLayoutY(49);
                labelWarenkorbAnzahl.setLayoutX(71);
                labelWarenkorbAnzahl.setLayoutY(49);
                labelWarenkorbTotal.setLayoutX(151);
                labelWarenkorbTotal.setLayoutY(49);
                int counter = gridpaneWarenkorb.getRowCount();
                paneWarenkorb.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                labelWarenkorbArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelWarenkorbName.setStyle("-fx-text-fill: #FFFFFF ");
                labelWarenkorbPreis.setStyle("-fx-text-fill: #FFFFFF ");
                labelWarenkorbAnzahl.setStyle("-fx-text-fill: #FFFFFF ");
                labelWarenkorbTotal.setStyle("-fx-text-fill: #FFFFFF ");
                gridpaneWarenkorb.addRow(counter+1, paneWarenkorb);
                gridpaneWarenkorb.setStyle("-fx-border-color: #FFFFFF");

            }
            resultSetWarenkorb.close();
            ResultSet resultSetSumme = statement.executeQuery("SELECT SUM(anzahl * preis) AS summe FROM bestellung, unternehmen, artikel WHERE rechnungsnummer = bestellungId AND artikelId = artikel_id");
            if(resultSetSumme.next()){
                labelTotal.setText(String.valueOf(resultSetSumme.getDouble("summe")));
                labelTotal.setTextAlignment(TextAlignment.LEFT);
            }

            resultSetSumme.close();
            ResultSet resultSetRabatt = statement.executeQuery("SELECT * FROM artikel, bestellung, unternehmen WHERE rechnungsnummer = bestellungId AND artikelId = artikel_id");
            double rabattTotal = 0;
            while (resultSetRabatt.next()){
                int rabattArtikel = resultSetRabatt.getInt("bestellung.rabatt");
                double preisArtikel = resultSetRabatt.getDouble("preis");
                int anzahlArtikel = resultSetRabatt.getInt("anzahl");
                rabattTotal = (preisArtikel / 100 * (rabattArtikel))*anzahlArtikel;
            }
            double gerundet = Math.round(rabattTotal * 20.0) / 20.0;

            labelRabatte.setText("-" + gerundet);
            labelRabatte.setTextAlignment(TextAlignment.LEFT);
            resultSetRabatt.close();

            labelSumme.setText(String.valueOf(Double.parseDouble(labelTotal.getText()) + Double.parseDouble(labelRabatte.getText())));
            System.out.println(Double.parseDouble(labelTotal.getText()));
            System.out.println(Double.parseDouble(labelTotal.getText()) + Double.parseDouble(labelRabatte.getText()));
            labelSumme.setTextAlignment(TextAlignment.RIGHT);

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }


    }
    @FXML
    protected void buttonRechnungErstellen(ActionEvent event) {

        if (modus == 1){
            modusRechnung();
        } else if (modus == 2) {
            modusQuittung();
        }
        try {
            ResultSet resultSetLagerbestand = statement.executeQuery("SELECT * FROM unternehmen, bestellung WHERE rechnungsnummer = bestellungId");
            while (resultSetLagerbestand.next()){
                Statement updateStatement = connection.createStatement();
                updateStatement.execute("UPDATE artikel SET lagerbestand = lagerbestand -" + resultSetLagerbestand.getInt("anzahl") + "WHERE artikelId = " + resultSetLagerbestand.getInt("artikel_id"));
            }
            resultSetLagerbestand.close();
            int rechnungsnummer= 0;
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT * FROM unternehmen");
            if(resultSetRechnungsnummer.next()){
                rechnungsnummer = resultSetRechnungsnummer.getInt("rechnungsnummer");
            }
            statement.execute("UPDATE unternehmen SET rechnungsnummer="+ (rechnungsnummer+1) +"WHERE rechnungsnummer="+rechnungsnummer);
            resultSetRechnungsnummer.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

    private void modusRechnung(){
        PdfErstellen.layout1(1);
    }
    private void modusQuittung() {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Zahlen-Alert");
        dialog.setHeaderText("Hier steht der Text");

        TextField textField = new TextField();
        textField.setPromptText("0");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Buttons für die Zahlen 1-9, Clear, 0 und OK
        for (int i = 1; i <= 9; i++) {
            Button button = new Button(Integer.toString(i));
            button.setOnAction(e -> {
                textField.appendText(button.getText());
            });
            grid.add(button, (i - 1) % 3, (i - 1) / 3);
        }

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            textField.setText("0");
        });
        grid.add(clearButton, 0, 3);

        Button zeroButton = new Button("0");
        zeroButton.setOnAction(e -> {
            textField.appendText("0");
        });
        grid.add(zeroButton, 1, 3);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            String resultText = textField.getText();
            if (!resultText.isEmpty()) {
                dialog.setResult(Integer.parseInt(resultText));
            }
            dialog.close();
        });
        grid.add(okButton, 2, 3);


        VBox content = new VBox(textField, grid);
        dialog.getDialogPane().setContent(content);

        // Ergebnis des Dialogs abrufen
        int result = (int) dialog.showAndWait().orElse(null);

    }
}
