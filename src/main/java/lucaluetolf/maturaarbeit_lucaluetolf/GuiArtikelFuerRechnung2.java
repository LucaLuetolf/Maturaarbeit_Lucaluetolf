package lucaluetolf.maturaarbeit_lucaluetolf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuiArtikelFuerRechnung2 extends GuiTaskleiste implements Initializable {
    Statement statement;
    Connection connection;
    private int modus;
    private int rechnungsnummer;

    {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
            ResultSet resultsetBearbeiten = statement.executeQuery("SELECT COUNT(bearbeiten) FROM unternehmen");
            resultsetBearbeiten.next();
            int exists = resultsetBearbeiten.getInt(1);
            System.out.println("exists: " + exists);
            resultsetBearbeiten.close();
            if (exists == 1){
                ResultSet resultsetRechnungsnummer = statement.executeQuery("SELECT * FROM unternehmen");
                resultsetRechnungsnummer.next();
                rechnungsnummer = resultsetRechnungsnummer.getInt("bearbeiten");
                resultsetRechnungsnummer.close();
                ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE bearbeiten = bestellung_id");
                resultSetModus.next();
                modus = resultSetModus.getInt("dokumenttyp");
                System.out.println("modus: " + modus);
                resultSetModus.close();
                statement.execute("UPDATE unternehmen SET bearbeiten = null");
            }else{
                ResultSet resultsetRechnungsnummer = statement.executeQuery("SELECT * FROM unternehmen");
                resultsetRechnungsnummer.next();
                rechnungsnummer = resultsetRechnungsnummer.getInt("rechnungsnummer");
                resultsetRechnungsnummer.close();
                ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE rechnungsnummer = bestellung_id");
                resultSetModus.next();
                modus = resultSetModus.getInt("dokumenttyp");
                System.out.println(modus);
                resultSetModus.close();
            }
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

    private int column = 0;
    private int row = 0;
    private int prefHeight = 220;
    private int prefHeightPerColumn = 220;

    private LinkedList<Table> linkedlistTabelleBestellungmR = new LinkedList<>();
    private LinkedList<Table> linkedlistTabelleBestellungoR = new LinkedList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (modus == 1){
            dokumentAbschliessen.setText("Rechnung erstellen");
        } else if (modus == 2) {
            dokumentAbschliessen.setText("Verkauf abschliessen");
        }

        try {
            ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikelId) FROM artikel");
            resultsetLaenge.next();
            int laenge = resultsetLaenge.getInt(1);

            resultsetLaenge.close();
            System.out.println("länge: " + laenge);

            for (int i = 0; i < laenge; i++) {
                ResultSet resultsetArtikelId = statement.executeQuery("SELECT * FROM artikel");
                int id = 0;
                if(resultsetArtikelId.absolute(i+1)){
                    id = resultsetArtikelId.getInt("artikelId");
                }
                resultsetArtikelId.close();
                ResultSet resultsetArtikel = statement.executeQuery("SELECT * FROM artikel, einheiten WHERE einheitId = einheit_Id AND artikelId = " + id);
                resultsetArtikel.next();
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

                Label labelTitelArtikelnummer = new Label ("Artikelnr.:");
                Label labelTitelName = new Label ("Name:");
                Label labelTitelPreis = new Label ("Preis:");
                Label labelArtikelnummer = new Label(resultsetArtikel.getString("artikelId"));

                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                Label labelName = new Label(resultsetArtikel.getString("name"));
                Label labelPreis = new Label(resultsetArtikel.getString("preis"));

                paneArtikel.setPrefSize(170,220);
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

                ImageView imageView = null;
                if (resultsetArtikel.getString("dateityp") == null){
                    String imagePath = "src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Artikel\\Artikel.png";
                    Image image = new Image(new FileInputStream(imagePath));
                    imageView = new ImageView();
                    imageView.setImage(image);
                }else{
                    String imagePath = "src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Artikel\\" + resultsetArtikel.getInt("artikelId") + "\\" + resultsetArtikel.getInt("bildnummer") + "." + resultsetArtikel.getString("dateityp");
                    Image image = new Image(new FileInputStream(imagePath));
                    imageView = new ImageView();
                    imageView.setImage(image);
                }

                paneArtikel.getChildren().add(imageView);
                imageView.setLayoutX(54);
                imageView.setLayoutY(14);
                imageView.setFitWidth(65);
                imageView.setFitHeight(65);

                paneArtikel.getChildren().add(buttonMinus);
                paneArtikel.getChildren().add(buttonPlus);
                //TODO x und y festlegen
                buttonMinus.setLayoutX(20);
                buttonMinus.setLayoutY(152);
                buttonMinus.setId(resultsetArtikel.getString("artikelId"));
                buttonPlus.setLayoutX(70);
                buttonPlus.setLayoutY(152);
                buttonPlus.setId(resultsetArtikel.getString("artikelId"));


                JFXTextField textFieldAnzahl = new JFXTextField("0");
                paneArtikel.getChildren().add(textFieldAnzahl);
                textFieldAnzahl.setLayoutX(50);
                textFieldAnzahl.setLayoutY(152);
                textFieldAnzahl.setId(resultsetArtikel.getString("artikelId"));
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

                resultsetArtikel.close();

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
                            int anzahl = Integer.parseInt(textFieldAnzahl.getText());;
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

                textFieldAnzahl.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        try {
                            textFieldAnzahl.setText(textFieldAnzahl.getText().replaceAll("[^0-9]", ""));
                            textFieldAnzahl.positionCaret(textFieldAnzahl.getLength());
                            int lagerbestand = 0;
                            ResultSet resultSet = statement.executeQuery("SELECT lagerbestand FROM artikel WHERE artikelId = " + buttonPlus.getId());
                            if (resultSet.next()){
                                lagerbestand = resultSet.getInt("lagerbestand");
                            }
                            int anzahl = 0;
                            if (!textFieldAnzahl.getText().isEmpty()){
                                anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            }

                            if(anzahl > lagerbestand){
                                textFieldAnzahl.setText(String.valueOf(lagerbestand));
                                anzahl = lagerbestand;
                            }else{
                                textFieldAnzahl.setText(String.valueOf(anzahl));
                            }
                            resultSet.close();
                            paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + buttonPlus.getId()), anzahl);

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                ResultSet resultsetAnzahl = statement.executeQuery("SELECT COUNT(*) FROM bestellung, unternehmen WHERE artikel_Id = " + id + " AND rechnungsnummer = bestellungId");
                resultsetAnzahl.next();
                if (resultsetAnzahl.getInt(1) != 0){
                    resultsetAnzahl.close();
                    ResultSet resultsetAnzahl1 = statement.executeQuery("SELECT * FROM bestellung, unternehmen WHERE artikel_Id = " + id + " AND rechnungsnummer = bestellungId");
                    resultsetAnzahl1.next();
                    int anzahl = resultsetAnzahl1.getInt("anzahl");
                    textFieldAnzahl.setText(String.valueOf(anzahl));
                    resultsetAnzahl1.close();
                    paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + id), anzahl);

                }else{
                    resultsetAnzahl.close();
                }
                resultsetAnzahl.close();
                //TODO überarbeiten wenn ohne Kunde fortfahren

                ResultSet resultSetExistiertKunde = statement.executeQuery("SELECT * FROM bearbeiter, unternehmen WHERE bestellung_id = rechnungsnummer");
                resultSetExistiertKunde.next();
                if (resultSetExistiertKunde.getString("kunden_Id") ==  null) {
                    labelAdressatKundennummer.setText("kein Kunde ausgewählt");
                    labelAdressatNachname.setVisible(false);
                    labelAdressatVorname.setVisible(false);
                    labelAdressatAdresse.setVisible(false);
                    resultSetExistiertKunde.close();
                }else{
                    resultSetExistiertKunde.close();
                    ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden, bearbeiter, unternehmen WHERE bestellung_id = rechnungsnummer AND kunden_id = kundenId");
                    resultSetKunde.next();
                    labelAdressatKundennummer.setText(resultSetKunde.getString("kundenId"));
                    labelAdressatNachname.setText(resultSetKunde.getString("nachname"));
                    labelAdressatVorname.setText(resultSetKunde.getString("vorname"));
                    labelAdressatAdresse.setText(resultSetKunde.getString("adresse"));
                    resultSetKunde.close();
                }
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
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }













    private void paneFuerWarenkorb(ResultSet resultSet, int anzahl) {
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
        try {
            if (resultSet.next()) {
                id = resultSet.getInt("artikelId");
                artikelnummer = resultSet.getInt("artikelId");
                rabatt = resultSet.getDouble("rabatt");
            }
            resultSet.close();
            ResultSet resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
            if (resultSetRechnungsnummer.next()) {
                rechnungsnr = resultSetRechnungsnummer.getInt("rechnungsnummer");
            }
            resultSetRechnungsnummer.close();
            ResultSet resultSetUeberpruefen = statement.executeQuery("SELECT COUNT(artikelId) AS summe FROM artikel, bestellung WHERE artikel_id = " + id);
            resultSetUeberpruefen.next();

            if (anzahl == 0) {
                statement.execute("DELETE FROM bestellung WHERE artikel_id = " + id);
            } else {
                //statement.execute("UPDATE bestellung SET anzahl = " + anzahl + " WHERE artikel_id = " + id);
                if (resultSetUeberpruefen.getInt("summe") == 0) {
                    statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, rabatt) VALUES (" + rechnungsnr + "," + artikelnummer + "," + anzahl + "," + rabatt + ")");
                } else {
                    statement.execute("UPDATE bestellung SET anzahl = " + anzahl + " WHERE artikel_id = " + id);
                }
                //statement.execute("DELETE FROM bestellung WHERE artikel_id = " + id );
                //statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, rabatt) VALUES (" + rechnungsnr+ "," + artikelnummer + "," + anzahl + "," + rabatt + ")");
            }
            resultSetUeberpruefen.close();


            ResultSet resultSetWarenkorb = statement.executeQuery("SELECT * FROM unternehmen, bestellung, artikel WHERE rechnungsnummer = bestellungId AND artikelId = artikel_id");
            while (resultSetWarenkorb.next()) {
                Pane paneWarenkorb = new Pane();
                paneWarenkorb.setPrefSize(200, 82);

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
                gridpaneWarenkorb.addRow(counter + 1, paneWarenkorb);
                gridpaneWarenkorb.setStyle("-fx-border-color: #FFFFFF");

            }
            resultSetWarenkorb.close();
            ResultSet resultSetSumme = statement.executeQuery("SELECT SUM(anzahl * preis) AS summe FROM bestellung, unternehmen, artikel WHERE rechnungsnummer = bestellungId AND artikelId = artikel_id");
            if (resultSetSumme.next()) {
                labelTotal.setText(String.valueOf(resultSetSumme.getDouble("summe")));
                labelTotal.setTextAlignment(TextAlignment.LEFT);
            }

            resultSetSumme.close();
            ResultSet resultSetRabatt = statement.executeQuery("SELECT * FROM artikel, bestellung, unternehmen WHERE rechnungsnummer = bestellungId AND artikelId = artikel_id");
            double rabattTotal = 0;
            while (resultSetRabatt.next()) {
                int rabattArtikel = resultSetRabatt.getInt("bestellung.rabatt");
                double preisArtikel = resultSetRabatt.getDouble("preis");
                int anzahlArtikel = resultSetRabatt.getInt("anzahl");
                rabattTotal = (preisArtikel / 100 * (rabattArtikel)) * anzahlArtikel;
            }
            double gerundet = Math.round(rabattTotal * 20.0) / 20.0;

            labelRabatte.setText("-" + gerundet);
            labelRabatte.setTextAlignment(TextAlignment.LEFT);
            resultSetRabatt.close();

            labelSumme.setText(String.valueOf(Double.parseDouble(labelTotal.getText()) + Double.parseDouble(labelRabatte.getText())));
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
        layout1(1);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("new Information-Dialog");
        alert.setContentText("Die Rechnung wurde erfolgreich erstellt");
        alert.showAndWait();

    }
    private void modusQuittung() {
        Alert barOderKarteAlert = new Alert(Alert.AlertType.NONE);
        barOderKarteAlert.setTitle("Zahlungsmethode");
        barOderKarteAlert.setHeaderText("Wie wird bezahlt?");
        CheckBox checkBox = new CheckBox("Quittung als PDF erstellen");
        ButtonType buttonType = new ButtonType("abbrechen");
        barOderKarteAlert.getButtonTypes().add(buttonType);

        Button bar = new Button("Bar");
        bar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected()){
                    layout1(2);
                }
                barOderKarteAlert.close();
                Alert zahlen = new Alert(Alert.AlertType.INFORMATION);
                zahlen.setTitle("Zahlen-Alert");
                zahlen.setHeaderText("Hier steht der Text");

                TextField textField = new TextField();
                textField.setPromptText("0");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 20, 20, 20));

                for (int i = 1; i <= 9; i++) {
                    Button button = new Button(Integer.toString(i));
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            textField.appendText(button.getText());
                        }
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

                Button okButton = new Button(".");
                okButton.setOnAction(e -> {
                    textField.appendText(".");
                });
                grid.add(okButton, 2, 3);


                VBox content = new VBox(textField, grid);
                zahlen.getDialogPane().setContent(content);

                ButtonType ok = new ButtonType("OK");
                Optional<ButtonType> optional = zahlen.showAndWait();
                double result = 0;
                if (optional.isPresent() && optional.get() == ok) {
                    // Ergebnis des Dialogs abrufen
                    result = Double.parseDouble(textField.getText());
                }
                zahlen.showAndWait();

                Alert betrag = new Alert(Alert.AlertType.INFORMATION);
                betrag.setTitle("Barzahlung");
                double rückgeld;
                rückgeld = result - Double.parseDouble(labelTotal.getText());
                betrag.setHeaderText("Total: " + labelTotal.getText() + "\ngegeben: " + result + "\nzurück: " + rückgeld);
                betrag.showAndWait();

            }
        });
        Button karte = new Button("Karte");
        karte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected()){
                    layout1(2);
                }
                barOderKarteAlert.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kartenzahlung");
                alert.setHeaderText("Der folgende Betrag: " + labelTotal.getText() + " CHF ist zu bezahlen");
                alert.showAndWait();
            }
        });

        bar.setPrefSize(80, 60);
        karte.setPrefSize(80, 60);
        HBox hBox = new HBox(bar, karte);
        VBox vBox = new VBox(hBox, checkBox);
        barOderKarteAlert.getDialogPane().setContent(vBox);
        //barOderKarteAlert.showAndWait();
        Optional<ButtonType> result = barOderKarteAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonType) {
            barOderKarteAlert.close();
        }

    }


    private void layout1(int rechnung1Quittung2) {

        //Grössen der Zellen
        float ganzeseite = 570F;
        float absender1 = 100F;
        float absender2 = 223F;
        float adressat = 143F;

        //Bestellung
        float bezeichnungmR = 207F; //mR = mit Rabatt
        float bezeichnungoR = 267F; //oR = ohne Rabatt
        float artikelnummer = 79F;
        float menge = 79F;
        float preis = 70F;
        float rabatt = 60;
        float gesamt = 75F;

        //Grössen für Tabelle
        float ganzeseite1[] = {ganzeseite};
        float absenderAdressat[] = {absender1, absender2, adressat};
        float bestellungmR[] = {bezeichnungmR, artikelnummer, menge, preis, rabatt, gesamt};
        float bestellungoR[] = {bezeichnungoR, artikelnummer, menge, preis, gesamt};

        //Tabellen
        Table absatz = new Table(ganzeseite1);
        Table tabelleAbsenderAdressat = new Table(absenderAdressat);
        Table tabelleTitelBestellungoR = new Table(bestellungoR);
        Table tabelleTitelBestellungmR = new Table(bestellungmR);

        //Absatz
        absatz.addCell(new Cell().add(new ListItem("\n")).setBorder(Border.NO_BORDER));

        //Zeit
        LocalDateTime datum = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy"); //EEEE für Wochentag
        DateTimeFormatter formatterPfad = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        //Pdf Definieren
        PdfWriter writer = null;
        try {
            ResultSet resultSetAdresse = statement.executeQuery("SELECT * FROM unternehmen, kunden, bearbeiter WHERE rechnungsnummer = bestellung_id AND kundenId = kunden_id");
            resultSetAdresse.next();
            if (rechnung1Quittung2 == 1) {
                writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Rechnungen\\" + formatterPfad.format(datum) + "_" + resultSetAdresse.getInt("rechnungsnummer") + ".pdf");
            }else if(rechnung1Quittung2 == 2){
                writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Quittungen\\" + formatterPfad.format(datum) + "_" + resultSetAdresse.getInt("rechnungsnummer") + ".pdf");

            }

            resultSetAdresse.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        PdfDocument rechnung = new PdfDocument(writer);
        rechnung.setDefaultPageSize(PageSize.A4);
        Document document = new Document(rechnung);

        //Bilder
        //TODO ResultSet Pfad Logo
        ImageData datenLogo = null;
        try {
            datenLogo = ImageDataFactory.create("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\Benutzer\\Unternehmen\\Logo.png");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(datenLogo);
        logo.setHeight(60F);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(logo);
        document.add(absatz).add(absatz);

        /*ImageData datenQRCode = null;
        try {
            datenQRCode = ImageDataFactory.create(getPfadQRCode());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image QRCode = new Image(datenQRCode);*/


        try {
            ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden, bearbeiter, unternehmen WHERE bestellung_id = rechnungsnummer and kundenId = kunden_id");
            resultSetKunde.next();
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Datum")).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(formatter.format(datum))).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getString("nachname") + " " + resultSetKunde.getString("vorname")))).setBorder(Border.NO_BORDER));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Kundennummer")).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(String.valueOf(resultSetKunde.getInt("kundenId")))).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getString("adresse"))).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Verkäufer")).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER)));
            //tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem( + " " + mitarbeiter.getVorname())).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getInt("postleitzahl") + " " + resultSetKunde.getString("ort"))).setBorder(Border.NO_BORDER)));
            document.add(tabelleAbsenderAdressat);
            document.add(absatz);


            //TODO Quittung
            //Paragraph Rechnungsnummer
            Paragraph paragraphQuittungRechnungsnummer = null;
            if (rechnung1Quittung2 == 1){
                paragraphQuittungRechnungsnummer = new Paragraph("Rechnung " + resultSetKunde.getInt("rechnungsnummer")).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            } else if (rechnung1Quittung2 == 2) {
                paragraphQuittungRechnungsnummer = new Paragraph("Quittung " + resultSetKunde.getInt("rechnungsnummer")).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            }
            document.add(paragraphQuittungRechnungsnummer);
            document.add(absatz);

            resultSetKunde.close();

            //Bestellung
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Bezeichnung")).setBorder(Border.NO_BORDER).setBold());
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Artikel-NR")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Anzahl")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Preis")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Rabatt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Gesamt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Bezeichnung")).setBorder(Border.NO_BORDER).setBold());
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Artikel-NR")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Anzahl")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Preis")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Gesamt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));


            ResultSet resultSetBestellung = statement.executeQuery("SELECT * FROM unternehmen, bestellung, artikel, einheiten WHERE rechnungsnummer = bestellungId AND artikel_id = artikelId AND einheitId = einheit_id");

            double totalArtikel;
            double uebertrag = 0;
            double preisInklRabatt;
            double rabatt1;

            int seitenZaehler = 0;
            int zeilenZaehler = 0;
            boolean rabatttester = false;
            linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
            linkedlistTabelleBestellungoR.add(new Table(bestellungoR));


            while (resultSetBestellung.next()) {
                if (resultSetBestellung.getDouble("rabatt") != 0) {
                    rabatt1 = 100 - resultSetBestellung.getDouble("rabatt");
                    preisInklRabatt = resultSetBestellung.getDouble("preis") / 100 * rabatt1;
                    totalArtikel = preisInklRabatt * resultSetBestellung.getInt("anzahl");
                } else {
                    totalArtikel = resultSetBestellung.getDouble("preis") * resultSetBestellung.getInt("anzahl");
                }
                totalArtikel = Math.round(20.00 * totalArtikel) / 20.00; //TODO Zwei Kommastellen
                uebertrag = uebertrag + totalArtikel; //TODO Überprüfen

                if (seitenZaehler == 0) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    if (resultSetBestellung.getDouble("rabatt") == 0) {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

                    } else {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("rabatt")) + "%")).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                        rabatttester = true;
                    }
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    zeilenZaehler = zeilenZaehler + 1;
                    if (zeilenZaehler == 16) {
                        seitenZaehler = 1;
                        linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
                        linkedlistTabelleBestellungoR.add(new Table(bestellungoR));
                    }
                }
                if (seitenZaehler == 1) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    if (resultSetBestellung.getDouble("rabatt") == 0) {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

                    } else {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getDouble("rabatt") + "%")).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                        rabatttester = true;
                    }
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                    if (zeilenZaehler == 32) {
                        seitenZaehler = seitenZaehler + 1;
                        zeilenZaehler = 0;
                        linkedlistTabelleBestellungmR.addLast(new Table(bestellungmR));
                        linkedlistTabelleBestellungoR.addLast(new Table(bestellungoR));
                    }
                }
            }
            resultSetBestellung.close();
            if (rabatttester == true) {
                for (int i = 0; i < seitenZaehler + 1; i++) {
                    document.add(tabelleTitelBestellungmR);
                    document.add(absatz);
                    document.add(linkedlistTabelleBestellungmR.get(i));
                }
                seitenZaehler = 0;
                rabatttester = false;
            } else {
                for (int i = 0; i < seitenZaehler + 1; i++) {
                    document.add(tabelleTitelBestellungoR);
                    document.add(absatz);
                    document.add(linkedlistTabelleBestellungoR.get(i));
                }
                seitenZaehler = 0;
            }
            document.close();
            System.out.println("pdf generated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
