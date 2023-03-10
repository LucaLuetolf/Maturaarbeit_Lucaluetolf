package lucaluetolf.maturaarbeit_lucaluetolf;

import java.io.File;
import java.nio.file.Files;

public class AllgemeineMethoden {

    public static void bildKopieren(){
        String BildPfad = "C:\\Users\\Luca Schule\\Test.txt";
        String Ort="C:\\Users\\Luca Schule\\Maturaarbeit_LucaLuetolf\\src\\main\\Bilder\\Artikel";
        File sourceFile = new File(BildPfad);
        File destinationFile = new File(Ort+sourceFile.getName());   //Creating A Destination File. Name stays the same this way, referring to getName()
        try
        {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());

        } catch(Exception e)
        {
            System.out.println(e);  // printing in case of error.
        }
    }
}
