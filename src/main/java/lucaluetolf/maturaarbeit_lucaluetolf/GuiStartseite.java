package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.util.ResourceBundle;


public class GuiStartseite extends GuiLeiste implements Initializable{
    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

    @FXML
    private Label labelWillkommen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int time1 = Integer.parseInt(formatter.format(time));
        if (time1 >= 6 && time1 < 11){
            labelWillkommen.setText("Good Morning");
        }
        if (time1 >= 11 && time1 < 13){
            labelWillkommen.setText("Good Noon");
        }
        if (time1 >= 13 && time1 < 17){
            labelWillkommen.setText("Good Afternoon");
        }
        if (time1 >= 17 && time1 < 22){
            labelWillkommen.setText("Good Evening");
        }
        if (time1 >= 22 && time1 < 6){
            labelWillkommen.setText("Good Night");
        }
    }
}
