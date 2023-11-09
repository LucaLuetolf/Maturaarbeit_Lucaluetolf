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

public class GuiKunde extends GuiTaskleiste implements Initializable {
    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
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
    private Label labelName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT unternehmensname FROM unternehmen");
            resultSet.next();
            labelName.setText(resultSet.getString(1));
            resultSet.close();
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
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
            ResultSet resultSetKunden = statement.executeQuery("SELECT * FROM kunden");
            while (resultSetKunden.next()) {

                ObservableList<String> spalten = FXCollections.observableArrayList();
                spalten.add(resultSetKunden.getString("kundenId"));
                spalten.add(resultSetKunden.getString("nachname"));
                spalten.add(resultSetKunden.getString("vorname"));
                spalten.add(resultSetKunden.getString("adresse"));
                spalten.add(resultSetKunden.getString("postleitzahl"));
                spalten.add(resultSetKunden.getString("ort"));
                spalten.add(resultSetKunden.getString("email"));
                spalten.add(resultSetKunden.getString("natelnummer"));

                observableList.add(spalten);
                tableViewKunden.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        try {
                            int zahl = tableViewKunden.getSelectionModel().getFocusedIndex();
                            int kundennummer = Integer.parseInt(spalteKundennummer.getCellData(zahl));
                            statement.execute("UPDATE unternehmen SET bearbeiten = " + kundennummer);
                            root = FXMLLoader.load(getClass().getResource("kundeBearbeiten.fxml"));
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
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        tableViewKunden.setItems(observableList);

        spalteKundennummer.setPrefWidth(100);
        spalteNachname.setPrefWidth(113);
        spalteVorname.setPrefWidth(113);
        spalteAdresse.setPrefWidth(120);
        spalteOrt.setPrefWidth(120);
        spaltePostleitzahl.setPrefWidth(40);
        spalteEmail.setPrefWidth(130);
        spalteNatelnummer.setPrefWidth(110);

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
