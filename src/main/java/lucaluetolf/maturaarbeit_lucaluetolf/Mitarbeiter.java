package lucaluetolf.maturaarbeit_lucaluetolf;

public class Mitarbeiter extends Person{

    private int mitarbeiternummer;
    private boolean erstanmeldung;
    private int pin;

    public Mitarbeiter(int mitarbeiternummer, String nachname, String vorname, String adresse, int postleitzahl, String ort, String eMail, String natelnummer, boolean erstanmeldung, int pin) {
        super(nachname, vorname, adresse, postleitzahl, ort, eMail, natelnummer);
        this.mitarbeiternummer = mitarbeiternummer;
        this.erstanmeldung = erstanmeldung;
        this.pin = pin;
    }

    public int getMitarbeiternummer() {
        return mitarbeiternummer;
    }

    public boolean isErstanmeldung() {
        return erstanmeldung;
    }

    public void setErstanmeldung(boolean erstanmeldung) {
        this.erstanmeldung = erstanmeldung;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}