package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiEinstellungen extends GuiTaskleiste implements Initializable {

    Statement statement;
    Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    Stage stage;

    @FXML
    private JFXButton buttonAppInfos;

    @FXML
    private JFXButton buttonEinheiten;

    @FXML
    private JFXButton buttonKonto;

    @FXML
    private JFXButton buttonStartseite;

    @FXML
    private JFXButton buttonTitelKonto;

    @FXML
    private RadioButton radioButtonAktivErfassen;

    @FXML
    private RadioButton radioButtonInaktivErfassen;
    @FXML
    private TextField textfieldEinheitId;

    @FXML
    private TextField textfieldBezeichnung;

    @FXML
    private TextField textfieldAbkuerzung;

    @FXML
    private TableView tableViewEinheiten;

    @FXML
    private AnchorPane anchorPaneStartseite;
    @FXML
    private AnchorPane anchorPaneKonto;
    @FXML
    private AnchorPane anchorPaneEinheiten;
    @FXML
    private AnchorPane anchorPaneAppInfos;

    @FXML
    private Label labelUnternehmensname;


    private boolean booleanEinheitId = false;
    private boolean booleanBezeichnung = false;
    private boolean booleanAbkuerzung = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT unternehmensname FROM unternehmen");
            resultSet.next();
            labelUnternehmensname.setText(resultSet.getString(1));
            resultSet.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        anchorPaneStartseite.setVisible(true);
        anchorPaneEinheiten.setVisible(false);
    }

    @FXML
    void appInfos(ActionEvent event) {
    }

    @FXML
    void einheiten(ActionEvent event) {
        anchorPaneStartseite.setVisible(false);
        anchorPaneEinheiten.setVisible(true);
        radioButtonAktivErfassen.setSelected(true);
        tableViewEinheiten = einheitenAnzeigen();
    }
    private TableView einheitenAnzeigen(){
        tableViewEinheiten.getItems().clear();
        tableViewEinheiten.getColumns().clear();
        ObservableList<ObservableList<Object>> observableList = FXCollections.observableArrayList();
        observableList.clear();
        TableColumn<ObservableList<Object>, String> spalteEinheitId = new TableColumn<>("Id");
        TableColumn<ObservableList<Object>, String> spalteEinheit = new TableColumn<>("Einheit");
        TableColumn<ObservableList<Object>, String> spalteAbkuerzung = new TableColumn<>("Abkürzung");
        TableColumn<ObservableList<Object>, RadioButton> spalteRadioButtonAktiv = new TableColumn<>("Aktiv");
        TableColumn<ObservableList<Object>, RadioButton> spalteRadioButtonInaktiv = new TableColumn<>("Inaktiv");
        spalteEinheitId.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));
        spalteEinheit.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));
        spalteAbkuerzung.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));
        spalteRadioButtonAktiv.setCellValueFactory(param -> new SimpleObjectProperty<RadioButton>((RadioButton) param.getValue().get(3)));
        spalteRadioButtonInaktiv.setCellValueFactory(param -> new SimpleObjectProperty<RadioButton>((RadioButton) param.getValue().get(4)));

        tableViewEinheiten.getColumns().addAll(spalteEinheitId, spalteEinheit, spalteAbkuerzung, spalteRadioButtonAktiv, spalteRadioButtonInaktiv);

        try {
            ResultSet resultsetEinheiten = statement.executeQuery("SELECT * FROM einheiten");
            while (resultsetEinheiten.next()){
                ObservableList<Object> spalten = FXCollections.observableArrayList();
                spalten.add(resultsetEinheiten.getString("einheitId"));
                spalten.add(resultsetEinheiten.getString("bezeichnung"));
                spalten.add(resultsetEinheiten.getString("abkuerzung"));

                RadioButton radioButtonAktiv = new RadioButton("Wird verwendet");
                radioButtonAktiv.setId(String.valueOf(resultsetEinheiten.getInt("einheitId")));

                boolean aktiv = resultsetEinheiten.getBoolean("aktiv");
                radioButtonAktiv.setSelected(aktiv);

                RadioButton radioButtonInaktiv = new RadioButton("Wird nicht verwendet");
                radioButtonInaktiv.setId(String.valueOf(resultsetEinheiten.getInt("einheitId")));
                radioButtonInaktiv.setSelected(!aktiv);


                radioButtonAktiv.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (radioButtonAktiv.isSelected()) {
                            int einheitId = Integer.parseInt(radioButtonAktiv.getId());
                            radioButtonInaktiv.setSelected(false);
                            try {
                                statement.execute("UPDATE einheiten SET aktiv = true WHERE einheitId = " + radioButtonAktiv.getId());
                            } catch (Exception e) {
                                AllgemeineMethoden.fehlermeldung(e);
                            }
                        } else{
                            radioButtonInaktiv.setSelected(true);
                            try {
                                statement.execute("UPDATE einheiten SET aktiv = false WHERE einheitId = " + radioButtonAktiv.getId());
                            } catch (Exception e) {
                                AllgemeineMethoden.fehlermeldung(e);
                            }
                        }
                    }
                });
                spalten.add(radioButtonAktiv);


                radioButtonInaktiv.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (radioButtonInaktiv.isSelected()){
                            radioButtonAktiv.setSelected(false);
                            try {
                                statement.execute("UPDATE einheiten SET aktiv = false WHERE einheitId = " + radioButtonAktiv.getId());
                            } catch (Exception e) {
                                AllgemeineMethoden.fehlermeldung(e);
                            }
                        } else{
                            radioButtonAktiv.setSelected(true);
                            try {
                                statement.execute("UPDATE einheiten SET aktiv = true WHERE einheitId = " + radioButtonAktiv.getId());
                            } catch (Exception e) {
                                AllgemeineMethoden.fehlermeldung(e);
                            }
                        }
                    }
                });

                spalten.add(radioButtonInaktiv);


                observableList.add(spalten);
            }
            resultsetEinheiten.close();
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        spalteEinheitId.setPrefWidth(50);
        spalteEinheit.setPrefWidth(150);
        spalteAbkuerzung.setPrefWidth(70);
        spalteRadioButtonAktiv.setPrefWidth(170);
        spalteRadioButtonInaktiv.setPrefWidth(188);
        tableViewEinheiten.setItems(observableList);
        return tableViewEinheiten;
    }


    @FXML
    void konto(ActionEvent event) {

    }

    @FXML
    void startseite(ActionEvent event) {
        anchorPaneStartseite.setVisible(true);
        anchorPaneEinheiten.setVisible(false);
    }

    @FXML
    void einheitErfassen(ActionEvent event) {
        if (booleanEinheitId && booleanBezeichnung && booleanAbkuerzung){
            try {
                statement.execute("INSERT INTO einheiten (einheitId, bezeichnung, abkuerzung, aktiv) VALUES (" + textfieldEinheitId.getText() + ",'" + textfieldBezeichnung.getText() + "','" + textfieldAbkuerzung.getText() + "'," + radioButtonAktivErfassen.isSelected() + ")");
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            textfieldEinheitId.setText("");
            textfieldBezeichnung.setText("");
            textfieldAbkuerzung.setText("");
            radioButtonAktivErfassen.setSelected(true);
            radioButtonInaktivErfassen.setSelected(false);
            textfieldEinheitId.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
            textfieldBezeichnung.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
            textfieldAbkuerzung.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
            tableViewEinheiten = einheitenAnzeigen();
        } else{
            if (booleanEinheitId == false){
                textfieldEinheitId.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanBezeichnung == false){
                textfieldBezeichnung.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (booleanAbkuerzung == false){
                textfieldAbkuerzung.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
        }
    }

    @FXML
    void einheitErfassenZuruecksetzten(ActionEvent event) {
        textfieldEinheitId.setText("");
        textfieldBezeichnung.setText("");
        textfieldAbkuerzung.setText("");
        radioButtonAktivErfassen.setSelected(true);
        radioButtonInaktivErfassen.setSelected(false);
        textfieldEinheitId.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        textfieldBezeichnung.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        textfieldAbkuerzung.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
    }

    private boolean tester(String regex, TextField textField) {
        boolean boolean1 = textField.getText().matches(regex);
        if (boolean1) {
            textField.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
        } else {
            textField.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        }
        return boolean1;
    }

    @FXML
    void textfieldEinheitIdKey() {
        textfieldEinheitId.setText(textfieldEinheitId.getText().replaceAll("[^0-9]", ""));
        textfieldEinheitId.positionCaret(textfieldEinheitId.getLength());
        booleanEinheitId = tester("^[1-9]\\d*$", textfieldEinheitId);
        if (textfieldEinheitId.getText() != ""){
            try {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT COUNT(einheitId) FROM einheiten WHERE einheitId = " + textfieldEinheitId.getText());
                resultsetArtikel.next();
                int res = resultsetArtikel.getInt(1);
                if (res == 0){
                    textfieldEinheitId.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
                }
                else{
                    textfieldEinheitId.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                resultsetArtikel.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
        }
    }

    @FXML
    void textfieldBezeichnungKey() {
        textfieldBezeichnung.setText(textfieldBezeichnung.getText().replaceAll("[^A-Za-z]", ""));
        textfieldBezeichnung.positionCaret(textfieldBezeichnung.getLength());
        booleanBezeichnung = tester("[A-Z][a-z]+$", textfieldBezeichnung);
    }
    @FXML
    void textfieldAbkuerzungKey() {
        textfieldAbkuerzung.setText(textfieldAbkuerzung.getText().replaceAll("[^A-Za-z]", ""));
        textfieldAbkuerzung.positionCaret(textfieldAbkuerzung.getLength());
        if (textfieldAbkuerzung.getLength() >= 4){
            booleanAbkuerzung = false;
            textfieldAbkuerzung.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
        } else{
            booleanAbkuerzung = tester("^[A-Z]?[a-z]*$", textfieldAbkuerzung);
        }

    }
    @FXML
    void radioButtonAktivErfassen(ActionEvent event) {
        if (radioButtonAktivErfassen.isSelected()) {
            radioButtonInaktivErfassen.setSelected(false);
        }else{
            radioButtonInaktivErfassen.setSelected(true);
        }
    }

    @FXML
    void radioButtonInaktivErfassen(ActionEvent event) {
        if (radioButtonInaktivErfassen.isSelected()) {
            radioButtonAktivErfassen.setSelected(false);
        }else{
            radioButtonAktivErfassen.setSelected(true);
        }
    }

    @FXML
    void appZuruecksetzten(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Achtung, es werden alle Daten gelöscht!");
        alert.setContentText("Möchten Sie fortfahren?");

        ButtonType buttonTypeFortfahren = new ButtonType("Fortfahren", ButtonBar.ButtonData.RIGHT);
        ButtonType buttonTypeAbbrechen = new ButtonType("Abbrechen", ButtonBar.ButtonData.RIGHT);

        alert.getButtonTypes().setAll(buttonTypeFortfahren, buttonTypeAbbrechen);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeFortfahren) {
                try {
                    statement.execute("DROP TABLE IF EXISTS unternehmen");
                    statement.execute("DROP TABLE IF EXISTS artikel");
                    statement.execute("DROP TABLE IF EXISTS kunden");
                    statement.execute("DROP TABLE IF EXISTS einheiten");
                    statement.execute("DROP TABLE IF EXISTS bearbeiter");
                    statement.execute("DROP TABLE IF EXISTS bestellung");
                    statement.execute("DROP TABLE IF EXISTS verkaufteStueck");
                    statement.execute("DROP TABLE IF EXISTS loeschen");
                    Stage stage = (Stage) buttonEinheiten.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
            } else if (response == buttonTypeAbbrechen) {
                alert.close();
            }
        });
    }
}
