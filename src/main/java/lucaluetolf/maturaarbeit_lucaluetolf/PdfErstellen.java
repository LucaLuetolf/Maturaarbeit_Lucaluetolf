package lucaluetolf.maturaarbeit_lucaluetolf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class PdfErstellen {
    //Linkedlist
    public static LinkedList linkedlistAbsenderAdressat = new LinkedList<>();
    public static LinkedList<Artikel> linkedlistBestellung = new LinkedList<>();
    public static LinkedList<Table> linkedlistTabelleBestellungmR = new LinkedList<>();
    public static LinkedList<Table> linkedlistTabelleBestellungoR = new LinkedList<>();

    //Pfade
    private static String pfadLogo = "C:\\Users\\Luca Schule\\OneDrive - sluz\\Desktop\\Bild.jpeg";
    private static String pfadQRCode = "";
    private static String pfadRechnung = "Rechnungen\\test3.pdf";

    public static String getPfadLogo() {
        return pfadLogo;
    }

    public static void setPfadLogo(String pfadLogo) {
        PdfErstellen.pfadLogo = pfadLogo;
    }

    public static String getPfadQRCode() {
        return pfadQRCode;
    }

    public static void setPfadQRCode(String pfadQRCode) {
        PdfErstellen.pfadQRCode = pfadQRCode;
    }

    public static String getPfadRechnung() {
        return pfadRechnung;
    }

    public static void setPfadRechnung(String pfadRechnung) {
        PdfErstellen.pfadRechnung = pfadRechnung;
    }

    //Rechnungsnummer
    private static int rechnungsnummer;
    public static int getRechnungsnummer() {
        return rechnungsnummer;
    }
    public static void setRechnungsnummer(int rechnungsnummer) {
        PdfErstellen.rechnungsnummer = rechnungsnummer;
    }


    public static void layout1(){
        //nur für Test
        Kunde kunde1 = new Kunde(1234, "Mustermann", "Max", "Testweg 3", 6207, "Nottwil", "Max.mustermann@bluewin.ch", "123456789");
        Mitarbeiter mitarbeiter1 = new Mitarbeiter(1234, "Mustermann", "Max", "Testweg 3", 6207, "Nottwil", "Max.mustermann@bluewin.ch", "123456789", true, 1234);
        for (int i = 0; i < Setup.linkedlistArtikel.size(); i++) {
            linkedlistBestellung.add(Setup.linkedlistArtikel.get(i));
            linkedlistBestellung.get(i).setAnzahl(1);
        }
        PdfErstellen.linkedlistAbsenderAdressat.add(mitarbeiter1);
        PdfErstellen.linkedlistAbsenderAdressat.add(kunde1);

        //Grössen der Zellen
        float ganzeseite = 570F;
        float absender1 = 100F;
        float absender2 = 223F;
        float adressat = 143F;

        //Bestellung
        float bezeichnungmR = 207F; //mR = mit Rabatt
        float bezeichnungoR = 267F; //oR = ohne Rabatt
        float artikelnummer = 79F;
        float menge = 79F;
        float preis = 70F;
        float rabatt = 60;
        float gesamt = 75F;

        //Grössen für Tabelle
        float ganzeseite1[] = {ganzeseite};
        float absenderAdressat[] = {absender1, absender2, adressat};
        float bestellungmR[] = {bezeichnungmR, artikelnummer, menge, preis, rabatt, gesamt};
        float bestellungoR[] = {bezeichnungoR, artikelnummer, menge, preis, gesamt};

        //Tabellen
        Table absatz = new Table(ganzeseite1);
        Table tabelleAbsenderAdressat = new Table(absenderAdressat);
        Table tabelleTitelBestellungoR = new Table(bestellungoR);
        Table tabelleTitelBestellungmR = new Table(bestellungmR);

        //Absatz
        absatz.addCell(new Cell().add(new ListItem("\n")).setBorder(Border.NO_BORDER));

        //Pdf Definieren
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(pfadRechnung);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfDocument rechnung = new PdfDocument(writer);
        rechnung.setDefaultPageSize(PageSize.A4);
        Document document = new Document(rechnung);

        //Bilder
        ImageData datenLogo = null;
        try {
            datenLogo = ImageDataFactory.create(getPfadLogo());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image logo = new Image(datenLogo);
        logo.setHeight(60F);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(logo);
        document.add(absatz).add(absatz);

        /*ImageData datenQRCode = null;
        try {
            datenQRCode = ImageDataFactory.create(getPfadQRCode());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image QRCode = new Image(datenQRCode);*/

        //Zeit
        LocalDateTime datum = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy"); //EEEE für Wochentag

        //Absender und Adressat
        Mitarbeiter mitarbeiter = (Mitarbeiter) PdfErstellen.linkedlistAbsenderAdressat.get(0);
        Kunde kunde = (Kunde) PdfErstellen.linkedlistAbsenderAdressat.get(1);

        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Datum")).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(formatter.format(datum))).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(kunde.getNachname() + " " + kunde.getVorname())).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Kundennummer")).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(String.valueOf(kunde.getKundenummer()))).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(kunde.getAdresse())).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Verkäufer")).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(mitarbeiter.getNachname() + " " + mitarbeiter.getVorname())).setBorder(Border.NO_BORDER)));
        tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(kunde.getPostleitzahl() + " " + kunde.getOrt())).setBorder(Border.NO_BORDER)));
        document.add(tabelleAbsenderAdressat);
        document.add(absatz);

        //Paragraph Rechnungsnummer
        Paragraph paragraphRechnungsnummer = new Paragraph("Rechnungsnummer " + getRechnungsnummer()).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();
        document.add(paragraphRechnungsnummer);
        document.add(absatz);

        //Bestellung
        tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Bezeichnung")).setBorder(Border.NO_BORDER).setBold());
        tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Artikel-NR")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Anzahl")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Preis")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Rabatt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungmR.addCell(new Cell().add(new ListItem("Gesamt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));

        tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Bezeichnung")).setBorder(Border.NO_BORDER).setBold());
        tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Artikel-NR")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Anzahl")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Preis")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        tabelleTitelBestellungoR.addCell(new Cell().add(new ListItem("Gesamt")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        double totalArtikel;
        double uebertrag = 0;
        double preisInklRabatt;
        int rabatt1;

        int seitenZaehler = 0;
        int zeilenZaehler = 0;
        boolean rabatttester = false;
        linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
        linkedlistTabelleBestellungoR.add(new Table(bestellungoR));

        for (int i = 0; i < linkedlistBestellung.size(); i++) {
            if (linkedlistBestellung.get(i).getRabatt() != 0){
                rabatt1 = 100 - linkedlistBestellung.get(i).getRabatt();
                preisInklRabatt = linkedlistBestellung.get(i).getPreis() / 100 * rabatt1;
                totalArtikel = preisInklRabatt * linkedlistBestellung.get(i).getAnzahl();
            }
            else{
                totalArtikel = linkedlistBestellung.get(i).getPreis() * linkedlistBestellung.get(i).getAnzahl();
            }
            totalArtikel = Math.round(20.00 * totalArtikel) / 20.00; //TODO Zwei Kommastellen
            uebertrag = uebertrag + totalArtikel; //TODO Überprüfen

            if (seitenZaehler == 0){
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(linkedlistBestellung.get(i).getName())).setBorder(Border.NO_BORDER));
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getArtikelnummer()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getAnzahl()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getPreis()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                if (linkedlistBestellung.get(seitenZaehler).getRabatt() == 0) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                } else {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getRabatt()) + "%")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    rabatttester = true;
                }
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(linkedlistBestellung.get(i).getName())).setBorder(Border.NO_BORDER));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getArtikelnummer()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getAnzahl()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getPreis()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                zeilenZaehler = zeilenZaehler+1;
                if (zeilenZaehler == 16){
                    seitenZaehler = 1;
                    linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
                    linkedlistTabelleBestellungoR.add(new Table(bestellungoR));
                }
            }
            if (seitenZaehler == 1){
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(linkedlistBestellung.get(i).getName())).setBorder(Border.NO_BORDER));
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getArtikelnummer()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getAnzahl()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getPreis()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                if (linkedlistBestellung.get(seitenZaehler).getRabatt() == 0) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                } else {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(linkedlistBestellung.get(i).getRabatt() + "%")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    rabatttester = true;
                }
                linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(linkedlistBestellung.get(i).getName())).setBorder(Border.NO_BORDER));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getArtikelnummer()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getAnzahl()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(linkedlistBestellung.get(i).getPreis()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                if (zeilenZaehler == 32){
                    seitenZaehler = seitenZaehler +1;
                    zeilenZaehler = 0;
                    linkedlistTabelleBestellungmR.addLast(new Table(bestellungmR));
                    linkedlistTabelleBestellungoR.addLast(new Table(bestellungoR));
                }
            }
        }
        if (rabatttester == true){
            for (int i = 0; i < seitenZaehler+1; i++) {
                document.add(tabelleTitelBestellungmR);
                document.add(absatz);
                document.add(linkedlistTabelleBestellungmR.get(i));
            }
            seitenZaehler = 0;
            rabatttester = false;
        }else{
            for (int i = 0; i < seitenZaehler+1; i++) {
                document.add(tabelleTitelBestellungoR);
                document.add(absatz);
                document.add(linkedlistTabelleBestellungoR.get(i));
            }
            seitenZaehler = 0;
        }
        document.close();
        System.out.println("pdf generated");

    }
}