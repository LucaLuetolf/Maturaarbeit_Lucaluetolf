package lucaluetolf.maturaarbeit_lucaluetolf;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GuiAnalyse extends GuiTaskleiste implements Initializable {
    @FXML
    private TextField textfieldArtikelnummer;
    @FXML
    private DatePicker datePickerVon;
    @FXML
    private DatePicker datePickerBis;
    @FXML
    private LineChart<String, Integer> lineChart;
    @FXML
    private CheckBox checkboxVergleich;
    @FXML
    private JFXButton buttonZuruecksetzten;
    @FXML
    private JFXButton buttonAnwenden;
    XYChart.Series<String, Integer> series;
    private boolean booleanArtikelnummer = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonZuruecksetzten.setDisable(true);
        lineChart.getXAxis().setAnimated(false);
        lineChart.getYAxis().setAnimated(false);
    }

    @FXML
    protected void textfieldArtikelnummerKey(){
        textfieldArtikelnummer.setText(textfieldArtikelnummer.getText().replaceAll("[^0-9]", ""));
        textfieldArtikelnummer.positionCaret(textfieldArtikelnummer.getLength());

        if (textfieldArtikelnummer.getLength() != 0 && textfieldArtikelnummer.getLength() <= 8){
            try {
                ResultSet resultsetArtikel = statement.executeQuery("SELECT COUNT(artikelId) AS summe FROM artikel WHERE artikelId = " + textfieldArtikelnummer.getText());
                resultsetArtikel.next();
                int res = resultsetArtikel.getInt("summe");
                if (res == 0){
                    textfieldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                }
                else{
                    booleanArtikelnummer = true;
                    textfieldArtikelnummer.setStyle("-fx-border-color: #7CFC00; -fx-border-radius: 3px");
                }
                resultsetArtikel.close();
            } catch (Exception e) {
                AllgemeineMethoden.fehlermeldung(e);
            }
        } else{
            textfieldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
        }

    }

    @FXML
    protected void anwenden(){
        buttonZuruecksetzten.setDisable(false);
        if (booleanArtikelnummer == true && datePickerVon.getValue() != null && datePickerBis.getValue() != null && (datePickerBis.getValue().isAfter(datePickerVon.getValue()) || datePickerBis.getValue().isEqual(datePickerVon.getValue()))){
            checkboxVergleich.setDisable(true);
            datePickerVon.setEditable(false);
            datePickerVon.setMouseTransparent(true);
            datePickerBis.setEditable(false);
            datePickerBis.setMouseTransparent(true);
            series = new XYChart.Series<>();
            series.setName(textfieldArtikelnummer.getText());
            if (checkboxVergleich.isSelected()){
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM verkaufteStueck WHERE artikel_id = " + textfieldArtikelnummer.getText() + " AND datum BETWEEN '" + datePickerVon.getValue() + "' AND '" + datePickerBis.getValue() + "' ORDER BY datum ASC");
                    while (resultSet.next()){
                        series.getData().add(new XYChart.Data<String, Integer>(resultSet.getDate("datum").toString(), resultSet.getInt("anzahl")));
                    }
                    resultSet.close();
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                lineChart.getData().add(series);
            } else{
                try {
                    textfieldArtikelnummer.setEditable(false);
                    buttonAnwenden.setDisable(true);

                    ResultSet resultSet = statement.executeQuery("SELECT * FROM verkaufteStueck WHERE artikel_id = " + textfieldArtikelnummer.getText() + " AND ANZAHL != 0 AND datum BETWEEN '" + datePickerVon.getValue() + "' AND '" + datePickerBis.getValue() + "' ORDER BY datum ASC");
                    while (resultSet.next()){
                        series.getData().add(new XYChart.Data<String, Integer>(resultSet.getDate("datum").toString(), resultSet.getInt("anzahl")));
                    }
                    resultSet.close();
                } catch (Exception e) {
                    AllgemeineMethoden.fehlermeldung(e);
                }
                lineChart.getData().add(series);
            }

        }else{
            if(booleanArtikelnummer == false){
                textfieldArtikelnummer.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if(datePickerVon.getValue() == null){
                datePickerVon.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if(datePickerBis.getValue() == null){
                datePickerBis.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
            if (datePickerVon.getValue().isAfter(datePickerBis.getValue())){
                datePickerVon.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
                datePickerBis.setStyle("-fx-border-color: #FF0000; -fx-border-radius: 3px");
            }
        }
    }


    @FXML
    protected void zuruecksetzten() {
        lineChart.getData().clear();
        series.getData().clear();
        checkboxVergleich.setSelected(false);

        buttonZuruecksetzten.setDisable(true);
        buttonAnwenden.setDisable(false);
        textfieldArtikelnummer.setText("");
        textfieldArtikelnummer.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        datePickerVon.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        datePickerBis.setStyle("-fx-border-color: #BABABA; -fx-border-radius: 3px");
        textfieldArtikelnummer.setEditable(true);
        checkboxVergleich.setDisable(false);
        booleanArtikelnummer = false;
        datePickerVon.setValue(null);
        datePickerBis.setValue(null);
        datePickerVon.setEditable(true);
        datePickerVon.setMouseTransparent(false);
        datePickerBis.setEditable(true);
        datePickerBis.setMouseTransparent(false);
    }

}
