package lucaluetolf.maturaarbeit_lucaluetolf;

import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.Files;

public class AllgemeineMethoden {

    public static void dateiKopieren(String Pfad) {
        String Ort = "C:\\Users\\Luca Schule\\Maturaarbeit_LucaLuetolf\\src\\main\\Bilder\\Artikel";
        File sourceFile = new File(Pfad);
        File destinationFile = new File(Ort + sourceFile.getName());
        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());

        } catch (Exception e) {
            new RuntimeException(e);
        }
    }
    public static void ordnerErstellen(String kundennummer, String nachname, String vorname){
        String pfad = "Rechnungen\\"+kundennummer + " " + nachname + " " + vorname;
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
