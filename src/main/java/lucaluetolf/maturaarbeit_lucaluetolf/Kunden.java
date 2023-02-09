package lucaluetolf.maturaarbeit_lucaluetolf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Kunden {

    private static int kundennummer;
    private static String nachname;
    private static String vorname;
    private static String adresse;
    private static int postleitzahl;
    private static String ort;
    private static String email;
    private static String handynummer;
    private static int groesseArray;

    private static File kundenregister = new File("src/main/Textdokumente/kunden");

    public static Kunden[] array = new Kunden[10];
    private static int position = 0;

    public Kunden(int kundennummer, String nachname, String vorname, String adresse, int postleitzahl, String ort, String email, String handynummer) {
        this.kundennummer = kundennummer;
        this.nachname = nachname;
        this.vorname = vorname;
        this.adresse = adresse;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.email = email;
        this.handynummer = handynummer;
    }

    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(int postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandynummer() {
        return handynummer;
    }

    public void setHandynummer(String handynummer) {
        this.handynummer = handynummer;
    }


    public static void setupKunden(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(kundenregister);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int a = 0;

        while (scanner.hasNext()) {

            kundennummer = scanner.nextInt();
            nachname = scanner.next();
            vorname = scanner.next();
            adresse = scanner.next();
            postleitzahl = scanner.nextInt();
            ort = scanner.next();
            email = scanner.next();
            handynummer = scanner.next();
            array[a] = new Kunden(kundennummer, nachname, vorname, adresse, postleitzahl, ort, email, handynummer);
            a++;
            //problem --> Kunde wird überschrieben
        }
    }
    public static void artikelnummerPruefer(){
    }





    public static void tester(){
        if (array[5] == null){
            System.out.println("null");
        }
        else {System.out.println("Dieser Platz ist besetzt");}
    }
}
