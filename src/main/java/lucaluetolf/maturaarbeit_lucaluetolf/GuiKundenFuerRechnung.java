package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class GuiKundenFuerRechnung extends GuiTaskleiste implements Initializable {
    Statement statement;
    Connection connection;
    private int rechnungsnummer = 0;
    private int modus = 0;
    private boolean bearbeiten = false;

    {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
            ResultSet resultsetBearbeiten = statement.executeQuery("SELECT COUNT(bearbeiten) FROM unternehmen");
            resultsetBearbeiten.next();
            int exists = resultsetBearbeiten.getInt(1);
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
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TableView tableViewKunden;
    @FXML
    private TextField textfieldFilterKundennummer;
    @FXML
    private TextField textfieldFilterNachname;
    @FXML
    private TextField textfieldFilterVorname;
    @FXML
    private TextField textfieldFilterPostleitzahl;
    @FXML
    private JFXButton buttonOhneKunde;
    private String stringResultset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            kundenAnzeigen(statement.executeQuery("SELECT * FROM kunden"));
            if (modus == 1){
                buttonOhneKunde.setVisible(false);
            }
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    @FXML
    protected void textfieldFilterKundennummerKey() {
        textfieldFilterKundennummer.setText(textfieldFilterKundennummer.getText().replaceAll("[^0-9]", ""));
        textfieldFilterKundennummer.positionCaret(textfieldFilterKundennummer.getLength());
    }

    @FXML
    protected void textfieldFilterNachnameKey() {
        textfieldFilterNachname.setText(textfieldFilterNachname.getText().replaceAll("[^A-Z, ^a-z]", ""));
        textfieldFilterNachname.positionCaret(textfieldFilterNachname.getLength());

    }

    @FXML
    protected void textfieldFilterVornameKey() {
        textfieldFilterVorname.setText(textfieldFilterVorname.getText().replaceAll("[^A-Z, ^a-z]", ""));
        textfieldFilterVorname.positionCaret(textfieldFilterVorname.getLength());
    }

    @FXML
    protected void textfieldFilterPostleitzahlKey() {
        textfieldFilterPostleitzahl.setText(textfieldFilterPostleitzahl.getText().replaceAll("[^0-9]", ""));
        textfieldFilterPostleitzahl.positionCaret(textfieldFilterPostleitzahl.getLength());
    }

    @FXML
    protected void kundenRechnungFilterAnwenden() {
        int counter = 0;
        stringResultset = "SELECT * FROM Kunden";
        if (textfieldFilterKundennummer.getText() != "") {
            if (counter == 0) {
                stringResultset = stringResultset + " WHERE";
            }
            if (counter >= 1) {
                stringResultset = stringResultset + " AND";
            }
            counter++;
            stringResultset = stringResultset + " kundenId = " + textfieldFilterKundennummer.getText();
        }
        if (textfieldFilterNachname.getText() != "") {
            if (counter == 0) {
                stringResultset = stringResultset + " WHERE";
            }
            if (counter >= 1) {
                stringResultset = stringResultset + " AND";
            }
            counter++;
            stringResultset = stringResultset + " nachname = '" + textfieldFilterNachname.getText() + "'";
        }
        if (textfieldFilterVorname.getText() != "") {
            if (counter == 0) {
                stringResultset = stringResultset + " WHERE";
            }
            if (counter >= 1) {
                stringResultset = stringResultset + " AND";
            }
            counter++;
            stringResultset = stringResultset + " vorname = '" + textfieldFilterVorname.getText() + "'";
        }
        if (textfieldFilterPostleitzahl.getText() != "") {
            if (counter == 0) {
                stringResultset = stringResultset + " WHERE";
            }
            if (counter >= 1) {
                stringResultset = stringResultset + " AND";
            }
            counter++;
            stringResultset = stringResultset + " postleitzahl = " + textfieldFilterPostleitzahl.getText();
        }
        stringResultset = stringResultset + ";";
        try {
            kundenAnzeigen(statement.executeQuery(stringResultset));
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }
    @FXML
    protected void kundenRechnungFilterZuruecksetzten(){
        textfieldFilterKundennummer.setText("");
        textfieldFilterNachname.setText("");
        textfieldFilterVorname.setText("");
        textfieldFilterPostleitzahl.setText("");
        try {
            kundenAnzeigen(statement.executeQuery("SELECT * FROM kunden"));
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    private void kundenAnzeigen(ResultSet resultSet) {
        tableViewKunden.getColumns().clear();
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList();

        TableColumn<ObservableList<String>, String> spalteKundennummer = new TableColumn<>("Kundennr.");
        TableColumn<ObservableList<String>, String> spalteNachname = new TableColumn<>("Nachname");
        TableColumn<ObservableList<String>, String> spalteVorname = new TableColumn<>("Vorname");
        TableColumn<ObservableList<String>, String> spalteAdresse = new TableColumn<>("Adresse");
        TableColumn<ObservableList<String>, String> spalteOrt = new TableColumn<>("Ort");
        TableColumn<ObservableList<String>, String> spaltePostleitzahl = new TableColumn<>("PLZ");
        TableColumn<ObservableList<String>, String> spalteEmail = new TableColumn<>("E-Mail");
        TableColumn<ObservableList<String>, String> spalteNatelnummer = new TableColumn<>("Natelnr.");

        spalteKundennummer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        spalteNachname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        spalteVorname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        spalteAdresse.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
        spaltePostleitzahl.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4)));
        spalteOrt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5)));
        spalteEmail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(6)));
        spalteNatelnummer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7)));

        tableViewKunden.getColumns().addAll(spalteKundennummer, spalteNachname, spalteVorname, spalteAdresse, spaltePostleitzahl, spalteOrt, spalteEmail, spalteNatelnummer);

        try {
            while (resultSet.next()) {

                ObservableList<String> spalten = FXCollections.observableArrayList();
                spalten.add(resultSet.getString("kundenId"));
                spalten.add(resultSet.getString("nachname"));
                spalten.add(resultSet.getString("vorname"));
                spalten.add(resultSet.getString("adresse"));
                spalten.add(resultSet.getString("postleitzahl"));
                spalten.add(resultSet.getString("ort"));
                spalten.add(resultSet.getString("email"));
                spalten.add(resultSet.getString("natelnummer"));

                observableList.add(spalten);
                tableViewKunden.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int zeilennummer = tableViewKunden.getSelectionModel().getFocusedIndex();
                        int kundennummer = Integer.parseInt(spalteKundennummer.getCellData(zeilennummer));

                        try {
                            if(bearbeiten){
                                ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter, kunden WHERE kundenId = kunden_id AND bestellung_id=" + rechnungsnummer);
                                resultsetKunde.next();
                                Date datum = resultsetKunde.getDate("datum");
                                resultsetKunde.close();
                                statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id, dokumenttyp, datum) VALUES (" + rechnungsnummer + "," + kundennummer + "," + modus + ", '" + datum + "')");
                            } else{
                                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                                LocalDate datum = LocalDate.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id, dokumenttyp, datum) VALUES (" + rechnungsnummer + "," + kundennummer + "," + modus + ", '" + formatter.format(datum) + "')");
                            }
                            root = FXMLLoader.load(getClass().getResource("artikelFuerRechnung.fxml"));
                        } catch (Exception e) {
                            AllgemeineMethoden.fehlermeldung(e);
                        }
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                });

            }

            tableViewKunden.setItems(observableList);

        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        spalteKundennummer.setPrefWidth(70);
        spalteNachname.setPrefWidth(113);
        spalteVorname.setPrefWidth(113);
        spalteAdresse.setPrefWidth(140);
        spalteOrt.setPrefWidth(120);
        spaltePostleitzahl.setPrefWidth(40);
        spalteEmail.setPrefWidth(160);
        spalteNatelnummer.setPrefWidth(90);

    }

    @FXML
    protected void ohneKunde(ActionEvent event){
        try {
            if(bearbeiten) {
                ResultSet resultsetKunde = statement.executeQuery("SELECT * FROM bearbeiter, kunden WHERE kundenId = kunden_id AND bestellung_id=" + rechnungsnummer);
                resultsetKunde.next();
                Date datum = resultsetKunde.getDate("datum");
                resultsetKunde.close();
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp, datum) VALUES (" + rechnungsnummer + "," + modus + ",'" + datum + "')");

            } else{
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                LocalDate datum = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                statement.execute("INSERT INTO bearbeiter (bestellung_id, dokumenttyp, datum) VALUES (" + rechnungsnummer + "," + modus + ",'" + formatter.format(datum) + "')");
            }
            root = FXMLLoader.load(getClass().getResource("artikelFuerRechnung.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }

    @FXML
    protected void zurueck(ActionEvent event){
        try {
            if (bearbeiten == true){
                ResultSet resultsetLagerbestandAnzahl = statement.executeQuery("SELECT COUNT(artikel_id) FROM bestellung, bearbeiter WHERE bestellungId = 0");
                resultsetLagerbestandAnzahl.next();
                int anzahlArtikel = resultsetLagerbestandAnzahl.getInt(1);
                resultsetLagerbestandAnzahl.close();
                for (int i = 0; i < anzahlArtikel; i++) {
                    ResultSet resultsetLagerbestandAendern = statement.executeQuery("SELECT * FROM bestellung, bearbeiter WHERE bestellungId = 0");
                    if (resultsetLagerbestandAendern.absolute(i+1)){
                        int anzahl = resultsetLagerbestandAendern.getInt("anzahl");
                        int artikelId = resultsetLagerbestandAendern.getInt("artikel_id");
                        Date datum = resultsetLagerbestandAendern.getDate("datum");
                        statement.execute("UPDATE artikel SET lagerbestand = lagerbestand - " + anzahl + " WHERE artikelId = " + artikelId);
                        statement.execute("UPDATE verkaufteStueck SET anzahl = anzahl + " + anzahl + " WHERE artikel_Id = " + artikelId + " AND datum = '" + datum + "'");
                    }
                    resultsetLagerbestandAendern.close();
                }

                statement.execute("DELETE FROM bestellung WHERE bestellungId = " + rechnungsnummer);
                statement.execute("UPDATE bestellung SET bestellungId = " + rechnungsnummer + "WHERE bestellungId = 0");
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                statement.execute("UPDATE bearbeiter SET bestellung_id = " + rechnungsnummer + " WHERE bestellung_id = 0");
                statement.execute("UPDATE unternehmen SET bearbeiten = null");
            } else{
                statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                statement.execute("DELETE FROM bestellung WHERE bestellungId = " + rechnungsnummer);
            }

            root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event1 -> {
                stage.close();
            });
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

    }


}