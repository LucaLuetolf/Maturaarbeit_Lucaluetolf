package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ResourceBundle;

public class GuiEinstellungen extends GuiTaskleiste implements Initializable {

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
    private Label labelUnternehmensname;


    // Konto einstellungen:
    @FXML
    private Label labelUnternehmen;
    @FXML
    private Label labelBenutzername;
    @FXML
    private Label labelPasswort;
    @FXML
    private Label labelLagerbestand;
    @FXML
    private Label labelBank;
    @FXML
    private Label labelIban;

    @FXML
    private JFXButton buttonBearbeitenKonto;
    @FXML
    private JFXButton buttonAbbrechenKonto;
    @FXML
    private JFXButton buttonBildAendern;

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
        anchorPaneKonto.setVisible(false);
        anchorPaneEinheiten.setVisible(false);
    }

    @FXML
    void appInfos(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("erstanmeldung.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void einheiten(ActionEvent event) {
        anchorPaneStartseite.setVisible(false);
        anchorPaneEinheiten.setVisible(true);
        anchorPaneKonto.setVisible(false);
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
        booleanUnternehmen = true;
        booleanBenutzername = true;
        booleanPasswort = true;
        booleanLagerbestandAnzeige = true;
        booleanBank = true;
        booleanIban = true;
        buttonBearbeitenKonto.setId("1");
        anchorPaneStartseite.setVisible(false);
        anchorPaneKonto.setVisible(true);
        anchorPaneEinheiten.setVisible(false);

        buttonAbbrechenKonto.setVisible(false);
        buttonBildAendern.setVisible(false);

        textfeldUnternehmen.setVisible(false);
        textfeldBenutzername.setVisible(false);
        textfeldPasswort.setVisible(false);
        textfeldLagerbestandAnzeige.setVisible(false);
        textfeldBank.setVisible(false);
        textfeldIban.setVisible(false);
        try {
            ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");
            resultSetUnternehmen.next();
            if (resultSetUnternehmen.getString("dateityp") == null){
                Image image = new Image(String.valueOf(getClass().getResource("/lucaluetolf/maturaarbeit_lucaluetolf/Bilder/System/Unternehmen/Logo.png")));
                imageviewUnternehmen.setImage(image);
            }else{
                String imagePath = "Bilder/Benutzer/Unternehmen/" + resultSetUnternehmen.getInt("bildnummer") + "." + resultSetUnternehmen.getString("dateityp");
                Image image = new Image(new FileInputStream(imagePath));
                imageviewUnternehmen.setImage(image);
            }

            labelUnternehmen.setText(resultSetUnternehmen.getString("unternehmensname"));
            labelBenutzername.setText(resultSetUnternehmen.getString("benutzername"));
            labelPasswort.setText(resultSetUnternehmen.getString("passwort"));
            labelLagerbestand.setText(String.valueOf(resultSetUnternehmen.getInt("lagerbestandOrange")));
            labelBank.setText(resultSetUnternehmen.getString("bank"));
            labelIban.setText(resultSetUnternehmen.getString("iban"));
            resultSetUnternehmen.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }
    @FXML
    void buttonBearbeitenKonto(ActionEvent event) {
        if (Integer.parseInt(buttonBearbeitenKonto.getId()) == 1){
            buttonBearbeitenKonto.setId("2");
            buttonBearbeitenKonto.setText("speichern");
            buttonBildAendern.setVisible(true);
            buttonAbbrechenKonto.setVisible(true);
            labelUnternehmen.setVisible(false);
            labelBenutzername.setVisible(false);
            labelPasswort.setVisible(false);
            labelLagerbestand.setVisible(false);
            labelBank.setVisible(false);
            labelIban.setVisible(false);
            textfeldUnternehmen.setVisible(true);
            textfeldBenutzername.setVisible(true);
            textfeldPasswort.setVisible(true);
            textfeldLagerbestandAnzeige.setVisible(true);
            textfeldBank.setVisible(true);
            textfeldIban.setVisible(true);

            textfeldUnternehmen.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldBenutzername.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldPasswort.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldLagerbestandAnzeige.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldBank.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
            textfeldIban.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");

            try {
                ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");
                resultSetUnternehmen.next();
                textfeldUnternehmen.setText(resultSetUnternehmen.getString("unternehmensname"));
                textfeldBenutzername.setText(resultSetUnternehmen.getString("benutzername"));
                textfeldPasswort.setText(resultSetUnternehmen.getString("passwort"));
                textfeldLagerbestandAnzeige.setText(String.valueOf(resultSetUnternehmen.getInt("lagerbestandOrange")));
                textfeldBank.setText(resultSetUnternehmen.getString("bank"));
                textfeldIban.setText(resultSetUnternehmen.getString("iban"));
                resultSetUnternehmen.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }

        } else {
            if (booleanUnternehmen && booleanBenutzername && booleanPasswort && booleanLagerbestandAnzeige && booleanBank && booleanIban){
                if ((textfeldBank.getLength() != 0 && textfeldIban.getLength() != 0) || (textfeldBank.getLength() == 0 && textfeldIban.getLength() ==0)){
                    try {
                        if(textfeldBank.getLength() == 0 && textfeldIban.getLength() == 0){
                            statement.execute("UPDATE unternehmen SET unternehmensname = '" + textfeldUnternehmen.getText() + "', benutzername = '" + textfeldBenutzername.getText() + "', passwort = '" + textfeldPasswort.getText() + "', lagerbestandOrange = " + textfeldLagerbestandAnzeige.getText() + ", bank = '', iban = ''");
                        } else {
                            statement.execute("UPDATE unternehmen SET unternehmensname = '" + textfeldUnternehmen.getText() + "', benutzername = '" + textfeldBenutzername.getText() + "', passwort = '" + textfeldPasswort.getText() + "', lagerbestandOrange = " + textfeldLagerbestandAnzeige.getText() + ", bank = '" + textfeldBank.getText() + "', iban = '" + textfeldIban.getText() + "'");
                        }
                        if (pfadBildLogo != ""){
                            AllgemeineMethoden.dateiKopieren(pfadBildLogo, neuerPfadBildLogo);
                            ResultSet resultSetDateityp = statement.executeQuery("SELECT * FROM unternehmen");
                            resultSetDateityp.next();
                            String dateityp = resultSetDateityp.getString("dateityp");
                            int bildnummer = resultSetDateityp.getInt("bildnummer");
                            resultSetDateityp.close();

                            File neuesBildAlterPfad = new File(pfadBildLogo);
                            File neuesBildAlterName = new File(neuerPfadBildLogo + "/" + neuesBildAlterPfad.getName());
                            String dateitypNeu = "";
                            int index = neuesBildAlterPfad.getName().lastIndexOf(".");
                            if (index > 0) {
                                dateitypNeu = neuesBildAlterPfad.getName().substring(index + 1);
                            }
                            File neuesBildNeuerName = new File(String.valueOf("Bilder/Benutzer/Unternehmen/" + (bildnummer+1) + "." + dateitypNeu));
                            if(neuesBildAlterName.renameTo(neuesBildNeuerName)){
                                statement.execute("UPDATE unternehmen SET dateityp = '" + dateitypNeu + "', bildnummer = " + (bildnummer+1));
                            }
                        }

                        buttonBearbeitenKonto.setId("1");
                        buttonBearbeitenKonto.setText("bearbeiten");

                        buttonAbbrechenKonto.setVisible(false);
                        buttonBildAendern.setVisible(false);

                        labelUnternehmen.setVisible(true);
                        labelBenutzername.setVisible(true);
                        labelPasswort.setVisible(true);
                        labelLagerbestand.setVisible(true);
                        labelBank.setVisible(true);
                        labelIban.setVisible(true);

                        ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");
                        resultSetUnternehmen.next();
                        labelUnternehmen.setText(resultSetUnternehmen.getString("unternehmensname"));
                        labelBenutzername.setText(resultSetUnternehmen.getString("benutzername"));
                        labelPasswort.setText(resultSetUnternehmen.getString("passwort"));
                        labelLagerbestand.setText(String.valueOf(resultSetUnternehmen.getInt("lagerbestandOrange")));
                        labelBank.setText(resultSetUnternehmen.getString("bank"));
                        labelIban.setText(resultSetUnternehmen.getString("iban"));
                        resultSetUnternehmen.close();

                        textfeldUnternehmen.setVisible(false);
                        textfeldBenutzername.setVisible(false);
                        textfeldPasswort.setVisible(false);
                        textfeldLagerbestandAnzeige.setVisible(false);
                        textfeldBank.setVisible(false);
                        textfeldIban.setVisible(false);
                    } catch (Exception e) {
                        AllgemeineMethoden.fehlermeldung(e);
                    }
                } else {
                    textfeldBank.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                    textfeldIban.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }


            } else {
                if (booleanUnternehmen == false) {
                    textfeldUnternehmen.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanBenutzername == false) {
                    textfeldBenutzername.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanPasswort == false) {
                    textfeldPasswort.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanLagerbestandAnzeige == false) {
                    textfeldLagerbestandAnzeige.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanBank == false) {
                    textfeldBank.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                if (booleanIban == false) {
                    textfeldIban.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
            }
        }
    }
    @FXML
    void buttonAbbrechenKonto(ActionEvent event) {
        buttonBearbeitenKonto.setId("1");
        buttonBearbeitenKonto.setText("bearbeiten");
        buttonAbbrechenKonto.setVisible(false);
        labelUnternehmen.setVisible(true);
        labelBenutzername.setVisible(true);
        labelPasswort.setVisible(true);
        labelLagerbestand.setVisible(true);
        labelBank.setVisible(true);
        labelIban.setVisible(true);
        try {
            ResultSet resultSetUnternehmen = statement.executeQuery("SELECT * FROM unternehmen");
            resultSetUnternehmen.next();
            labelUnternehmen.setText(resultSetUnternehmen.getString("unternehmensname"));
            labelBenutzername.setText(resultSetUnternehmen.getString("benutzername"));
            labelPasswort.setText(resultSetUnternehmen.getString("passwort"));
            labelLagerbestand.setText(String.valueOf(resultSetUnternehmen.getInt("lagerbestandOrange")));
            labelBank.setText(resultSetUnternehmen.getString("bank"));
            labelIban.setText(resultSetUnternehmen.getString("iban"));
            resultSetUnternehmen.close();
        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        buttonBildAendern.setVisible(false);

        textfeldUnternehmen.setVisible(false);
        textfeldBenutzername.setVisible(false);
        textfeldPasswort.setVisible(false);
        textfeldLagerbestandAnzeige.setVisible(false);
        textfeldBank.setVisible(false);
        textfeldIban.setVisible(false);
    }

    @FXML
    void startseite(ActionEvent event) {
        anchorPaneStartseite.setVisible(true);
        anchorPaneKonto.setVisible(false);
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
                    booleanEinheitId = false;
                }
                resultsetArtikel.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
        }
    }

    @FXML
    void textfieldBezeichnungKey() {
        textfieldBezeichnung.setText(textfieldBezeichnung.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ]", ""));
        textfieldBezeichnung.positionCaret(textfieldBezeichnung.getLength());
        booleanBezeichnung = tester("[A-Z][a-zéàèöäüÉÀÈÖÄÜ]+$", textfieldBezeichnung);
    }
    @FXML
    void textfieldAbkuerzungKey() {
        textfieldAbkuerzung.setText(textfieldAbkuerzung.getText().replaceAll("[^A-Za-zéàèöäüÉÀÈÖÄÜ]", ""));
        textfieldAbkuerzung.positionCaret(textfieldAbkuerzung.getLength());
        if (textfieldAbkuerzung.getLength() >= 4){
            booleanAbkuerzung = false;
            textfieldAbkuerzung.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
        } else{
            booleanAbkuerzung = tester("^(?=.*[A-Za-zéàèöäüÉÀÈÖÄÜ]).*$", textfieldAbkuerzung);
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
                    Stage aktuelleStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    aktuelleStage.close();
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
            } else if (response == buttonTypeAbbrechen) {
                alert.close();
            }
        });
    }
}
