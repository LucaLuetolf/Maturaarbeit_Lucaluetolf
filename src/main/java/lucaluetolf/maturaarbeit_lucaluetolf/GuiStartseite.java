package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class GuiStartseite extends GuiTaskleiste implements Initializable {
    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

    @FXML
    private Label labelWillkommen;
    @FXML
    private Label labelName;
    @FXML
    private Label labelBesterArtikel;
    @FXML
    private LineChart<String, Integer> lineChart;
    XYChart.Series<String, Integer> series;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int time1 = Integer.parseInt(formatter.format(time));
        if (time1 >= 6 && time1 < 11) {
            labelWillkommen.setText("Good Morning");
        }
        if (time1 >= 11 && time1 < 13) {
            labelWillkommen.setText("Good Noon");
        }
        if (time1 >= 13 && time1 < 17) {
            labelWillkommen.setText("Good Afternoon");
        }
        if (time1 >= 17 && time1 < 22) {
            labelWillkommen.setText("Good Evening");
        }
        if (time1 >= 22 || time1 < 6) {
            labelWillkommen.setText("Good Night");
        }

        try {
            ResultSet resultSet = statement.executeQuery("SELECT unternehmensname FROM unternehmen");
            resultSet.next();
            labelName.setText(resultSet.getString(1));
            resultSet.close();
        } catch (SQLException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }

        try {
            ResultSet resultSetBestseller = statement.executeQuery("SELECT COUNT(artikel_id) FROM verkaufteStueck");
            resultSetBestseller.next();
            int sum = resultSetBestseller.getInt(1);
            resultSetBestseller.close();
            if(sum == 0){
                labelBesterArtikel.setText("bisher wurde kein Artikel verkauft");
            }
            else{
                lineChart.getXAxis().setAnimated(false);
                lineChart.getYAxis().setAnimated(false);
                series = new XYChart.Series<>();
                ResultSet resultsetArtikelId = statement.executeQuery("SELECT artikel_id, SUM(Anzahl) AS Gesamtverkauf FROM verkaufteStueck GROUP BY Artikel_id ORDER BY Gesamtverkauf DESC");
                resultsetArtikelId.next();
                int artikelId = resultsetArtikelId.getInt(1);
                labelBesterArtikel.setAlignment(Pos.CENTER);
                resultsetArtikelId.close();
                ResultSet resultsetName = statement.executeQuery("SELECT name FROM artikel WHERE artikelId = " + artikelId);
                resultsetName.next();
                String name = resultsetName.getString(1);
                labelBesterArtikel.setText("Bestseller: " + name);
                resultsetName.close();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM verkaufteStueck WHERE artikel_id = " + artikelId + " AND anzahl != 0 ORDER BY datum ASC");
                while (resultSet.next()){
                    series.getData().add(new XYChart.Data<String, Integer>(resultSet.getDate("datum").toString(), resultSet.getInt("anzahl")));
                }
                resultSet.close();
                lineChart.getData().add(series);
            }

        } catch (Exception e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
    }

    @FXML
    protected void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Möchten sie sich ausloggen?");
        ButtonType ja = new ButtonType("ja");
        ButtonType nein = new ButtonType("nein");
        alert.getButtonTypes().setAll(ja, nein);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ja) {
            try {
                root = FXMLLoader.load(getClass().getResource("login.fxml"));
            } catch (IOException e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            alert.close();
        }
    }


    @FXML
    protected void toSceneEinstellungen(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("einstellungen.fxml"));
        } catch (IOException e) {
            AllgemeineMethoden.fehlermeldung(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
