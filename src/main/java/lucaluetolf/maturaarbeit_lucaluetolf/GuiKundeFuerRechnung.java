package lucaluetolf.maturaarbeit_lucaluetolf;

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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiKundeFuerRechnung extends GuiLeiste implements Initializable {
    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int rechnungsnummer;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ListView<Kunde> listviewKunden;
    @FXML
    private ComboBox sortierenKunde;

    @FXML
    private TableView tableViewKunden;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*String auswahl[] = {"Nachname", "Vorname", "Kundennummer", "Hinzugefügt"};
        ObservableList<String> observableListCombobox = FXCollections.observableArrayList(auswahl);
        sortierenKunde.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ba8759;");
        sortierenKunde.setItems(observableListCombobox);

        ObservableList<Kunde> observableList = FXCollections.observableArrayList();
        observableList.clear();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden");
            while (resultSet.next()) {
                int kundennummer = Integer.parseInt(resultSet.getString("id"));
                String nachname = String.valueOf(resultSet.getString("nachname"));
                String vorname = String.valueOf(resultSet.getString("vorname"));
                String adresse = String.valueOf(resultSet.getString("adresse"));
                int postleitzahl = Integer.parseInt(resultSet.getString("postleitzahl"));
                String ort = String.valueOf(resultSet.getString("ort"));
                String email = String.valueOf(resultSet.getString("email"));
                int natelnummer = Integer.parseInt(resultSet.getString("natelnummer"));
                observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (sortierenKunde.getValue() == "Nachname") {
                    observableList.clear();
                    try {
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden ORDER BY Nachname");
                        while (resultSet.next()) {
                            int kundennummer = Integer.parseInt(resultSet.getString("id"));
                            String nachname = String.valueOf(resultSet.getString("nachname"));
                            String vorname = String.valueOf(resultSet.getString("vorname"));
                            String adresse = String.valueOf(resultSet.getString("adresse"));
                            int postleitzahl = Integer.parseInt(resultSet.getString("postleitzahl"));
                            String ort = String.valueOf(resultSet.getString("ort"));
                            String email = String.valueOf(resultSet.getString("email"));
                            int natelnummer = Integer.parseInt(resultSet.getString("natelnummer"));
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (sortierenKunde.getValue() == "Vorname") {
                    observableList.clear();
                    try {
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden ORDER BY vorname");
                        while (resultSet.next()) {
                            int kundennummer = Integer.parseInt(resultSet.getString("id"));
                            String nachname = String.valueOf(resultSet.getString("nachname"));
                            String vorname = String.valueOf(resultSet.getString("vorname"));
                            String adresse = String.valueOf(resultSet.getString("adresse"));
                            int postleitzahl = Integer.parseInt(resultSet.getString("postleitzahl"));
                            String ort = String.valueOf(resultSet.getString("ort"));
                            String email = String.valueOf(resultSet.getString("email"));
                            int natelnummer = Integer.parseInt(resultSet.getString("natelnummer"));
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                if (sortierenKunde.getValue() == "Kundennummer") {
                    observableList.clear();
                    try {
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden ORDER BY id");
                        while (resultSet.next()) {
                            int kundennummer = Integer.parseInt(resultSet.getString("id"));
                            String nachname = String.valueOf(resultSet.getString("nachname"));
                            String vorname = String.valueOf(resultSet.getString("vorname"));
                            String adresse = String.valueOf(resultSet.getString("adresse"));
                            int postleitzahl = Integer.parseInt(resultSet.getString("postleitzahl"));
                            String ort = String.valueOf(resultSet.getString("ort"));
                            String email = String.valueOf(resultSet.getString("email"));
                            int natelnummer = Integer.parseInt(resultSet.getString("natelnummer"));
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                if (sortierenKunde.getValue() == "Hinzugefügt") {
                    observableList.clear();
                    try {
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden DESC");
                        while (resultSet.next()) {
                            int kundennummer = Integer.parseInt(resultSet.getString("id"));
                            String nachname = String.valueOf(resultSet.getString("nachname"));
                            String vorname = String.valueOf(resultSet.getString("vorname"));
                            String adresse = String.valueOf(resultSet.getString("adresse"));
                            int postleitzahl = Integer.parseInt(resultSet.getString("postleitzahl"));
                            String ort = String.valueOf(resultSet.getString("ort"));
                            String email = String.valueOf(resultSet.getString("email"));
                            int natelnummer = Integer.parseInt(resultSet.getString("natelnummer"));
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        };
        sortierenKunde.setOnAction(event);

        listviewKunden.setItems(null);
        listviewKunden.setItems(observableList);

        listviewKunden.setCellFactory(param -> new ListCell<Kunde>() {
            @Override
            protected void updateItem(Kunde item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getKundenummer() + item.getNachname() + item.getVorname() + item.getAdresse() + item.getPostleitzahl() + item.getOrt() + item.geteMail() + item.getNatelnummer());
                    listviewKunden.setId(String.valueOf(item.getKundenummer()));
                }
            }
        });
        listviewKunden.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    root = FXMLLoader.load(getClass().getResource("kundeBearbeiten.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        });*/
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList();
        TableColumn<ObservableList<String>, String> spalteKundennummer = new TableColumn<>("Kundennummer");
        TableColumn<ObservableList<String>, String> spalteNachname = new TableColumn<>("Nachname");
        TableColumn<ObservableList<String>, String> spalteVorname = new TableColumn<>("Vorname");
        TableColumn<ObservableList<String>, String> spalteAdresse = new TableColumn<>("Adresse");
        TableColumn<ObservableList<String>, String> spalteOrt = new TableColumn<>("Ort");
        TableColumn<ObservableList<String>, String> spaltePostleitzahl = new TableColumn<>("Postleitzahl");
        TableColumn<ObservableList<String>, String> spalteEmail = new TableColumn<>("E-Mail");
        TableColumn<ObservableList<String>, String> spalteNatelnummer = new TableColumn<>("Natelnummer");

        spalteKundennummer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        spalteNachname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        spalteVorname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        spalteAdresse.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
        spalteOrt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4)));
        spaltePostleitzahl.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5)));
        spalteEmail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(6)));
        spalteNatelnummer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7)));


        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM kunden");
            while (resultSet.next()) {
                ObservableList spalten = FXCollections.observableArrayList();
                spalten.add(resultSet.getString("kundenId"));
                spalten.add(resultSet.getString("nachname"));
                spalten.add(resultSet.getString("vorname"));
                spalten.add(resultSet.getString("adresse"));
                spalten.add(resultSet.getString("postleitzahl"));
                spalten.add(resultSet.getString("ort"));
                spalten.add(resultSet.getString("email"));
                spalten.add(resultSet.getString("natelnummer"));

                observableList.add(spalten);


                tableViewKunden.getColumns().addAll(spalteKundennummer, spalteNachname, spalteVorname, spalteAdresse, spalteOrt, spaltePostleitzahl, spalteEmail, spalteNatelnummer);

                tableViewKunden.setId(resultSet.getString("kundenId"));
                tableViewKunden.setItems(observableList);
                tableViewKunden.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        int zeilennummer = tableViewKunden.getSelectionModel().getFocusedIndex();
                        int kundennummer = Integer.parseInt(spalteKundennummer.getCellData(zeilennummer));

                        ResultSet resultSetRechnungsnummer = null;
                        try {
                            resultSetRechnungsnummer = statement.executeQuery("SELECT rechnungsnummer FROM unternehmen");
                        if (resultSetRechnungsnummer.next()){
                                rechnungsnummer = resultSetRechnungsnummer.getInt("rechnungsnummer");
                            }
                            statement.execute("INSERT INTO bearbeiter (bestellung_id, kunden_id) VALUES (" + rechnungsnummer + "," + kundennummer + ")");
                            root = FXMLLoader.load(getClass().getResource("artikelFuerRechnung.fxml"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
