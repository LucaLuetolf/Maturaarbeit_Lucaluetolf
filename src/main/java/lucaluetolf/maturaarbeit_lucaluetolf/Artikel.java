package lucaluetolf.maturaarbeit_lucaluetolf;

public class Artikel {

    int artikelnummer;
    String name;
    double menge;
    float preis;
    int lagerbestand;

    public Artikel(int artikelnummer, String name, double menge, float preis, int lagerbestand) {
        this.artikelnummer = artikelnummer;
        this.name = name;
        this.menge = menge;
        this.preis = preis;
        this.lagerbestand = lagerbestand;
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
}
