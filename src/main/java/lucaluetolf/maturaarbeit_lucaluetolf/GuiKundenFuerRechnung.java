package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiKundenFuerRechnung extends GuiTaskleiste implements Initializable {
    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    private int rechnungsnummer;
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
    private String stringResultset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            kundenAnzeigen(statement.executeQuery("SELECT * FROM kunden"));
        } catch (SQLException e) {
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
        System.out.println(stringResultset);
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

        TableColumn<ObservableList<String>, String> spalteKundennummer = new TableColumn<>("Kundennummer");
        TableColumn<ObservableList<String>, String> spalteNachname = new TableColumn<>("Nachname");
        TableColumn<ObservableList<String>, String> spalteVorname = new TableColumn<>("Vorname");
        TableColumn<ObservableList<String>, String> spalteAdresse = new TableColumn<>("Adresse");
        TableColumn<ObservableList<String>, String> spalteOrt = new TableColumn<>("Ort");
        TableColumn<ObservableList<String>, String> spaltePostleitzahl = new TableColumn<>("PLZ");
        TableColumn<ObservableList<String>, String> spalteEmail = new TableColumn<>("E-Mail");
        TableColumn<ObservableList<String>, String> spalteNatelnummer = new TableColumn<>("Natelnummer");

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

                        ResultSet resultSetRechnungsnummer = null;
                        try {
                            resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
                            resultSetRechnungsnummer.next();
                            rechnungsnummer = resultSetRechnungsnummer.getInt("rechnungsnummer");
                            resultSetRechnungsnummer.close();
                            ResultSet resultsetDokumenttyp = statement.executeQuery("SELECT dokumenttyp FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                            resultsetDokumenttyp.next();
                            int dokumenttyp = resultsetDokumenttyp.getInt("dokumenttyp");
                            resultsetDokumenttyp.close();
                            statement.execute("DELETE FROM bearbeiter WHERE bestellung_id = " + rechnungsnummer);
                            statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id, dokumenttyp) VALUES (" + rechnungsnummer + "," + kundennummer + "," + dokumenttyp + ")");
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

        spalteKundennummer.setPrefWidth(100);
        spalteNachname.setPrefWidth(113);
        spalteVorname.setPrefWidth(113);
        spalteAdresse.setPrefWidth(120);
        spalteOrt.setPrefWidth(120);
        spaltePostleitzahl.setPrefWidth(40);
        spalteEmail.setPrefWidth(130);
        spalteNatelnummer.setPrefWidth(110);

    }


}
