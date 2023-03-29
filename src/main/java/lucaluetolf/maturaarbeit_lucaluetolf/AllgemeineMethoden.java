package lucaluetolf.maturaarbeit_lucaluetolf;

import java.io.File;
import java.nio.file.Files;

public class AllgemeineMethoden {

    public static void dateiKopieren() {
        String Pfad = "C:\\Users\\Luca Schule\\Test.txt";
        String Ort = "C:\\Users\\Luca Schule\\Maturaarbeit_LucaLuetolf\\src\\main\\Bilder\\Artikel";
        File sourceFile = new File(Pfad);
        File destinationFile = new File(Ort + sourceFile.getName());
        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void ordnerErstellen(){
        String pfad = "C:\\Users\\Luca Schule\\Maturaarbeit_LucaLuetolf\\Rechnungen\\test";
        File ordner = new File(pfad);
        boolean ordner1 = ordner.mkdirs();
        if(ordner1){
            System.out.println("Ordner wurde erstellt");
        }else{
            System.out.println("Fehler beim erstellen des Ordners");
        }
    }
}
