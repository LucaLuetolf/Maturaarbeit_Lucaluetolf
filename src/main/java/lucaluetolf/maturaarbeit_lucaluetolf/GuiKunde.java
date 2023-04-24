package lucaluetolf.maturaarbeit_lucaluetolf;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiKunde extends GuiLeiste implements Initializable {
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
    private ListView<Kunde> listviewKunden;
    @FXML
    private ComboBox sortierenKunde;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String auswahl[] = {"Nachname", "Vorname", "Kundennummer", "Hinzugefügt"};
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
                }
            }
        });
        listviewKunden.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println(listviewKunden.getSelectionModel().getSelectedItem().getKundenummer());
                setKundennummerBearbeiten(listviewKunden.getSelectionModel().getSelectedItem().getKundenummer());
                System.out.println(getKundennummerBearbeiten());
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
        });
    }

    @FXML
    protected void toSceneKundeErfassen(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("kundeErfassen.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
