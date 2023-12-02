package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.Files;

public class AllgemeineMethoden {

    public static void dateiKopieren(String Pfad, String ort) {
        File sourceFile = new File(Pfad);
        File destinationFile = new File(ort + sourceFile.getName());
        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
        } catch (Exception e) {
            fehlermeldung(e);
        }
    }
    public static void ordnerErstellen(String pfad){
        File ordner = new File(pfad);
        ordner.mkdirs();
    }

    public static void fehlermeldung(Exception e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Leider ist ein Fehler aufgetreten");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

}
