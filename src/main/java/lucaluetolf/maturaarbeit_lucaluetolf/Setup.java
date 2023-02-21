package lucaluetolf.maturaarbeit_lucaluetolf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Setup{

    private static File kundenregister = new File("src/main/Textdokumente/kunden");
    public static Kunde[] arrayKunde = new Kunde[10];
    public static File artikel = new File("src/main/Textdokumente/artikel");
    public static Artikel[] arrayArtikel = new Artikel[10];

    public static void setupKunden(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(kundenregister);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int a = 0;

        while (scanner.hasNext()) {

            int kundennummer = scanner.nextInt();
            String nachname = scanner.next();
            String vorname = scanner.next();
            String adresse = scanner.next();
            int postleitzahl = scanner.nextInt();
            String ort = scanner.next();
            String email = scanner.next();
            String handynummer = scanner.next();
            arrayKunde[a] = new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, handynummer);
            a++;
        }
    }

    public static void setupArtikel(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(artikel);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int a = 0;

        while(scanner.hasNext()){

            int artikelnummer = scanner.nextInt();
            String name = scanner.next();
            double menge = scanner.nextDouble();
            float preis = scanner.nextFloat();
            int lagerbestand = scanner.nextInt();
            arrayArtikel[a] = new Artikel(artikelnummer, name, menge, preis, lagerbestand);
            a++;
        }
    }
}
