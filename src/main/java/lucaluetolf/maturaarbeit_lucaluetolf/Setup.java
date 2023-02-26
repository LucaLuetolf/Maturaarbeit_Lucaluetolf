package lucaluetolf.maturaarbeit_lucaluetolf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Setup{

    private static File kunden = new File("src/main/Textdokumente/kunden");
    public static Kunde[] arrayKunde = new Kunde[10];
    public static File artikel = new File("src/main/Textdokumente/artikel");
    public static Artikel[] arrayArtikel = new Artikel[10];

    public static void setupKunden(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(kunden);
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
            String natelnummer = scanner.next();
            arrayKunde[a] = new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer);
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
    public static void speichernKunde(){
        int a = 0;
        try {
            FileWriter writer = new FileWriter(kunden);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < arrayKunde.length-1; i++) {

            if (arrayKunde[a] != null) {
                try {
                    FileWriter writer = new FileWriter(kunden, true);
                    writer.write(arrayKunde[a].getKundenummer() + " " + arrayKunde[a].getNachname() + " " + arrayKunde[a].getVorname() + " " + arrayKunde[a].getAdresse() + " " + arrayKunde[a].getPostleitzahl() + " " + arrayKunde[a].getOrt() + " " + arrayKunde[a].geteMail() + " " + arrayKunde[a].getNatelnummer() + '\n');
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Setup.arrayKunde[a].getNachname();
            }
            a++;
        }
    }

    public static void speichernArtikel(){
        int a = 0;
        try {
            FileWriter writer = new FileWriter(artikel);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < arrayKunde.length-1; i++) {

            if (arrayKunde[a] != null) {
                try {
                    FileWriter writer = new FileWriter(artikel, true);
                    writer.write(arrayArtikel[a].getArtikelnummer() + " " + arrayArtikel[a].getName() + " " + arrayArtikel[a].getMenge() + " " + arrayArtikel[a].getPreis() + " " + arrayArtikel[a].getLagerbestand() + '\n');
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            a++;
        }
    }
}
