package lucaluetolf.maturaarbeit_lucaluetolf;


public class Kunde extends Person{

    private int kundenummer;

    public Kunde(int kundenummer, String nachname, String vorname, String adresse, int postleitzahl, String ort, String eMail, int natelnummer) {
        super(nachname, vorname, adresse, postleitzahl, ort, eMail, natelnummer);
        this.kundenummer = kundenummer;
    }

    public int getKundenummer() {
        return kundenummer;
    }
}