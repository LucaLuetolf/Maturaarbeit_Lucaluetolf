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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuiArtikelFuerRechnung3 extends GuiTaskleiste implements Initializable {
    Statement statement;
    Connection connection;
    private int modus;
    private int rechnungsnummer;
    private boolean bearbeiten = false;

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
                bearbeiten = true;
                ResultSet resultsetRechnungsnummer = statement.executeQuery("SELECT * FROM unternehmen");
                resultsetRechnungsnummer.next();
                rechnungsnummer = resultsetRechnungsnummer.getInt("bearbeiten");
                resultsetRechnungsnummer.close();
                ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE bearbeiten = bestellung_id");
                resultSetModus.next();
                modus = resultSetModus.getInt("dokumenttyp");
                resultSetModus.close();
            }else{
                ResultSet resultsetRechnungsnummer = statement.executeQuery("SELECT * FROM unternehmen");
                resultsetRechnungsnummer.next();
                rechnungsnummer = resultsetRechnungsnummer.getInt("rechnungsnummer");
                resultsetRechnungsnummer.close();
                ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE rechnungsnummer = bestellung_id");
                resultSetModus.next();
                modus = resultSetModus.getInt("dokumenttyp");
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

    private int barKarte;

    private int column = 0;
    private int row = 0;
    private int prefHeight = 220;
    private int prefHeightPerColumn = 220;

    private LinkedList<Table> linkedlistTabelleBestellungmR = new LinkedList<>();
    private LinkedList<Table> linkedlistTabelleBestellungoR = new LinkedList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Text festlegen für den Button dokument abschliessen
        if (modus == 1){
            dokumentAbschliessen.setText("Rechnung erstellen");
        } else if (modus == 2) {
            dokumentAbschliessen.setText("Verkauf abschliessen");
        }

        try {
            ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");
            int orange;
            resultSetUnternehmen.next();
            orange = resultSetUnternehmen.getInt("lagerbestandOrange");
            resultSetUnternehmen.close();
            if (bearbeiten == true) {
                //statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, name_bestellung, preis_bestellung, menge_bestellung, einheit_id_bestellung, rabatt_bestellung) SELECT 0,artikel_id, anzahl, name_bestellung, preis_bestellung, menge_bestellung, einheit_id_bestellung, rabatt_bestellung FROM bestellung WHERE bestellungId = " + rechnungsnummer);
                //statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id, mitarbeiter_id, dokumenttyp, datum) SELECT 0, kunden_id, mitarbeiter_id, dokumenttyp, datum FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikel_Id) FROM bestellung WHERE bestellungId = " + rechnungsnummer);
                resultsetLaenge.next();
                int laenge = resultsetLaenge.getInt(1);
                resultsetLaenge.close();
                ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                resultsetKunde.next();
                int kunden_id = resultsetKunde.getInt("kunden_id");
                Date date = resultsetKunde.getDate("datum");
                resultsetKunde.close();
                statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id, dokumenttyp, datum) VALUES (0, " + kunden_id + "," + modus + ",'" + date +"')");
                for (int i = 0; i < laenge; i++) {

                    //Artikelnummer für den jeweiligen Schleifen durchlauf
                    int artikelId = 0;
                    int anzahl = 0;
                    String name ="";
                    double preis = 0;
                    double menge = 0;
                    int einheitId = 0;
                    int rabatt = 0;
                    ResultSet resultsetArtikelIdUndAnzahl = statement.executeQuery("SELECT * FROM bestellung WHERE bestellungId = " + rechnungsnummer);
                    if (resultsetArtikelIdUndAnzahl.absolute(i + 1)) {
                        artikelId = resultsetArtikelIdUndAnzahl.getInt("artikel_Id");
                        anzahl = resultsetArtikelIdUndAnzahl.getInt("anzahl");
                        name = resultsetArtikelIdUndAnzahl.getString("name_bestellung");
                        preis = resultsetArtikelIdUndAnzahl.getDouble("preis_bestellung");
                        menge = resultsetArtikelIdUndAnzahl.getDouble("menge_bestellung");
                        einheitId = resultsetArtikelIdUndAnzahl.getInt("einheit_id_bestellung");
                        rabatt = resultsetArtikelIdUndAnzahl.getInt("rabatt_bestellung");

                    }
                    resultsetArtikelIdUndAnzahl.close();
                    statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, name_bestellung, preis_bestellung, menge_bestellung, einheit_id_bestellung, rabatt_bestellung) VALUES (0," + artikelId + "," + anzahl + ",'" + name + "'," + preis + "," + menge + "," + einheitId + "," + rabatt + ")");
                    statement.execute("UPDATE artikel SET lagerbestand = (lagerbestand + " + anzahl + ") WHERE artikelId = " + artikelId);
                    statement.execute("UPDATE verkaufteStueck SET anzahl = anzahl - " + anzahl + " WHERE artikel_Id = " + artikelId + " AND datum = '" + date + "'");

                }
                }

            //Anzahl der Artikel:
            ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikelId) FROM artikel");
            resultsetLaenge.next();
            int laenge = resultsetLaenge.getInt(1);
            resultsetLaenge.close();


            for (int i = 0; i < laenge; i++) {

                //Artikelnummer für den jeweiligen Schleifen durchlauf
                int artikelId = 0;
                ResultSet resultsetArtikelId = statement.executeQuery("SELECT * FROM artikel");
                if(resultsetArtikelId.absolute(i+1)){
                    artikelId = resultsetArtikelId.getInt("artikelId");
                }
                resultsetArtikelId.close();

                ResultSet resultsetArtikel = statement.executeQuery("SELECT * FROM artikel, einheiten WHERE einheitId = einheit_Id AND artikelId = " + artikelId);
                resultsetArtikel.next();

                //Festlegen, an welcher Position das Pane ist
                if(column == 5){
                    row = row + 1;
                    gridpaneArtikel.addColumn(row);
                    prefHeight = prefHeight + prefHeightPerColumn;
                    gridpaneArtikel.setPrefSize(850, prefHeight);
                    column = 0;
                }

                //Pane für die Anzeige der Artikel
                Pane paneArtikel = new Pane();
                paneArtikel.setPrefSize(170,220);
                paneArtikel.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                gridpaneArtikel.add(paneArtikel,column,row);
                column++;

                Label labelTitelArtikelnummer = new Label ("Artikelnr.:");
                labelTitelArtikelnummer.setLayoutX(14);
                labelTitelArtikelnummer.setLayoutY(88);
                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelArtikelnummer);

                Label labelTitelName = new Label ("Name:");
                labelTitelName.setLayoutX(14);
                labelTitelName.setLayoutY(105);
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelName);

                Label labelTitelPreis = new Label ("Preis:");
                labelTitelPreis.setLayoutX(14);
                labelTitelPreis.setLayoutY(122);
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelPreis);

                Label labelTitelMenge = new Label("Menge: ");
                labelTitelMenge.setLayoutX(14);
                labelTitelMenge.setLayoutY(139);
                labelTitelMenge.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelMenge);

                Label labelArtikelnummer = new Label(resultsetArtikel.getString("artikelId"));
                labelArtikelnummer.setLayoutX(76);
                labelArtikelnummer.setLayoutY(88);
                labelArtikelnummer.setMaxWidth(90);
                labelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelArtikelnummer);

                Label labelName = new Label(resultsetArtikel.getString("name"));
                labelName.setLayoutX(76);
                labelName.setLayoutY(105);
                labelName.setMaxWidth(90);
                labelName.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelName);

                Label labelPreis = new Label(resultsetArtikel.getString("preis"));
                labelPreis.setLayoutX(76);
                labelPreis.setLayoutY(122);
                labelPreis.setMaxWidth(90);
                labelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelPreis);

                Label labelMenge = new Label(resultsetArtikel.getInt("menge") + " " + resultsetArtikel.getString("abkuerzung"));
                labelMenge.setLayoutX(76);
                labelMenge.setLayoutY(139);
                labelMenge.setMaxWidth(90);
                labelMenge.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelMenge);

                Line lineLagerbestand = new Line();
                lineLagerbestand.setLayoutX(84);
                lineLagerbestand.setLayoutY(196);
                lineLagerbestand.setStartX(-68.125);
                lineLagerbestand.setEndX(71.625);
                paneArtikel.getChildren().add(lineLagerbestand);

                int bestand = resultsetArtikel.getInt("lagerbestand");

                if (bestand == 0) {
                    lineLagerbestand.setStyle("-fx-stroke: #ff172e; -fx-stroke-width: 2px");
                } else if (bestand <= orange && bestand > 0) {
                    lineLagerbestand.setStyle("-fx-stroke: #ffae5c; -fx-stroke-width: 2px");
                } else {
                    lineLagerbestand.setStyle("-fx-stroke: #00dd76; -fx-stroke-width: 2px");
                }

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

                imageView.setLayoutX(54);
                imageView.setLayoutY(14);
                imageView.setFitWidth(65);
                imageView.setFitHeight(65);
                paneArtikel.getChildren().add(imageView);

                JFXButton buttonMinus = new JFXButton("-");
                buttonMinus.setLayoutX(20);
                buttonMinus.setLayoutY(162);
                buttonMinus.setPrefSize(25, 25);
                buttonMinus.setId(String.valueOf(artikelId));
                buttonMinus.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");
                paneArtikel.getChildren().add(buttonMinus);

                JFXButton buttonPlus = new JFXButton("+");
                buttonPlus.setLayoutX(125);
                buttonPlus.setLayoutY(162);
                buttonPlus.setPrefSize(25,25);
                buttonPlus.setId(String.valueOf(artikelId));
                buttonPlus.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");
                paneArtikel.getChildren().add(buttonPlus);

                TextField textFieldAnzahl = new TextField("0");
                textFieldAnzahl.setAlignment(Pos.CENTER);
                textFieldAnzahl.setLayoutX(60);
                textFieldAnzahl.setLayoutY(162);
                textFieldAnzahl.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #E8CFB0");
                textFieldAnzahl.setPrefSize(50,25);
                textFieldAnzahl.setId(String.valueOf(artikelId));
                paneArtikel.getChildren().add(textFieldAnzahl);

                resultsetArtikel.close();

                ResultSet resultsetArtikelUeberpruefenObVerwendet = statement.executeQuery("SELECT COUNT(*) FROM bestellung WHERE bestellungId = " + rechnungsnummer + " AND artikel_Id = " + artikelId);
                resultsetArtikelUeberpruefenObVerwendet.next();
                if (bearbeiten == true && resultsetArtikelUeberpruefenObVerwendet.getInt(1) == 1){
                    resultsetArtikelUeberpruefenObVerwendet.close();
                    ResultSet resultsetArtikelAngabenBestellung = statement.executeQuery("SELECT * FROM bestellung WHERE bestellungId = " + rechnungsnummer + " AND artikel_Id = " + artikelId);
                    resultsetArtikelAngabenBestellung.next();
                    labelName.setText(resultsetArtikelAngabenBestellung.getString("name_bestellung"));
                    labelPreis.setText(String.valueOf(resultsetArtikelAngabenBestellung.getDouble("preis_bestellung")));
                    resultsetArtikelAngabenBestellung.close();
                    ResultSet resultsetAnzahl = statement.executeQuery("SELECT * FROM bestellung WHERE artikel_Id = " + artikelId + " AND " + rechnungsnummer + " = bestellungId");
                    resultsetAnzahl.next();
                    textFieldAnzahl.setText(String.valueOf(resultsetAnzahl.getInt("anzahl")));
                    paneFuerWarenkorb1(artikelId, resultsetAnzahl.getInt("anzahl"));
                    resultsetAnzahl.close();
                    //paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + artikelId), resultsetAnzahl.getInt("anzahl"));
                } else{
                    resultsetArtikelUeberpruefenObVerwendet.close();
                }

                buttonMinus.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int anzahl = Integer.parseInt(textFieldAnzahl.getText());
                        if (anzahl != 0){
                            anzahl = anzahl -1;
                        }
                        textFieldAnzahl.setText(String.valueOf(anzahl));
                        paneFuerWarenkorb1(Integer.parseInt(buttonPlus.getId()), anzahl);
                        //paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + buttonMinus.getId()), anzahl);
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
                            paneFuerWarenkorb1(Integer.parseInt(buttonPlus.getId()), anzahl);
                            //paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId =" + buttonPlus.getId()), anzahl);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

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
                            paneFuerWarenkorb1(Integer.parseInt(buttonPlus.getId()), anzahl);
                            //paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + textFieldAnzahl.getId()), anzahl);

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
                            paneFuerWarenkorb1(Integer.parseInt(buttonPlus.getId()), anzahl);
                            //paneFuerWarenkorb(statement.executeQuery("SELECT * FROM artikel WHERE artikelId=" + buttonPlus.getId()), anzahl);

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                ResultSet resultSetExistiertKunde = statement.executeQuery("SELECT COUNT(kunden_id) FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                resultSetExistiertKunde.next();
                if (resultSetExistiertKunde.getInt(1) == 0) {
                    resultSetExistiertKunde.close();
                    labelAdressatKundennummer.setText("kein Kunde ausgewählt");
                    labelAdressatAdresse.setVisible(false);
                }else{
                    resultSetExistiertKunde.close();
                    ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden, bearbeiter WHERE bestellung_id = " + rechnungsnummer + " AND kunden_id = kundenId");
                    resultSetKunde.next();
                    labelAdressatKundennummer.setText(resultSetKunde.getString("kundenId") + " " + resultSetKunde.getString("nachname") + " " + resultSetKunde.getString("vorname"));
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

    private void paneFuerWarenkorb1(int artikelId, int anzahl){
        gridpaneWarenkorb.getChildren().clear();
        try {
            ResultSet resultsetArtikelExistiert = statement.executeQuery("SELECT COUNT(*) FROM bestellung WHERE artikel_id = " + artikelId + " AND bestellungId = " + rechnungsnummer);
            boolean artikelExistiert = false;
            resultsetArtikelExistiert.next();
            if (resultsetArtikelExistiert.getInt(1) == 1){
                artikelExistiert = true;
            }
            resultsetArtikelExistiert.close();
            if (artikelExistiert){
                if (anzahl == 0){
                    statement.execute("DELETE FROM bestellung WHERE artikel_id = " + artikelId);
                } else{
                    statement.execute("UPDATE bestellung SET anzahl = " + anzahl + " WHERE artikel_id = " + artikelId + " AND bestellungId = " + rechnungsnummer);
                }
            } else {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT * FROM artikel WHERE artikelId = " + artikelId);
                resultsetArtikel.next();
                statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, name_bestellung, preis_bestellung, menge_bestellung, einheit_id_bestellung, rabatt_bestellung) VALUES (" + rechnungsnummer + ", " + artikelId + "," + anzahl + ",'" + resultsetArtikel.getString("name") + "'," + resultsetArtikel.getDouble("preis") + ", " + resultsetArtikel.getDouble("menge") + ", " + resultsetArtikel.getInt("einheit_id") + ", " + resultsetArtikel.getInt("rabatt") + ")");
                resultsetArtikel.close();
            }

            ResultSet resultsetWarenkorb = statement.executeQuery("SELECT * FROM bestellung WHERE bestellungId = " + rechnungsnummer);
            while (resultsetWarenkorb.next()){
                gridpaneWarenkorb.setStyle("-fx-border-color: #FFFFFF");

                Pane paneWarenkorb = new Pane();
                paneWarenkorb.setPrefSize(200, 82);
                paneWarenkorb.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                gridpaneWarenkorb.addRow(gridpaneWarenkorb.getRowCount()+1, paneWarenkorb);

                Label labelWarenkorbArtikelnummer = new Label(resultsetWarenkorb.getString("artikel_Id"));
                labelWarenkorbArtikelnummer.setLayoutX(11);
                labelWarenkorbArtikelnummer.setLayoutY(16);
                labelWarenkorbArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                paneWarenkorb.getChildren().add(labelWarenkorbArtikelnummer);

                Label labelWarenkorbName = new Label(resultsetWarenkorb.getString("name_bestellung"));
                labelWarenkorbName.setLayoutX(71);
                labelWarenkorbName.setLayoutY(16);
                labelWarenkorbName.setStyle("-fx-text-fill: #FFFFFF ");
                paneWarenkorb.getChildren().add(labelWarenkorbName);

                Label labelWarenkorbPreis = new Label(resultsetWarenkorb.getString("preis_bestellung"));
                labelWarenkorbPreis.setLayoutX(11);
                labelWarenkorbPreis.setLayoutY(49);
                labelWarenkorbPreis.setStyle("-fx-text-fill: #FFFFFF ");
                paneWarenkorb.getChildren().add(labelWarenkorbPreis);

                Label labelWarenkorbAnzahl = new Label(String.valueOf(resultsetWarenkorb.getInt("anzahl")));
                labelWarenkorbAnzahl.setLayoutX(71);
                labelWarenkorbAnzahl.setLayoutY(49);
                labelWarenkorbAnzahl.setStyle("-fx-text-fill: #FFFFFF ");
                paneWarenkorb.getChildren().add(labelWarenkorbAnzahl);

                double warenkorbTotal = resultsetWarenkorb.getInt("anzahl") * resultsetWarenkorb.getDouble("preis_bestellung");
                double warenkorbTotalGerundet = Math.round(warenkorbTotal * 20.0) / 20.0;
                Label labelWarenkorbTotal = new Label(String.valueOf(warenkorbTotalGerundet));
                labelWarenkorbTotal.setLayoutX(151);
                labelWarenkorbTotal.setLayoutY(49);
                labelWarenkorbTotal.setStyle("-fx-text-fill: #FFFFFF ");
                paneWarenkorb.getChildren().add(labelWarenkorbTotal);
            }
            resultsetWarenkorb.close();

            ResultSet resultsetSumme = statement.executeQuery("SELECT SUM(anzahl * preis_bestellung) FROM bestellung WHERE " + rechnungsnummer +" = bestellungId");
            resultsetSumme.next();
            double total = resultsetSumme.getDouble(1);
            double totalGerundet = Math.round(total*20.0) / 20.0;
            labelTotal.setText(String.valueOf(totalGerundet));
            labelTotal.setTextAlignment(TextAlignment.LEFT);
            resultsetSumme.close();

            ResultSet resultsetRabatt = statement.executeQuery("SELECT * FROM bestellung WHERE bestellungId = " + rechnungsnummer);
            double rabattTotal = 0;
            while (resultsetRabatt.next()) {
                int rabattArtikel = resultsetRabatt.getInt("rabatt_bestellung");
                double preisArtikel = resultsetRabatt.getDouble("preis_bestellung");
                int anzahlArtikel = resultsetRabatt.getInt("anzahl");
                rabattTotal = (preisArtikel / 100 * (rabattArtikel)) * anzahlArtikel;
            }
            double rabattGerundet = Math.round(rabattTotal * 20.0) / 20.0;

            labelRabatte.setText("-" + rabattGerundet);
            labelRabatte.setTextAlignment(TextAlignment.LEFT);
            resultsetRabatt.close();

            labelSumme.setText(String.valueOf(Double.parseDouble(labelTotal.getText()) + Double.parseDouble(labelRabatte.getText())));
            labelSumme.setTextAlignment(TextAlignment.RIGHT);

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }
    private void paneFuerWarenkorb(ResultSet resultSet, int anzahl) {
        gridpaneWarenkorb.getChildren().clear();


        Label labelWarenkorbArtikelnummer;
        Label labelWarenkorbName;
        Label labelWarenkorbPreis;
        Label labelWarenkorbAnzahl;
        Label labelWarenkorbTotal;

        int id = 0;
        double rabatt = 0;
        try {
            if (resultSet.next()) {
                id = resultSet.getInt("artikelId");
                rabatt = resultSet.getDouble("rabatt");
            }
            resultSet.close();
            ResultSet resultSetUeberpruefen = statement.executeQuery("SELECT COUNT(artikelId) AS summe FROM artikel, bestellung WHERE artikel_id = " + id);
            resultSetUeberpruefen.next();

            if (anzahl == 0) {
                statement.execute("DELETE FROM bestellung WHERE artikel_id = " + id + " AND bestellungId = " + rechnungsnummer);
            } else {
                //statement.execute("UPDATE bestellung SET anzahl = " + anzahl + " WHERE artikel_id = " + id);
                if (resultSetUeberpruefen.getInt("summe") == 0) {
                    statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, rabatt) VALUES (" + rechnungsnummer + "," + id + "," + anzahl + "," + rabatt + ")");
                } else {
                    statement.execute("UPDATE bestellung SET anzahl = " + anzahl + " WHERE artikel_id = " + id + " AND bestellungId = " + rechnungsnummer);
                }
                //statement.execute("DELETE FROM bestellung WHERE artikel_id = " + id );
                //statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, rabatt) VALUES (" + rechnungsnr+ "," + artikelnummer + "," + anzahl + "," + rabatt + ")");
            }
            resultSetUeberpruefen.close();


            ResultSet resultSetWarenkorb = statement.executeQuery("SELECT * FROM bestellung, artikel WHERE " + rechnungsnummer + " = bestellungId AND artikelId = artikel_id AND anzahl != 0");
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
            ResultSet resultSetSumme = statement.executeQuery("SELECT SUM(anzahl * preis) FROM bestellung, artikel WHERE " + rechnungsnummer +" = bestellungId AND artikelId = artikel_id");
            resultSetSumme.next();
            double summe = resultSetSumme.getDouble(1);
            double gerundeteSumme = Math.round(summe * 20.0)/20.0;
            labelTotal.setText(String.valueOf(resultSetSumme.getDouble("summe")));
            labelTotal.setTextAlignment(TextAlignment.LEFT);

            resultSetSumme.close();
            ResultSet resultSetRabatt = statement.executeQuery("SELECT * FROM artikel, bestellung WHERE " + rechnungsnummer + " = bestellungId AND artikelId = artikel_id");
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
        if (gridpaneWarenkorb.getRowCount() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fehlermeldung");
            alert.setContentText("Es wurden keine Artikel ausgewählt");
            alert.showAndWait();
            return;
        }

        if (modus == 1) {
            layout1(1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("new Information-Dialog");
            alert.setContentText("Die Rechnung wurde erfolgreich erstellt");
            alert.showAndWait();
        } else if (modus == 2) {
            Alert barOderKarteAlert = new Alert(Alert.AlertType.NONE);
            barOderKarteAlert.setTitle("Zahlungsmethode");
            barOderKarteAlert.setHeaderText("Wie wird bezahlt?");
            CheckBox checkBox = new CheckBox("Quittung als PDF erstellen");
            ButtonType buttonTypeAbbrechenBarKarte = new ButtonType("abbrechen");
            barOderKarteAlert.getButtonTypes().add(buttonTypeAbbrechenBarKarte);

            Button bar = new Button("Bar");
            bar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (checkBox.isSelected()) {
                        layout1(2);
                    }
                    barKarte = 1;
                    barOderKarteAlert.close();
                }
            });
            Button karte = new Button("Karte");
            karte.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (checkBox.isSelected()) {
                        layout1(2);
                    }
                    barKarte = 2;
                    barOderKarteAlert.close();

                }
            });

            bar.setPrefSize(80, 60);
            karte.setPrefSize(80, 60);
            HBox hBox = new HBox(bar, karte);
            VBox vBox = new VBox(hBox, checkBox);
            barOderKarteAlert.getDialogPane().setContent(vBox);

            Optional<ButtonType> result = barOderKarteAlert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeAbbrechenBarKarte) {
                return;
            }
            if (barKarte == 1) {
                Alert zahlen = new Alert(Alert.AlertType.INFORMATION);
                zahlen.setTitle("Zahlen-Alert");
                zahlen.setHeaderText("Geben sie den Betrag\ndes gegebenen Geldes ein: ");

                TextField textField = new TextField();
                textField.setText("0");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                for (int i = 1; i <= 9; i++) {
                    Button button = new Button(Integer.toString(i));
                    button.setPrefSize(60,60);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (textField.getText().contains("0") && textField.getLength() == 1){
                                textField.setText(button.getText());
                            }else{
                                textField.appendText(button.getText());
                            }

                        }
                    });

                    grid.add(button, (i - 1) % 3, (i - 1) / 3);
                }



                Button zeroButton = new Button("0");
                zeroButton.setPrefSize(60,60);

                zeroButton.setOnAction(e -> {
                    textField.appendText("0");
                });
                grid.add(zeroButton, 1, 3);

                Button punktButton = new Button(".");
                punktButton.setPrefSize(60,60);
                punktButton.setOnAction(e -> {
                    punktButton.setDisable(true);
                    textField.appendText(".");
                });
                grid.add(punktButton, 2, 3);

                Button clearButton = new Button("Clear");
                clearButton.setPrefSize(60,60);

                clearButton.setOnAction(e -> {
                    punktButton.setDisable(false);
                    textField.setText("0");
                });
                grid.add(clearButton, 0, 3);

                VBox content = new VBox(textField, grid);
                zahlen.getDialogPane().setContent(content);

                ButtonType ok = new ButtonType("OK");
                ButtonType abbrechen = new ButtonType("abbrechen");
                zahlen.getButtonTypes().setAll(ok, abbrechen);
                Optional<ButtonType> optional = zahlen.showAndWait();
                if (optional.isPresent() && optional.get() == ok) {
                    if (Double.parseDouble(textField.getText()) >= Double.parseDouble(labelTotal.getText())){
                        Alert rueckgeld = new Alert(Alert.AlertType.INFORMATION);
                        rueckgeld.setHeaderText("Abschluss:");
                        rueckgeld.setContentText("Total:" + labelTotal.getText() +"\ngegeben: " + Double.parseDouble(textField.getText()) +"\nRückgeld: " + (Double.parseDouble(textField.getText())-Double.parseDouble(labelTotal.getText())));
                        rueckgeld.showAndWait();
                    } else{
                        Alert zuwenigRueckgeld = new Alert(Alert.AlertType.INFORMATION);
                        zuwenigRueckgeld.setHeaderText("Fehlermeldung");
                        zuwenigRueckgeld.setContentText("Zu wenig Geld gegeben");
                        ButtonType buttonTypeOkZuWenigRueckgeld = new ButtonType("ok");
                        ButtonType buttonTypeAbbrechenZuWenigRückgeld = new ButtonType("abbrechen");
                        zuwenigRueckgeld.getButtonTypes().setAll(buttonTypeOkZuWenigRueckgeld, buttonTypeAbbrechenZuWenigRückgeld);
                        Optional<ButtonType> optional1 = zuwenigRueckgeld.showAndWait();
                        if (optional1.isPresent() && optional1.get() == buttonTypeOkZuWenigRueckgeld){
                            textField.setText("0");
                            zahlen.showAndWait();
                        }else{
                            return;
                        }
                    }
                } else {
                    return;
                }

            } else if (barKarte == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kartenzahlung");
                alert.setHeaderText("Der folgende Betrag: " + labelTotal.getText() + " CHF ist zu bezahlen");
                alert.showAndWait();
            }

        }
        try {
            LocalDate datum = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            ResultSet resultsetExists = statement.executeQuery("SELECT COUNT(*) FROM verkaufteStueck WHERE datum = '" + formatter.format(datum) + "'");
            resultsetExists.next();
            int exists = resultsetExists.getInt(1);
            resultsetExists.close();
            if (exists == 0) {
                ResultSet resultsetUebrigeArtikelLaenge = statement.executeQuery("SELECT COUNT(artikelId) FROM artikel");
                resultsetUebrigeArtikelLaenge.next();
                int laenge = resultsetUebrigeArtikelLaenge.getInt(1);
                resultsetUebrigeArtikelLaenge.close();
                for (int i = 0; i < laenge; i++) {
                    ResultSet resultsetArtikelId = statement.executeQuery("SELECT artikelId FROM artikel");
                    resultsetArtikelId.next();
                    if (resultsetArtikelId.absolute(i + 1)) {
                        int artikelId = resultsetArtikelId.getInt("artikelId");
                        resultsetArtikelId.close();
                        statement.execute("INSERT INTO verkaufteStueck (artikel_id, anzahl, datum) VALUES ( " + artikelId + ", 0, '" + formatter.format(datum) + "')");
                    }
                }
            }

            ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikel_Id) FROM bestellung, bearbeiter WHERE bestellungId = " + rechnungsnummer + " AND bestellung_id = " + rechnungsnummer + " AND datum = '" + formatter.format(datum) + "'");
            resultsetLaenge.next();
            int laenge = resultsetLaenge.getInt(1);
            resultsetLaenge.close();

            for (int i = 0; i < laenge; i++) {

                int artikelId = 0;
                int anzahl = 0;
                ResultSet resultsetArtikelIdUndAnzahl = statement.executeQuery("SELECT * FROM bestellung, bearbeiter WHERE bestellungId = " + rechnungsnummer + " AND bestellung_id = " + rechnungsnummer + " AND datum = '" + formatter.format(datum) + "'");
                if (resultsetArtikelIdUndAnzahl.absolute(i + 1)) {
                    artikelId = resultsetArtikelIdUndAnzahl.getInt("artikel_Id");
                    anzahl = resultsetArtikelIdUndAnzahl.getInt("anzahl");
                }
                resultsetArtikelIdUndAnzahl.close();
                statement.execute("UPDATE artikel SET lagerbestand = lagerbestand - " + anzahl + " WHERE artikelId = " + artikelId);
                statement.execute("UPDATE verkaufteStueck SET anzahl = anzahl + " + anzahl + " WHERE artikel_id = " + artikelId + " AND datum = '" + formatter.format(datum) + "'");

            }
            if (bearbeiten == true) {
                statement.execute("UPDATE unternehmen SET bearbeiten = null");
            } else {
                statement.execute("UPDATE unternehmen SET rechnungsnummer=" + (rechnungsnummer + 1) + "WHERE rechnungsnummer=" + rechnungsnummer);
            }
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
            ResultSet resultSetAdresse = statement.executeQuery("SELECT * FROM kunden, bearbeiter WHERE " + rechnungsnummer + " = bestellung_id AND kundenId = kunden_id");
            resultSetAdresse.next();
            if (rechnung1Quittung2 == 1) {
                if (bearbeiten == true) {
                    int i = 0;
                    boolean ueberpruefen = true;
                    while (ueberpruefen == true) {
                        i++;
                        File alteRechnung = new File("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Rechnungen\\" + formatterPfad.format(datum) + "\\" + rechnungsnummer + "." + i + ".pdf");
                        if (alteRechnung.exists() == false) {
                            ueberpruefen = false;
                        }
                    }
                    writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Rechnungen\\" + formatterPfad.format(datum) + "_" + rechnungsnummer + "_" + i + ".pdf");
                } else {
                    writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Rechnungen\\" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");
                }

            }else if(rechnung1Quittung2 == 2){
                File quittungExists = new File("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Quittungen\\" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");
                if (quittungExists.exists()){
                    int i = 0;
                    boolean ueberpruefen = true;
                    while (ueberpruefen == true){
                        i++;
                        File alteQuittung = new File("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Quittungen\\" + formatterPfad.format(datum) + "\\" + rechnungsnummer + "." + i + ".pdf");
                        if (alteQuittung.exists() == false){
                            ueberpruefen = false;
                        }
                    }
                    writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Quittungen\\" + formatterPfad.format(datum) + "_" + rechnungsnummer + "_" + i +".pdf");
                } else{
                    writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Quittungen\\" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");
                }
                writer = new PdfWriter("Kundendateien\\" + resultSetAdresse.getInt("kundenId") + ", " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\Quittungen\\" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");

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
            datenLogo = ImageDataFactory.create("src\\main\\resources\\lucaluetolf\\maturaarbeit_lucaluetolf\\Bilder\\System\\Unternehmen\\Logo.png");
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
            ResultSet resultsetExistiertKunde = statement.executeQuery("SELECT COUNT(kunden_id) FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
            resultsetExistiertKunde.next();
            int exists = resultsetExistiertKunde.getInt(1);
            resultsetExistiertKunde.close();
            if (exists == 1){
                ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden, bearbeiter WHERE bestellung_id = " + rechnungsnummer + " and kundenId = kunden_id");
                resultSetKunde.next();
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Datum")).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(formatter.format(datum))).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getString("nachname") + " " + resultSetKunde.getString("vorname")))).setBorder(Border.NO_BORDER));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Kundennummer")).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(String.valueOf(resultSetKunde.getInt("kundenId")))).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getString("adresse"))).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER)));
                //tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getInt("postleitzahl") + " " + resultSetKunde.getString("ort"))).setBorder(Border.NO_BORDER)));
                resultSetKunde.close();
            } else{
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("Datum").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(formatter.format(datum)).setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("").setBorder(Border.NO_BORDER)));

            }

            document.add(tabelleAbsenderAdressat);
            document.add(absatz);


            //TODO Quittung
            //Paragraph Rechnungsnummer
            Paragraph paragraphQuittungRechnungsnummer = null;
            if (rechnung1Quittung2 == 1){
                paragraphQuittungRechnungsnummer = new Paragraph("Rechnung " + rechnungsnummer).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            } else if (rechnung1Quittung2 == 2) {
                paragraphQuittungRechnungsnummer = new Paragraph("Quittung " + rechnungsnummer).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            }
            document.add(paragraphQuittungRechnungsnummer);
            document.add(absatz);

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


            ResultSet resultSetBestellung = statement.executeQuery("SELECT * FROM bestellung, artikel, einheiten WHERE " + rechnungsnummer + " = bestellungId AND artikel_id = artikelId AND einheitId = einheit_id");

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
