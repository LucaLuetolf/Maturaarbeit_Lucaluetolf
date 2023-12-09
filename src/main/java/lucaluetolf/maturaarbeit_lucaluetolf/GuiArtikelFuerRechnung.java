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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuiArtikelFuerRechnung extends GuiTaskleiste implements Initializable {
    private int modus;
    private int rechnungsnummer;
    private boolean bearbeiten = false;

    {
        try {
            ResultSet resultsetBearbeiten = statement.executeQuery("SELECT COUNT(bearbeiten) FROM unternehmen");
            resultsetBearbeiten.next();
            int exists = resultsetBearbeiten.getInt(1);
            resultsetBearbeiten.close();
            if (exists == 1) {
                bearbeiten = true;
                ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE bearbeiten = bestellung_id");
                resultSetModus.next();
                rechnungsnummer = resultSetModus.getInt("bearbeiten");
                modus = resultSetModus.getInt("dokumenttyp");
                resultSetModus.close();
            } else {
                ResultSet resultSetModus = statement.executeQuery("SELECT * FROM unternehmen, bearbeiter WHERE rechnungsnummer = bestellung_id");
                resultSetModus.next();
                rechnungsnummer = resultSetModus.getInt("rechnungsnummer");
                modus = resultSetModus.getInt("dokumenttyp");
                resultSetModus.close();
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    @FXML
    private GridPane gridpaneArtikel;
    @FXML
    private GridPane gridpaneWarenkorb;
    @FXML
    private Label labelAdressatKundennummer;
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
    private final int prefHeightPerColumn = 220;

    private final LinkedList<Table> linkedlistTabelleBestellungmR = new LinkedList<>();
    private final LinkedList<Table> linkedlistTabelleBestellungoR = new LinkedList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (modus == 1) {
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
            ResultSet resultsetAbgezogen = statement.executeQuery("SELECT COUNT(artikel_id) FROM bestellung WHERE bestellungId = 0");
            resultsetAbgezogen.next();
            int schonAbgezogen = resultsetAbgezogen.getInt(1);
            resultsetAbgezogen.close();
            if (bearbeiten && schonAbgezogen == 0) {
                ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikel_Id) FROM bestellung WHERE bestellungId = " + rechnungsnummer);
                resultsetLaenge.next();
                int laenge = resultsetLaenge.getInt(1);
                resultsetLaenge.close();
                ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                resultsetKunde.next();
                int kunden_id = resultsetKunde.getInt("kunden_id");
                Date date = resultsetKunde.getDate("datum");
                resultsetKunde.close();
                if (kunden_id == 0){
                    statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp, datum) VALUES (0," + modus + ",'" + date + "')");
                } else {
                    statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id, dokumenttyp, datum) VALUES (0, " + kunden_id + "," + modus + ",'" + date + "')");
                }
                for (int i = 0; i < laenge; i++) {
                    int artikelId = 0;
                    int anzahl = 0;
                    String name = "";
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
                    ResultSet resultSetLagerbestand = statement.executeQuery("SELECT * FROM artikel WHERE artikelid = " + artikelId);
                    resultSetLagerbestand.next();
                    int lagerbestand = resultSetLagerbestand.getInt("lagerbestand");
                    resultSetLagerbestand.close();
                    ResultSet resultSetVerkaufteStueck = statement.executeQuery("SELECT * FROM verkaufteStueck WHERE artikel_id = " + artikelId + " AND datum = '" + date + "'");
                    resultSetVerkaufteStueck.next();
                    int verkaufteStueck = resultSetVerkaufteStueck.getInt("anzahl");
                    resultSetVerkaufteStueck.close();
                    int neuerLagerbestand = lagerbestand + anzahl;
                    int neuVerkaufteStueck = verkaufteStueck - anzahl;
                    statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, name_bestellung, preis_bestellung, menge_bestellung, einheit_id_bestellung, rabatt_bestellung) VALUES (0," + artikelId + "," + anzahl + ",'" + name + "'," + preis + "," + menge + "," + einheitId + "," + rabatt + ")");
                    statement.execute("UPDATE artikel SET lagerbestand = " + neuerLagerbestand + " WHERE artikelId = " + artikelId);
                    statement.execute("UPDATE verkaufteStueck SET anzahl = " + neuVerkaufteStueck + " WHERE artikel_Id = " + artikelId + " AND datum = '" + date + "'");
                }
            }

            int laenge = 0;
            if (bearbeiten){
                ResultSet resultSetDatum = statement.executeQuery("SELECT * FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                resultSetDatum.next();
                Date datum = resultSetDatum.getDate("datum");
                resultSetDatum.close();
                ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikel_Id) FROM verkaufteStueck WHERE datum = '" + datum + "'");
                resultsetLaenge.next();
                laenge = resultsetLaenge.getInt(1);
                resultsetLaenge.close();
            }
            else {
                ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikelId) FROM artikel");
                resultsetLaenge.next();
                laenge = resultsetLaenge.getInt(1);
                resultsetLaenge.close();
            }
            //Anzahl der Artikel:



            for (int i = 0; i < laenge; i++) {

                //Artikelnummer für den jeweiligen Schleifen durchlauf
                int artikelId = 0;
                ResultSet resultsetArtikelId = null;
                if (bearbeiten){
                    ResultSet resultSetDatum = statement.executeQuery("SELECT * FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                    resultSetDatum.next();
                    Date datum = resultSetDatum.getDate("datum");
                    resultSetDatum.close();
                    resultsetArtikelId = statement.executeQuery("SELECT * FROM verkaufteStueck WHERE datum = '" + datum + "'");
                    if (resultsetArtikelId.absolute(i + 1)) {
                        artikelId = resultsetArtikelId.getInt("artikel_Id");
                    }
                }
                else{
                    resultsetArtikelId = statement.executeQuery("SELECT * FROM artikel");
                    if (resultsetArtikelId.absolute(i + 1)) {
                        artikelId = resultsetArtikelId.getInt("artikelId");
                    }
                }

                resultsetArtikelId.close();
                ResultSet resultsetArtikel = statement.executeQuery("SELECT * FROM artikel, einheiten WHERE einheitId = einheit_Id AND artikelId = " + artikelId);
                resultsetArtikel.next();

                //Festlegen, an welcher Position das Pane ist
                if (column == 5) {
                    row = row + 1;
                    gridpaneArtikel.addColumn(row);
                    prefHeight = prefHeight + prefHeightPerColumn;
                    gridpaneArtikel.setPrefSize(850, prefHeight);
                    column = 0;
                }

                //Pane für die Anzeige der Artikel
                Pane paneArtikel = new Pane();
                paneArtikel.setPrefSize(170, 220);
                paneArtikel.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                gridpaneArtikel.add(paneArtikel, column, row);
                column++;

                Label labelTitelArtikelnummer = new Label("Artikelnr.:");
                labelTitelArtikelnummer.setLayoutX(14);
                labelTitelArtikelnummer.setLayoutY(88);
                labelTitelArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelArtikelnummer);

                Label labelTitelName = new Label("Name:");
                labelTitelName.setLayoutX(14);
                labelTitelName.setLayoutY(105);
                labelTitelName.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelName);

                Label labelTitelPreis = new Label("Preis:");
                labelTitelPreis.setLayoutX(14);
                labelTitelPreis.setLayoutY(122);
                labelTitelPreis.setStyle("-fx-text-fill: #FFFFFF ");
                paneArtikel.getChildren().add(labelTitelPreis);

                Label labelTitelMenge = new Label("Inhalt: ");
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
                if (resultsetArtikel.getString("dateityp") == null) {
                    Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Artikel/Artikel.png")));
                    imageView = new ImageView();
                    imageView.setImage(image);
                } else {
                    Image image = new Image(new FileInputStream("Bilder/Benutzer/Artikel/" + resultsetArtikel.getInt("artikelId") + "/" + resultsetArtikel.getInt("bildnummer") + "." + resultsetArtikel.getString("dateityp")));
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

                paneArtikel.getChildren().add(stackPaneImage);

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
                buttonPlus.setPrefSize(25, 25);
                buttonPlus.setId(String.valueOf(artikelId));
                buttonPlus.setStyle("-fx-border-radius: 15px; -fx-text-fill: #ba8759; -fx-background-color: #FFFFFF");
                paneArtikel.getChildren().add(buttonPlus);

                TextField textFieldAnzahl = new TextField("0");
                textFieldAnzahl.setAlignment(Pos.CENTER);
                textFieldAnzahl.setLayoutX(60);
                textFieldAnzahl.setLayoutY(162);
                textFieldAnzahl.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #E8CFB0");
                textFieldAnzahl.setPrefSize(50, 25);
                textFieldAnzahl.setId(String.valueOf(artikelId));
                paneArtikel.getChildren().add(textFieldAnzahl);

                resultsetArtikel.close();

                ResultSet resultsetArtikelUeberpruefenObVerwendet = statement.executeQuery("SELECT COUNT(*) FROM bestellung WHERE bestellungId = " + rechnungsnummer + " AND artikel_Id = " + artikelId);
                resultsetArtikelUeberpruefenObVerwendet.next();
                if (resultsetArtikelUeberpruefenObVerwendet.getInt(1) == 1) {
                    resultsetArtikelUeberpruefenObVerwendet.close();
                    ResultSet resultsetArtikelAngabenBestellung = statement.executeQuery("SELECT * FROM bestellung, einheiten WHERE einheitId = einheit_id_bestellung AND bestellungId = " + rechnungsnummer + " AND artikel_Id = " + artikelId);
                    resultsetArtikelAngabenBestellung.next();
                    labelName.setText(resultsetArtikelAngabenBestellung.getString("name_bestellung"));
                    labelPreis.setText(String.valueOf(resultsetArtikelAngabenBestellung.getDouble("preis_bestellung")));
                    labelMenge.setText(resultsetArtikelAngabenBestellung.getDouble("menge_bestellung") + " " + resultsetArtikelAngabenBestellung.getString("abkuerzung"));
                    resultsetArtikelAngabenBestellung.close();
                    ResultSet resultsetAnzahl = statement.executeQuery("SELECT * FROM bestellung WHERE artikel_Id = " + artikelId + " AND " + rechnungsnummer + " = bestellungId");
                    resultsetAnzahl.next();
                    textFieldAnzahl.setText(String.valueOf(resultsetAnzahl.getInt("anzahl")));
                    paneFuerWarenkorb(artikelId, resultsetAnzahl.getInt("anzahl"));
                    resultsetAnzahl.close();
                } else {
                    resultsetArtikelUeberpruefenObVerwendet.close();
                }

                buttonMinus.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int anzahl = Integer.parseInt(textFieldAnzahl.getText());
                        if (anzahl != 0) {
                            anzahl = anzahl - 1;
                        }
                        textFieldAnzahl.setText(String.valueOf(anzahl));
                        paneFuerWarenkorb(Integer.parseInt(buttonPlus.getId()), anzahl);

                    }
                });

                buttonPlus.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int lagerbestand = 0;
                        try {
                            ResultSet resultSet = statement.executeQuery("SELECT lagerbestand FROM artikel WHERE artikelId = " + buttonPlus.getId());
                            if (resultSet.next()) {
                                lagerbestand = resultSet.getInt("lagerbestand");
                            }
                            int anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            if (anzahl < lagerbestand) {
                                anzahl = anzahl + 1;
                            }
                            resultSet.close();
                            textFieldAnzahl.setText(String.valueOf(anzahl));
                            paneFuerWarenkorb(Integer.parseInt(buttonPlus.getId()), anzahl);

                        } catch (Exception e) {
                            AllgemeineMethoden.fehlermeldung(e);
                        }
                    }
                });

                textFieldAnzahl.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        int lagerbestand = 0;
                        try {
                            ResultSet resultSet = statement.executeQuery("SELECT lagerbestand FROM artikel WHERE artikelId = " + buttonPlus.getId());
                            if (resultSet.next()) {
                                lagerbestand = resultSet.getInt("lagerbestand");
                            }
                            resultSet.close();
                        } catch (SQLException e) {
                            AllgemeineMethoden.fehlermeldung(e);
                        }
                        textFieldAnzahl.setText(textFieldAnzahl.getText().replaceAll("[^0-9]", ""));
                        int anzahl = 0;
                        if (textFieldAnzahl.getText() == ""){
                            textFieldAnzahl.setText("0");
                        } else {
                            anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            paneFuerWarenkorb(Integer.parseInt(buttonPlus.getId()), 0);
                        }
                        if (anzahl > lagerbestand) {
                            textFieldAnzahl.setText(String.valueOf(lagerbestand));
                        }
                        if (!textFieldAnzahl.getText().isEmpty()) {
                            anzahl = Integer.parseInt(textFieldAnzahl.getText());
                            paneFuerWarenkorb(Integer.parseInt(buttonPlus.getId()), anzahl);
                        }
                        textFieldAnzahl.positionCaret(textFieldAnzahl.getLength());
                    }
                });

                ResultSet resultSetExistiertKunde = statement.executeQuery("SELECT COUNT(kunden_id) FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                resultSetExistiertKunde.next();
                if (resultSetExistiertKunde.getInt(1) == 0) {
                    resultSetExistiertKunde.close();
                    labelAdressatKundennummer.setText("kein Kunde ausgewählt");
                    labelAdressatAdresse.setVisible(false);
                } else {
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
                        } catch (Exception e) {
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

    private void paneFuerWarenkorb(int artikelId, int anzahl) {
        gridpaneWarenkorb.getChildren().clear();
        try {
            ResultSet resultsetArtikelExistiert = statement.executeQuery("SELECT COUNT(*) FROM bestellung WHERE artikel_id = " + artikelId + " AND bestellungId = " + rechnungsnummer);
            boolean artikelExistiert = false;
            resultsetArtikelExistiert.next();
            if (resultsetArtikelExistiert.getInt(1) == 1) {
                artikelExistiert = true;
            }
            resultsetArtikelExistiert.close();
            if (artikelExistiert) {
                if (anzahl == 0) {
                    if (bearbeiten){
                        statement.execute("UPDATE bestellung SET anzahl = 0 WHERE artikel_id = " + artikelId + " AND bestellungId = " + rechnungsnummer);
                    }else{
                        statement.execute("DELETE FROM bestellung WHERE artikel_id = " + artikelId);
                    }
                } else {
                    statement.execute("UPDATE bestellung SET anzahl = " + anzahl + " WHERE artikel_id = " + artikelId + " AND bestellungId = " + rechnungsnummer);
                }
            } else {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT * FROM artikel WHERE artikelId = " + artikelId);
                resultsetArtikel.next();
                statement.execute("INSERT INTO bestellung (bestellungId, artikel_id, anzahl, name_bestellung, preis_bestellung, menge_bestellung, einheit_id_bestellung, rabatt_bestellung) VALUES (" + rechnungsnummer + ", " + artikelId + "," + anzahl + ",'" + resultsetArtikel.getString("name") + "'," + resultsetArtikel.getDouble("preis") + ", " + resultsetArtikel.getDouble("menge") + ", " + resultsetArtikel.getInt("einheit_id") + ", " + resultsetArtikel.getInt("rabatt") + ")");
                resultsetArtikel.close();
            }
            int prefHeightWarenkorb = 82;
            int rowWarenkorb = 0;

            ResultSet resultsetWarenkorb = statement.executeQuery("SELECT * FROM bestellung WHERE anzahl != 0 AND bestellungId = " + rechnungsnummer);
            while (resultsetWarenkorb.next()) {
                if (resultsetWarenkorb.getInt("anzahl") != 0){
                    gridpaneWarenkorb.setStyle("-fx-border-color: #FFFFFF");

                    rowWarenkorb = rowWarenkorb + 1;
                    prefHeightWarenkorb = prefHeightWarenkorb + 82;
                    gridpaneWarenkorb.setPrefSize(200, prefHeightWarenkorb);

                    Pane paneWarenkorb = new Pane();
                    paneWarenkorb.setPrefSize(200, 82);
                    paneWarenkorb.setStyle("-fx-background-color: #E8CFB0; -fx-background-radius: 20px; -fx-border-color: #FFFFFF; -fx-border-radius: 20px");
                    gridpaneWarenkorb.addRow(rowWarenkorb, paneWarenkorb);

                    Label labelWarenkorbArtikelnummer = new Label(resultsetWarenkorb.getString("artikel_Id"));
                    labelWarenkorbArtikelnummer.setLayoutX(11);
                    labelWarenkorbArtikelnummer.setLayoutY(16);
                    //labelWarenkorbArtikelnummer.setMaxWidth(60);
                    labelWarenkorbArtikelnummer.setStyle("-fx-text-fill: #FFFFFF ");
                    paneWarenkorb.getChildren().add(labelWarenkorbArtikelnummer);

                    Label labelWarenkorbName = new Label(resultsetWarenkorb.getString("name_bestellung"));
                    labelWarenkorbName.setLayoutX(71);
                    labelWarenkorbName.setLayoutY(16);
                    labelWarenkorbName.setMaxWidth(130);
                    labelWarenkorbName.setStyle("-fx-text-fill: #FFFFFF ");
                    paneWarenkorb.getChildren().add(labelWarenkorbName);

                    Label labelWarenkorbPreis = new Label(resultsetWarenkorb.getString("preis_bestellung"));
                    labelWarenkorbPreis.setLayoutX(11);
                    labelWarenkorbPreis.setLayoutY(49);
                    //labelWarenkorbPreis.setMaxWidth(55);
                    labelWarenkorbPreis.setStyle("-fx-text-fill: #FFFFFF ");
                    paneWarenkorb.getChildren().add(labelWarenkorbPreis);

                    Label labelWarenkorbAnzahl = new Label(String.valueOf(resultsetWarenkorb.getInt("anzahl")));
                    labelWarenkorbAnzahl.setLayoutX(71);
                    labelWarenkorbAnzahl.setLayoutY(49);
                    //labelWarenkorbAnzahl.setMaxWidth(20);
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

            }
            resultsetWarenkorb.close();

            ResultSet resultsetSumme = statement.executeQuery("SELECT SUM(anzahl * preis_bestellung) FROM bestellung WHERE " + rechnungsnummer + " = bestellungId");
            resultsetSumme.next();
            double total = resultsetSumme.getDouble(1);
            double totalGerundet = Math.round(total * 20.0) / 20.0;
            labelTotal.setText(String.valueOf(totalGerundet));
            labelTotal.setTextAlignment(TextAlignment.LEFT);
            resultsetSumme.close();

            ResultSet resultsetRabatt = statement.executeQuery("SELECT * FROM bestellung WHERE bestellungId = " + rechnungsnummer);
            double rabattTotal = 0;
            while (resultsetRabatt.next()) {
                int rabattArtikel = resultsetRabatt.getInt("rabatt_bestellung");
                double preisArtikel = resultsetRabatt.getDouble("preis_bestellung");
                int anzahlArtikel = resultsetRabatt.getInt("anzahl");
                rabattTotal = rabattTotal + (preisArtikel / 100 * (rabattArtikel)) * anzahlArtikel;
            }
            double rabattGerundet = Math.round(rabattTotal * 20.0) / 20.0;

            labelRabatte.setText("-" + rabattGerundet);
            labelRabatte.setTextAlignment(TextAlignment.LEFT);
            resultsetRabatt.close();

            labelSumme.setText(String.valueOf(Math.round((Double.parseDouble(labelTotal.getText()) + Double.parseDouble(labelRabatte.getText())) * 20.0) / 20.0));
            labelSumme.setTextAlignment(TextAlignment.RIGHT);

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    @FXML
    protected void buttonRechnungErstellen(ActionEvent event) {
        try {
            ResultSet resultsetBestellung = statement.executeQuery("SELECT COUNT(artikel_id) FROM bestellung WHERE bestellungId = " + rechnungsnummer);
            resultsetBestellung.next();
            int exists = resultsetBestellung.getInt(1);
            resultsetBestellung.close();
            if (exists == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fehlermeldung");
                alert.setContentText("Es wurden keine Artikel ausgewählt");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        if (modus == 1) {
            layout1(1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("new Information-Dialog");
            alert.setContentText("Die Rechnung wurde erfolgreich erstellt");
            alert.showAndWait();
        } else if (modus == 2) {
            layout1(2);
            Alert barOderKarteAlert = new Alert(Alert.AlertType.NONE);
            barOderKarteAlert.setTitle("Zahlungsmethode");
            barOderKarteAlert.setHeaderText("Wie wird bezahlt?");
            ButtonType buttonTypeAbbrechenBarKarte = new ButtonType("abbrechen");
            barOderKarteAlert.getButtonTypes().add(buttonTypeAbbrechenBarKarte);

            Button bar = new Button("Bar");
            bar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    barKarte = 1;
                    barOderKarteAlert.close();
                }
            });
            Button karte = new Button("Karte");
            karte.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    barKarte = 2;
                    barOderKarteAlert.close();
                }
            });

            bar.setPrefSize(80, 60);
            karte.setPrefSize(80, 60);
            HBox hBox = new HBox(bar, karte);
            barOderKarteAlert.getDialogPane().setContent(hBox);

            Optional<ButtonType> result = barOderKarteAlert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeAbbrechenBarKarte) {
                return;
            }
            if (barKarte == 1) {
                barZahlung();
            } else if (barKarte == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kartenzahlung");
                alert.setHeaderText("Der folgende Betrag ist zu bezahlen: CHF " + labelSumme.getText());
                alert.showAndWait();
            }

        }
        try {
            LocalDate datum = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (!bearbeiten){
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
            }

            ResultSet resultsetLaenge = statement.executeQuery("SELECT COUNT(artikel_Id) FROM bestellung, bearbeiter WHERE bestellungId = " + rechnungsnummer + " AND bestellung_id = " + rechnungsnummer);
            resultsetLaenge.next();
            int laenge = resultsetLaenge.getInt(1);
            resultsetLaenge.close();

            for (int i = 0; i < laenge; i++) {

                int artikelId = 0;
                int anzahl = 0;
                Date datumBearbeiten = null;
                ResultSet resultsetArtikelIdUndAnzahl = statement.executeQuery("SELECT * FROM bestellung, bearbeiter WHERE bestellungId = " + rechnungsnummer + " AND bestellung_id = " + rechnungsnummer);
                if (resultsetArtikelIdUndAnzahl.absolute(i + 1)) {
                    artikelId = resultsetArtikelIdUndAnzahl.getInt("artikel_Id");
                    anzahl = resultsetArtikelIdUndAnzahl.getInt("anzahl");
                    datumBearbeiten = resultsetArtikelIdUndAnzahl.getDate("datum");
                }
                resultsetArtikelIdUndAnzahl.close();
                statement.execute("UPDATE artikel SET lagerbestand = lagerbestand - " + anzahl + " WHERE artikelId = " + artikelId);
                if (!bearbeiten){
                    statement.execute("UPDATE verkaufteStueck SET anzahl = anzahl + " + anzahl + " WHERE artikel_id = " + artikelId + " AND datum = '" + formatter.format(datum) + "'");
                } else {
                    statement.execute("UPDATE verkaufteStueck SET anzahl = anzahl + " + anzahl + " WHERE artikel_id = " + artikelId + " AND datum = '" + datumBearbeiten + "'");
                }
            }
            if (bearbeiten) {
                statement.execute("DELETE FROM bestellung WHERE anzahl = 0");
                statement.execute("UPDATE unternehmen SET bearbeiten = null");
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = 0");
                statement.execute("DELETE FROM bestellung WHERE bestellungId = 0");

            } else {
                statement.execute("UPDATE unternehmen SET rechnungsnummer=" + (rechnungsnummer + 1) + "WHERE rechnungsnummer=" + rechnungsnummer);
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        try {
            root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event1 -> {
            stage.close();
        });
    }

    public void barZahlung(){
        Alert zahlen = new Alert(Alert.AlertType.INFORMATION);
        zahlen.setTitle("Zahlen-Alert");
        zahlen.setHeaderText("Bar erhalten: ");

        TextField textField = new TextField();
        textField.setText("0");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 1; i <= 9; i++) {
            Button button = new Button(Integer.toString(i));
            button.setPrefSize(60, 60);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (textField.getText().contains("0") && textField.getLength() == 1) {
                        textField.setText(button.getText());
                    } else {
                        textField.appendText(button.getText());
                    }

                }
            });

            grid.add(button, (i - 1) % 3, (i - 1) / 3);
        }


        Button zeroButton = new Button("0");
        zeroButton.setPrefSize(60, 60);

        zeroButton.setOnAction(e -> {
            textField.appendText("0");
        });
        grid.add(zeroButton, 1, 3);

        Button punktButton = new Button(".");
        punktButton.setPrefSize(60, 60);
        punktButton.setOnAction(e -> {
            punktButton.setDisable(true);
            textField.appendText(".");
        });
        grid.add(punktButton, 2, 3);

        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(60, 60);

        clearButton.setOnAction(e -> {
            punktButton.setDisable(false);
            textField.setText("0");
        });
        grid.add(clearButton, 0, 3);

        VBox content = new VBox(textField, grid);
        zahlen.getDialogPane().setContent(content);

        /*ButtonType ok = new ButtonType("OK");
        ButtonType abbrechen = new ButtonType("abbrechen");
        zahlen.getButtonTypes().setAll(ok, abbrechen);*/
        zahlen.showAndWait();
        if (Double.parseDouble(textField.getText()) >= Double.parseDouble(labelSumme.getText())) {
            Alert rueckgeld = new Alert(Alert.AlertType.INFORMATION);
            rueckgeld.setTitle("Bargeldzahlung");
            rueckgeld.setHeaderText("Abschluss:");
            rueckgeld.setContentText("Total:" + labelSumme.getText() + "\nBar erhalten: " + Double.parseDouble(textField.getText()) + "\nRückgeld: " + Math.round(20.00 * (Double.parseDouble(textField.getText()) - Double.parseDouble(labelSumme.getText())))/20.00);
            rueckgeld.showAndWait();
        } else {
            Alert zuwenigRueckgeld = new Alert(Alert.AlertType.INFORMATION);
            zuwenigRueckgeld.setHeaderText("Fehlermeldung");
            zuwenigRueckgeld.setContentText("Zu wenig Geld gegeben");
            zuwenigRueckgeld.showAndWait();
            barZahlung();
        }

        /*Optional<ButtonType> optional = zahlen.showAndWait();
        if (optional.isPresent()) {
            if (Double.parseDouble(textField.getText()) >= Double.parseDouble(labelSumme.getText())) {
                Alert rueckgeld = new Alert(Alert.AlertType.INFORMATION);
                rueckgeld.setTitle("Bargeldzahlung");
                rueckgeld.setHeaderText("Abschluss:");
                rueckgeld.setContentText("Total:" + labelSumme.getText() + "\nBar erhalten: " + Double.parseDouble(textField.getText()) + "\nRückgeld: " + Math.round(20.00 * (Double.parseDouble(textField.getText()) - Double.parseDouble(labelSumme.getText())))/20.00);
                rueckgeld.showAndWait();
            } else {
                Alert zuwenigRueckgeld = new Alert(Alert.AlertType.INFORMATION);
                zuwenigRueckgeld.setHeaderText("Fehlermeldung");
                zuwenigRueckgeld.setContentText("Zu wenig Geld gegeben");
                ButtonType buttonTypeOkZuWenigRueckgeld = new ButtonType("OK");
                ButtonType buttonTypeAbbrechenZuWenigRueckgeld = new ButtonType("abbrechen");
                zuwenigRueckgeld.getButtonTypes().setAll(buttonTypeOkZuWenigRueckgeld, buttonTypeAbbrechenZuWenigRueckgeld);
                Optional<ButtonType> optional1 = zuwenigRueckgeld.showAndWait();
                barZahlung();
                if (optional1.isPresent() && optional1.get() == buttonTypeOkZuWenigRueckgeld) {
                    barZahlung();
                } else {
                    return;
                }
            }
        } else {
            return;
        }*/
    }

    private void layout1(int rechnung1Quittung2) {

        //Grössen der Zellen
        float ganzeseite = 570F;
        float absender1 = 100F;
        float absender2 = 223F;
        float adressat = 143F;

        //BestellungmR
        float bezeichnungmR = 245F; //mR = mit Rabatt
        float artikelnummermR = 60F;
        float einheitmR = 55F;
        float mengemR = 45F;
        float preismR = 40F;
        float rabattmR = 30;
        float gesamtmR = 50F;

        //BestellungoR
        float bezeichnungoR = 250F; //oR = ohne Rabatt
        float artikelnummeroR = 60F;
        float einheitoR = 70F;
        float mengeoR = 45F;
        float preisoR = 40F;
        float gesamtoR = 60F;

        //Grössen für Tabelle
        float[] ganzeseite1 = {ganzeseite};
        float[] absenderAdressat = {absender1, absender2, adressat};
        float[] bestellungmR = {artikelnummermR, bezeichnungmR, einheitmR, mengemR, preismR, rabattmR, gesamtmR};
        float[] bestellungoR = {artikelnummeroR, bezeichnungoR, einheitoR, mengeoR, preisoR, gesamtoR};

        //Tabellen
        Table absatz = new Table(ganzeseite1);
        Table tabelleAbsenderAdressat = new Table(absenderAdressat);
        Table tabelleTitelBestellungoR = new Table(bestellungoR);
        Table tabelleTitelBestellungmR = new Table(bestellungmR);

        Table tabelleBankdaten = new Table(ganzeseite1);
        Table tabelleTotal = new Table(ganzeseite1);

        //Absatz
        absatz.addCell(new Cell().add(new ListItem("\n")).setBorder(Border.NO_BORDER));

        //Zeit
        LocalDateTime datum = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy");
        DateTimeFormatter formatterPfad = DateTimeFormatter.ofPattern("yyyy.MM.dd");


        //PDF definieren
        PdfWriter writer = null;
        try {
            if (bearbeiten) {
                if (modus == 1){
                    ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter, kunden WHERE kunden_id = kundenId AND bestellung_id = " + rechnungsnummer);
                    resultsetKunde.next();
                    Date date = resultsetKunde.getDate("datum");
                    int i = 0;
                    boolean ueberpruefen = true;
                    while (ueberpruefen) {
                        i++;
                        File alteRechnung = new File("Kundendateien/" + resultsetKunde.getInt("kundenId") + ", " + resultsetKunde.getString("nachname") + " " + resultsetKunde.getString("vorname") + "/Rechnungen/" + formatterPfad.format(date.toLocalDate()) + "_" + rechnungsnummer + "_" + i + ".pdf");
                        if (!alteRechnung.exists()) {
                            ueberpruefen = false;
                        }
                    }
                    writer = new PdfWriter("Kundendateien/" + resultsetKunde.getInt("kundenId") + ", " + resultsetKunde.getString("nachname") + " " + resultsetKunde.getString("vorname") + "/Rechnungen/" + formatterPfad.format(date.toLocalDate()) + "_" + rechnungsnummer + "_" + i + ".pdf");
                } else if (modus == 2) {
                    ResultSet resultsetKundeExistiert = statement.executeQuery("SELECT COUNT(kunden_Id) FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                    resultsetKundeExistiert.next();
                    int exists = resultsetKundeExistiert.getInt(1);
                    resultsetKundeExistiert.close();

                    if(exists == 0){
                        ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                        resultsetKunde.next();
                        Date date = resultsetKunde.getDate("datum");
                        int i = 0;
                        boolean ueberpruefen = true;
                        while (ueberpruefen) {
                            i++;
                            File alteRechnung = new File("Kundendateien/übrige Quittungen/" + formatterPfad.format(date.toLocalDate()) + "_" + rechnungsnummer + "_" + i + ".pdf");
                            if (!alteRechnung.exists()) {
                                ueberpruefen = false;
                            }
                        }
                        writer = new PdfWriter("Kundendateien/übrige Quittungen/"  + formatterPfad.format(date.toLocalDate()) + "_" + rechnungsnummer + "_" + i + ".pdf");
                        resultsetKunde.close();
                    } else {
                        ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter, kunden WHERE kunden_id = kundenId AND bestellung_id = " + rechnungsnummer);
                        resultsetKunde.next();
                        Date date = resultsetKunde.getDate("datum");
                        int i = 0;
                        boolean ueberpruefen = true;
                        while (ueberpruefen) {
                            i++;
                            File alteRechnung = new File("Kundendateien/" + resultsetKunde.getInt("kundenId") + ", " + resultsetKunde.getString("nachname") + " " + resultsetKunde.getString("vorname") + "/Quittungen/" + formatterPfad.format(date.toLocalDate()) + "_" + rechnungsnummer + "_" + i + ".pdf");
                            if (!alteRechnung.exists()) {
                                ueberpruefen = false;
                            }
                            writer = new PdfWriter("Kundendateien/" + resultsetKunde.getInt("kundenId") + ", " + resultsetKunde.getString("nachname") + " " + resultsetKunde.getString("vorname") + "/Quittungen/" + formatterPfad.format(date.toLocalDate()) + "_" + rechnungsnummer + "_" + i + ".pdf");
                        }
                        resultsetKunde.close();
                    }

                }
            } else {
                if (modus == 1) {
                    ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter, kunden WHERE kunden_id = kundenId AND bestellung_id = " + rechnungsnummer);
                    resultsetKunde.next();
                    writer = new PdfWriter("Kundendateien/" + resultsetKunde.getInt("kundenId") + ", " + resultsetKunde.getString("nachname") + " " + resultsetKunde.getString("vorname") + "/Rechnungen/" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");
                    resultsetKunde.close();
                } else if (modus == 2) {
                    ResultSet resultsetKundeExistiert = statement.executeQuery("SELECT COUNT(kunden_Id) FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                    resultsetKundeExistiert.next();
                    int exists = resultsetKundeExistiert.getInt(1);
                    resultsetKundeExistiert.close();
                    ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter, kunden WHERE kunden_id = kundenId AND bestellung_id = " + rechnungsnummer);
                    resultsetKunde.next();
                    if (exists == 0){
                        writer = new PdfWriter("Kundendateien/übrige Quittungen/" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");
                    } else {
                        writer = new PdfWriter("Kundendateien/" + resultsetKunde.getInt("kundenId") + ", " + resultsetKunde.getString("nachname") + " " + resultsetKunde.getString("vorname") + "/Quittungen/" + formatterPfad.format(datum) + "_" + rechnungsnummer + ".pdf");
                    }
                    resultsetKunde.close();
                }
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        PdfDocument rechnung = new PdfDocument(writer);
        rechnung.setDefaultPageSize(PageSize.A4);
        Document document = new Document(rechnung);

        //Bankdaten
        try {
            if (rechnung1Quittung2 == 1){
                /*ResultSet resultSetBankdatenExists = statement.executeQuery("SELECT COUNT(IBAN) FROM unternehmen");
                resultSetBankdatenExists.next();
                int exists = resultSetBankdatenExists.getInt(1);
                resultSetBankdatenExists.close();*/
                ResultSet resultSetBankdatenExists = statement.executeQuery("SELECT IBAN FROM unternehmen");
                resultSetBankdatenExists.next();
                String iban = resultSetBankdatenExists.getString("IBAN");
                resultSetBankdatenExists.close();
                if (iban == "") {
                    tabelleBankdaten.addCell((new Cell().add(new ListItem("keine Bankdaten vorhanden")).setBorder(Border.NO_BORDER).setFontSize(10F)));
                    tabelleBankdaten.addCell((new Cell().add(new ListItem("Zahlbar innert 30 Tagen")).setBorder(Border.NO_BORDER).setFontSize(10F)));

                } else {
                    ResultSet resultSetBankdaten = statement.executeQuery("SELECT * FROM unternehmen");
                    resultSetBankdaten.next();
                    tabelleBankdaten.addCell((new Cell().add(new ListItem("Bank: " + resultSetBankdaten.getString("bank"))).setBorder(Border.NO_BORDER).setFontSize(10F)));
                    tabelleBankdaten.addCell((new Cell().add(new ListItem("IBAN: " + resultSetBankdaten.getString("iban"))).setBorder(Border.NO_BORDER).setFontSize(10F)));
                    tabelleBankdaten.addCell((new Cell().add(new ListItem("Zahlbar innert 30 Tagen")).setBorder(Border.NO_BORDER).setFontSize(10F)));
                    tabelleBankdaten.addCell((new Cell().add(new ListItem("Wir danken Ihnen für Ihren Auftrag")).setBorder(Border.NO_BORDER).setFontSize(10F)));
                    resultSetBankdaten.close();
                }
            } else if (rechnung1Quittung2 == 2) {
                tabelleBankdaten.addCell((new Cell().add(new ListItem("Betrag dankend erhalten")).setBorder(Border.NO_BORDER).setFontSize(10F)));
            }

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        //Bilder
        ImageData datenLogo = null;
        try {
            ResultSet resultsetUnternehmen = statement.executeQuery("SELECT COUNT(bildnummer) FROM unternehmen");
            resultsetUnternehmen.next();
            int exists = resultsetUnternehmen.getInt(1);
            resultsetUnternehmen.close();
            if (exists == 0) {
                datenLogo = ImageDataFactory.create(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Unternehmen/Logo.png")));
            } else {
                ResultSet resultsetLogo = statement.executeQuery("SELECT * FROM unternehmen");
                resultsetLogo.next();
                int bildnummer = resultsetLogo.getInt("bildnummer");
                String dateityp = resultsetLogo.getString("Dateityp");
                datenLogo = ImageDataFactory.create("Bilder/Benutzer/Unternehmen/" + bildnummer + "." + dateityp);
                resultsetLogo.close();
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(datenLogo);
        logo.setHeight(60F);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(logo);
        document.add(absatz).add(absatz);

        try {
            ResultSet resultsetExistiertKunde = statement.executeQuery("SELECT COUNT(kunden_id) FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
            resultsetExistiertKunde.next();
            int exists = resultsetExistiertKunde.getInt(1);
            resultsetExistiertKunde.close();
            if (exists == 1) {
                ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden, bearbeiter, Unternehmen WHERE bestellung_id = " + rechnungsnummer + " and kundenId = kunden_id");
                resultSetKunde.next();
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("Unternehmen")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(resultSetKunde.getString("unternehmensname"))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(resultSetKunde.getString("vorname") + " " + resultSetKunde.getString("nachname"))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("Datum")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(formatter.format(datum))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(resultSetKunde.getString("adresse"))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("Kundennummer")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(String.valueOf(resultSetKunde.getInt("kundenId")))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(resultSetKunde.getInt("postleitzahl") + " " + resultSetKunde.getString("ort"))).setBorder(Border.NO_BORDER).setFontSize(10F));
                resultSetKunde.close();
            } else {
                ResultSet resultsetUnternehmensname = statement.executeQuery("SELECT * FROM unternehmen");
                resultsetUnternehmensname.next();
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("Unternehmen")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(resultsetUnternehmensname.getString("unternehmensname"))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("Datum")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem(formatter.format(datum))).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setFontSize(10F));
                tabelleAbsenderAdressat.addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setFontSize(10F));
                resultsetUnternehmensname.close();
            }

            document.add(tabelleAbsenderAdressat);
            document.add(absatz);

            //Paragraph Rechnungsnummer
            Paragraph paragraphQuittungRechnungsnummer = null;
            if (rechnung1Quittung2 == 1) {
                paragraphQuittungRechnungsnummer = new Paragraph("Rechnung " + rechnungsnummer).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            } else if (rechnung1Quittung2 == 2) {
                paragraphQuittungRechnungsnummer = new Paragraph("Quittung " + rechnungsnummer).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            }
            document.add(paragraphQuittungRechnungsnummer);
            document.add(absatz);

            //Bestellung
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Nr.")).setBorder(Border.NO_BORDER).setBold());
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Bezeichnung")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Inhalt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Anz.")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Preis")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("%")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Betrag")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Nr.")).setBorder(Border.NO_BORDER).setBold());
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Bezeichnung")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Inhalt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Anz.")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Preis")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Betrag")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));


            ResultSet resultSetBestellung = statement.executeQuery("SELECT * FROM bestellung, einheiten WHERE " + rechnungsnummer + " = bestellungId AND einheitId = einheit_id_bestellung ORDER BY artikel_id");

            double totalArtikel;
            double uebertrag = 0;
            double preisInklRabatt;
            double rabatt1;

            int seitenZaehler = 0;
            int zeilenZaehler = 0;
            boolean rabatttester = false;
            linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
            linkedlistTabelleBestellungoR.add(new Table(bestellungoR));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            while (resultSetBestellung.next()) {

                if (resultSetBestellung.getDouble("rabatt_bestellung") != 0) {
                    rabatt1 = 100 - resultSetBestellung.getDouble("rabatt_bestellung");
                    preisInklRabatt = resultSetBestellung.getDouble("preis_bestellung") / 100 * rabatt1;
                    totalArtikel = preisInklRabatt * resultSetBestellung.getInt("anzahl");
                } else {
                    totalArtikel = resultSetBestellung.getDouble("preis_bestellung") * resultSetBestellung.getInt("anzahl");
                }
                totalArtikel = Math.round(20.00 * totalArtikel) / 20.00;
                uebertrag = uebertrag + totalArtikel;
                boolean booleanSeitenuebergang = false;
                if (seitenZaehler == 0) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikel_Id")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name_bestellung"))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("menge_bestellung") + " " + resultSetBestellung.getString("abkuerzung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(resultSetBestellung.getDouble("preis_bestellung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    if (resultSetBestellung.getDouble("rabatt_bestellung") == 0) {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem("")).setBorder(Border.NO_BORDER).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));

                    } else {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("rabatt_bestellung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                        rabatttester = true;
                    }
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));

                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikel_Id")))).setBorder(Border.NO_BORDER).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name_bestellung"))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("menge_bestellung") + " " + resultSetBestellung.getString("abkuerzung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(resultSetBestellung.getDouble("preis_bestellung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    zeilenZaehler = zeilenZaehler + 1;
                    if (zeilenZaehler == 22) {
                        seitenZaehler = 1;
                        booleanSeitenuebergang = true;
                        linkedlistTabelleBestellungmR.addLast(new Table(bestellungmR));
                        linkedlistTabelleBestellungoR.addLast(new Table(bestellungoR));
                    }
                }
                if (seitenZaehler >= 1 && !booleanSeitenuebergang) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name_bestellung"))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("menge_bestellung") + " " + resultSetBestellung.getString("abkuerzung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(resultSetBestellung.getDouble("preis_bestellung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    if (resultSetBestellung.getDouble("rabatt_bestellung") == 0) {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));

                    } else {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("rabatt_bestellung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                        rabatttester = true;
                    }
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));

                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name_bestellung"))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("menge_bestellung") + " " + resultSetBestellung.getString("abkuerzung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(resultSetBestellung.getDouble("preis_bestellung")))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(decimalFormat.format(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT).setFontSize(10F));
                    if (zeilenZaehler == 33) {
                        seitenZaehler = seitenZaehler + 1;
                        zeilenZaehler = 0;

                        linkedlistTabelleBestellungmR.addLast(new Table(bestellungmR));
                        linkedlistTabelleBestellungoR.addLast(new Table(bestellungoR));
                    }
                }
            }
            resultSetBestellung.close();

            if (rabatttester) {
                for (int i = 0; i < seitenZaehler +1; i++) {
                    if (i == 0) {
                        document.add(tabelleTitelBestellungmR);
                        document.add(absatz);
                        document.add(linkedlistTabelleBestellungmR.get(0));
                    } else {
                        document.add(tabelleTitelBestellungmR);
                        document.add(absatz);
                        document.add(linkedlistTabelleBestellungmR.get(i));
                    }
                }

            } else {
                for (int i = 0; i < seitenZaehler+1; i++) {
                    if (i == 0) {
                        document.add(tabelleTitelBestellungoR);
                        document.add(absatz);
                        document.add(linkedlistTabelleBestellungoR.get(0));
                    } else {
                        document.add(tabelleTitelBestellungoR);
                        document.add(absatz);
                        document.add(linkedlistTabelleBestellungoR.get(i));
                    }
                }
            }
            document.add(absatz);
            tabelleTotal.addCell(new Cell().add(new ListItem("Total CHF: " + decimalFormat.format(uebertrag))).setBorder(Border.NO_BORDER).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            document.add(tabelleTotal);
            document.add(tabelleBankdaten);
            document.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }


    }
}
