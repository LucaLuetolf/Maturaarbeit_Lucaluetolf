package lucaluetolf.maturaarbeit_lucaluetolf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Setup{

    //TODO Files:
    private static File kunden = new File("src/main/Textdokumente/kunden");
    public static File artikel = new File("src/main/Textdokumente/artikel");
    public static File mitarbeiter = new File("src/main/Textdokumente/mitarbeiter");


    //TODO Linkedlist:
    public static LinkedList<Kunde> linkedlistKunde = new LinkedList<>();
    public static LinkedList<Artikel> linkedlistArtikel = new LinkedList<>();
    public static LinkedList<Mitarbeiter> linkedlistMitarbeiter = new LinkedList<>();

    //TODO Setup_Methoden:
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
            int natelnummer = scanner.nextInt();
            linkedlistKunde.addLast(new Kunde(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer));
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
        double menge1;
        float preis1;

        while(scanner.hasNext()){

            int artikelnummer = scanner.nextInt();
            String name = scanner.next();
            String menge = scanner.next();
            String preis = scanner.next();
            int lagerbestand = scanner.nextInt();
            int artikel = 0;
            int rabatt = scanner.nextInt();
            menge1 = Double.parseDouble(menge);
            preis1 = Float.parseFloat(preis);

            linkedlistArtikel.addLast(new Artikel(artikelnummer, name, menge1, preis1, lagerbestand, artikel, rabatt));
            a++;
        }
    }
    public static void setupMitarbeiter(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(mitarbeiter);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int a = 0;
        while(scanner.hasNext()) {
            int mitarbeiternummer = scanner.nextInt();
            String nachname = scanner.next();
            String vorname = scanner.next();
            String adresse = scanner.next();
            int postleitzahl = scanner.nextInt();
            String ort = scanner.next();
            String email = scanner.next();
            int natelnummer = scanner.nextInt();
            Boolean erstanmeldung = scanner.nextBoolean();
            int pin = scanner.nextInt();
            linkedlistMitarbeiter.addLast(new Mitarbeiter(mitarbeiternummer, nachname, vorname, adresse, postleitzahl, ort, email, natelnummer, erstanmeldung, pin));
            a++;
        }
    }

    //TODO Speichern-Methoden:
    public static void speichernKunde(){
        int a = 0;
        try {
            FileWriter writer = new FileWriter(kunden);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < linkedlistKunde.size(); i++) {

            if (linkedlistKunde.get(a) != null) {
                try {
                    FileWriter writer = new FileWriter(kunden, true);
                    writer.write(linkedlistKunde.get(a).getKundenummer() + " " + linkedlistKunde.get(a).getNachname() + " " + linkedlistKunde.get(a).getVorname() + " " + linkedlistKunde.get(a).getAdresse() + " " + linkedlistKunde.get(a).getPostleitzahl() + " " + linkedlistKunde.get(a).getOrt() + " " + linkedlistKunde.get(a).geteMail() + " " + linkedlistKunde.get(a).getNatelnummer() + '\n');
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        String string = "";
        for (int i = 0; i < linkedlistArtikel.size(); i++) {

            if (linkedlistArtikel.get(a) != null) {
                try {
                    FileWriter writer = new FileWriter(artikel, true);
                    string = String.valueOf(linkedlistArtikel.get(i).getMenge());
                    string.replace(".", ",");
                    writer.write(linkedlistArtikel.get(a).getArtikelnummer() + " " + linkedlistArtikel.get(a).getName() + " " +string+ " " + linkedlistArtikel.get(a).getPreis() + " " + linkedlistArtikel.get(a).getLagerbestand() +  " " + linkedlistArtikel.get(a).getRabatt() + '\n');
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            a++;
        }
    }
    public static void speichernMitarbeiter(){
        int a = 0;
        try {
            FileWriter writer = new FileWriter(mitarbeiter);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < linkedlistMitarbeiter.size(); i++) {
            if (linkedlistMitarbeiter.get(a) != null) {

                try {
                    FileWriter writer = new FileWriter(mitarbeiter, true);
                    writer.write(linkedlistMitarbeiter.get(a).getMitarbeiternummer() + " " + linkedlistMitarbeiter.get(a).getNachname() + " " + linkedlistMitarbeiter.get(a).getVorname() + " " + linkedlistMitarbeiter.get(a).getAdresse() + " " + linkedlistMitarbeiter.get(a).getPostleitzahl() + " " + linkedlistMitarbeiter.get(a).getOrt() + " " + linkedlistMitarbeiter.get(a).geteMail() + " " + linkedlistMitarbeiter.get(a).getNatelnummer() + " " + linkedlistMitarbeiter.get(a).isErstanmeldung() + " " + linkedlistMitarbeiter.get(a).getPin() + '\n');
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }
            a++;
        }
    }
}