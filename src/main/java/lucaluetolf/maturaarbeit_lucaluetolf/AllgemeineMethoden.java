package lucaluetolf.maturaarbeit_lucaluetolf;

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
            System.out.println(e);
        }
    }
    public static void ordnerErstellen(String kundennummer, String nachname, String vorname){
        String pfad = "Rechnungen\\"+kundennummer + " " + nachname + " " + vorname;
        File ordner = new File(pfad);
        boolean ordner1 = ordner.mkdirs();
        if(ordner1){
            System.out.println("Ordner wurde erstellt");
        }else{
            System.out.println("Fehler beim erstellen des Ordners");
        }
    }
}
