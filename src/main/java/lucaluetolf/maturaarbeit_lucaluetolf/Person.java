package lucaluetolf.maturaarbeit_lucaluetolf;

public class Person {
    private String nachname;
    private String vorname;
    private String adresse;
    private int postleitzahl;
    private String ort;
    private String eMail;
    private int natelnummer;

    public Person(String nachname, String vorname, String adresse, int postleitzahl, String ort, String eMail, int natelnummer) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.adresse = adresse;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.eMail = eMail;
        this.natelnummer = natelnummer;
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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public int getNatelnummer() {
        return natelnummer;
    }

    public void setNatelnummer(int natelnummer) {
        this.natelnummer = natelnummer;
    }
}
