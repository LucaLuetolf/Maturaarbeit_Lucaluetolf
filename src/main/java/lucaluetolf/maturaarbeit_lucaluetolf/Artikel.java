package lucaluetolf.maturaarbeit_lucaluetolf;

public class Artikel {

    int artikelnummer;
    String name;
    double menge;
    float preis;
    int lagerbestand;
    double anzahl;
    int rabatt;

    public Artikel(int artikelnummer, String name, double menge, float preis, int lagerbestand, double anzahl, int rabatt) {
        this.artikelnummer = artikelnummer;
        this.name = name;
        this.menge = menge;
        this.preis = preis;
        this.lagerbestand = lagerbestand;
        this.anzahl = anzahl;
        this.rabatt = rabatt;
    }

    public int getArtikelnummer() {
        return artikelnummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMenge() {
        return menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public float getPreis() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis = preis;
    }

    public int getLagerbestand() {
        return lagerbestand;
    }

    public void setLagerbestand(int lagerbestand) {
        this.lagerbestand = lagerbestand;
    }

    public double getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(double anzahl) {
        this.anzahl = anzahl;
    }

    public int getRabatt() {
        return rabatt;
    }

    public void setRabatt(int rabatt) {
        this.rabatt = rabatt;
    }
}
