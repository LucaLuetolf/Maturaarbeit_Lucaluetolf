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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lucaluetolf.maturaarbeit_lucaluetolf.Kunde;

import javax.xml.stream.Location;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Gui{


    Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String passwort1 = "Hans";
    private String benutzername1 = "Hans";

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label labelWillkommen;
    @FXML
    private TextField textfeldArtikelnummer;
    @FXML
    private TextField textfeldName;
    @FXML
    private TextField textfeldPreis;
    @FXML
    private TextField textfeldMenge;
    @FXML
    private TextField textfeldRabatt;
    @FXML
    private TextField textfeldLagerbestand;
    @FXML
    private TextField benutzername;
    @FXML
    private PasswordField passwort;
    @FXML
    private Label falscheEingabe;
    @FXML
    private ListView<Kunde> listviewKunden;
    @FXML
    private GridPane gridpaneArtikel;
    @FXML
    private ComboBox sortierenKunde;

    @FXML
    public void login(ActionEvent event) {
        if (benutzername.getText().equals(benutzername1) && benutzername.getText().length() == benutzername1.length() && passwort.getText().equals(passwort1) && passwort.getText().length() == passwort1.length()) {
            try {
                root = FXMLLoader.load(getClass().getResource("startseite.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            labelWillkommen.setText("Hallo");
            stage.show();
        } else {
            falscheEingabe.setText("Falscher Benutzername oder Passwort");
        }
    }


    //Taskleiste
    @FXML
    protected void toSceneStartseite(ActionEvent event) {
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

    @FXML
    protected void toSceneArtikel(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneKunden(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("kunden.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneNeueRechnung(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("neueRechnung.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void toSceneVerkauf(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("Verkauf.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneLagerbestand(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("lagerbestand.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneMitarbeiter(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("mitarbeiter.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneArchiv(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("archiv.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void toSceneBuchhaltung(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("buchhaltung.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    //Artikel
    @FXML
    protected void toSceneArtikelErfassen(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("artikelErfassen.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void artikelErfassen(ActionEvent event) {
        int artikelnummer = Integer.parseInt(textfeldArtikelnummer.getText());
        String name = String.valueOf(textfeldName.getText());
        double preis = Double.parseDouble(textfeldPreis.getText());
        double menge = Double.parseDouble(textfeldMenge.getText());
        double rabatt = Double.parseDouble(textfeldRabatt.getText());
        int lagerbestand = Integer.parseInt(textfeldLagerbestand.getText());

        try {
            statement.execute("INSERT INTO artikel (id, name, preis, menge, rabatt, lagerbestand) VALUES (" + artikelnummer + "," + "'" + textfeldName.getText() + "'" + "," + preis + "," + menge + "," + rabatt + "," + lagerbestand + ")");
            root = FXMLLoader.load(getClass().getResource("artikel.fxml"));
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

    //Kunde
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

    @FXML
    protected void kundeErfassen(ActionEvent event) {
        int artikelnummer = Integer.parseInt(textfeldArtikelnummer.getText());
        String name = String.valueOf(textfeldName.getText());
        double preis = Double.parseDouble(textfeldPreis.getText());
        double menge = Double.parseDouble(textfeldMenge.getText());
        double rabatt = Double.parseDouble(textfeldRabatt.getText());
        int lagerbestand = Integer.parseInt(textfeldLagerbestand.getText());

        try {
            statement.execute("INSERT INTO artikel (id, name, preis, menge, rabatt, lagerbestand) VALUES (" + artikelnummer + "," + "'" + name + "'" + "," + preis + "," + menge + "," + rabatt + "," + lagerbestand + ")");
            root = FXMLLoader.load(getClass().getResource("kunde.fxml"));
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


    public void submitKunde() {
        String auswahl[] = {"Nachname", "Vorname", "Kundennummer", "Hinzugefügt"};
        ObservableList<String> observableListCombobox = FXCollections.observableArrayList(auswahl);
        sortierenKunde.setStyle("-fx-text-fill: #ba8759");
        sortierenKunde.setItems(observableListCombobox);
        ObservableList<Kunde> observableList = FXCollections.observableArrayList();


        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                sortierenKunde.getValue();
                System.out.println(sortierenKunde.getValue());
                if (sortierenKunde.getValue() == "Nachname") {
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
                            observableList.removeAll();
                            listviewKunden.getItems().removeAll();
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (sortierenKunde.getValue() == "Vorname") {
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
                            observableList.removeAll();
                            listviewKunden.getItems().removeAll();
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                if (sortierenKunde.getValue() == "Kundennummer") {
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
                            observableList.removeAll();
                            listviewKunden.getItems().removeAll();
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                if (sortierenKunde.getValue() == "Hinzugefügt") {
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
                            observableList.removeAll();
                            listviewKunden.getItems().removeAll();
                            observableList.add(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
        sortierenKunde.setOnAction(event);


        listviewKunden.setItems(observableList);

        listviewKunden.setCellFactory(param -> new ListCell<Kunde>() {
            @Override
            protected void updateItem(Kunde item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getOrt() == null) {
                    setText(null);
                } else {
                    setText(item.getKundenummer() + item.getNachname() + item.getVorname() + item.getAdresse() + item.getPostleitzahl() + item.getOrt() + item.geteMail() + item.getNatelnummer());
                }
            }
        });
        int a = 1;
        for (int i = 0; i < 1; i++) {
            Kunde kunde = (Kunde) listviewKunden.getItems();
            kunde.getNatelnummer();
            kunde.getKundenummer();
            System.out.println(kunde.getKundenummer() + kunde.getNatelnummer());
        }
    }


    @FXML
    public void submiiit() {
        Button button = new Button();
        Pane pane = new Pane(button);
        pane.getChildren();
        pane.setPrefSize(212, 193);

        gridpaneArtikel.setGridLinesVisible(true);


        gridpaneArtikel.add(button, 0, 0);
        gridpaneArtikel.add(pane, 1, 0);
        gridpaneArtikel.add(pane, 2, 0);
        gridpaneArtikel.add(pane, 3, 0);
        gridpaneArtikel.add(pane.getParent(), 0, 1);
        gridpaneArtikel.add(pane, 1, 1);
        gridpaneArtikel.add(pane, 2, 1);
        gridpaneArtikel.add(pane, 3, 1);
        gridpaneArtikel.add(pane, 0, 2);
        gridpaneArtikel.add(pane, 1, 2);
        gridpaneArtikel.add(pane, 2, 2);
        gridpaneArtikel.add(pane, 3, 2);
    }
}





