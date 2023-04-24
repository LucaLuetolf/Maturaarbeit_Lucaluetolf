package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;


public class GuiStartseite extends GuiLeiste{
    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

    @FXML
    private Label labelWillkommen;

    @FXML
    public void explorer(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            labelWillkommen.setText(filePath);

            // Pfad in die Zwischenablage kopieren
            StringSelection selection = new StringSelection(filePath);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        }

        /*try {
            Runtime.getRuntime().exec("explorer C:\\");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        //https://stackoverflow.com/questions/11174055/open-up-windows-explorer-in-java
        //https://stackoverflow.com/questions/10966999/how-to-make-a-button-that-when-clicked-opens-the-appdata-directory/10967320#10967320
    }

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO nach Good ... Namen von Mitarbeiter + Zeiten Festlegen
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
    }*/
}
