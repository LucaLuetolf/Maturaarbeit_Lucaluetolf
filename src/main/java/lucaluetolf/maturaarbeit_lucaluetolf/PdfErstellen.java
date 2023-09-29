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
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class PdfErstellen {

    static Statement statement;

    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Linkedlist
    //public static LinkedList linkedlistAbsenderAdressat = new LinkedList<>();
    //public static LinkedList<Artikel> linkedlistBestellung = new LinkedList<>();
    public static LinkedList<Table> linkedlistTabelleBestellungmR = new LinkedList<>();
    public static LinkedList<Table> linkedlistTabelleBestellungoR = new LinkedList<>();

    //Pfade
    private static String pfadLogo = "C:\\Users\\Luca Schule\\OneDrive - sluz\\Desktop\\Bild.jpeg";

    private static String pfadRechnung = "Rechnungen\\test3.pdf";


    public static void layout1(int rechnung1Quittung2) {
        Statement statement;
        {
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/Maturaarbeit", "User", "database");
                statement = connection.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


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

        //Zeit
        LocalDateTime datum = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy"); //EEEE für Wochentag
        DateTimeFormatter formatterPfad = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        //Pdf Definieren
        PdfWriter writer = null;
        try {
            ResultSet resultSetAdresse = statement.executeQuery("SELECT * FROM unternehmen, kunden, bearbeiter WHERE rechnungsnummer = bestellung_id AND kundenId = kunden_id");
            if (resultSetAdresse.next()) {
                writer = new PdfWriter("Rechnungen\\" + resultSetAdresse.getInt("kundenId") + " " + resultSetAdresse.getString("nachname") + " " + resultSetAdresse.getString("vorname") + "\\" + formatterPfad.format(datum) + "_" + resultSetAdresse.getInt("rechnungsnummer") + ".pdf");

            }
            resultSetAdresse.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PdfDocument rechnung = new PdfDocument(writer);
        rechnung.setDefaultPageSize(PageSize.A4);
        Document document = new Document(rechnung);

        //Bilder
        //TODO ResultSet Pfad Logo
        ImageData datenLogo = null;
        try {
            datenLogo = ImageDataFactory.create("C:\\Users\\Luca Schule\\OneDrive - sluz\\Desktop\\Bild.jpeg");
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


        try {
            ResultSet resultSetKunde = statement.executeQuery("SELECT * FROM kunden, bearbeiter, unternehmen WHERE bestellung_id = rechnungsnummer and kundenId = kunden_id");
            resultSetKunde.next();
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Datum")).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(formatter.format(datum))).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getString("nachname") + " " + resultSetKunde.getString("vorname")))).setBorder(Border.NO_BORDER));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Kundennummer")).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(String.valueOf(resultSetKunde.getInt("kundenId")))).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getString("adresse"))).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem("Verkäufer")).setBorder(Border.NO_BORDER)));
            //tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem( + " " + mitarbeiter.getVorname())).setBorder(Border.NO_BORDER)));
            tabelleAbsenderAdressat.addCell((new Cell().add(new ListItem(resultSetKunde.getInt("postleitzahl") + " " + resultSetKunde.getString("ort"))).setBorder(Border.NO_BORDER)));
            document.add(tabelleAbsenderAdressat);
            document.add(absatz);


            //TODO Quittung
            //Paragraph Rechnungsnummer
            Paragraph paragraphQuittungRechnungsnummer = null;
            if (rechnung1Quittung2 == 1){
                paragraphQuittungRechnungsnummer = new Paragraph("Rechnungsnummer " + resultSetKunde.getInt("rechnungsnummer")).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            } else if (rechnung1Quittung2 == 2) {
                paragraphQuittungRechnungsnummer = new Paragraph("Quittung " + resultSetKunde.getInt("rechnungsnummer")).setBorder(Border.NO_BORDER).setFontSize(15F).setBold();

            }
            document.add(paragraphQuittungRechnungsnummer);
            document.add(absatz);

            resultSetKunde.close();

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


            ResultSet resultSetBestellung = statement.executeQuery("SELECT * FROM unternehmen, bestellung, artikel WHERE rechnungsnummer = bestellungId AND artikel_id = artikelId");

            double totalArtikel;
            double uebertrag = 0;
            double preisInklRabatt;
            double rabatt1;

            int seitenZaehler = 0;
            int zeilenZaehler = 0;
            boolean rabatttester = false;
            linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
            linkedlistTabelleBestellungoR.add(new Table(bestellungoR));


            while (resultSetBestellung.next()) {
                if (resultSetBestellung.getDouble("rabatt") != 0) {
                    rabatt1 = 100 - resultSetBestellung.getDouble("rabatt");
                    preisInklRabatt = resultSetBestellung.getDouble("preis") / 100 * rabatt1;
                    totalArtikel = preisInklRabatt * resultSetBestellung.getInt("anzahl");
                } else {
                    totalArtikel = resultSetBestellung.getDouble("preis") * resultSetBestellung.getInt("anzahl");
                }
                totalArtikel = Math.round(20.00 * totalArtikel) / 20.00; //TODO Zwei Kommastellen
                uebertrag = uebertrag + totalArtikel; //TODO Überprüfen

                if (seitenZaehler == 0) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    if (resultSetBestellung.getDouble("rabatt") == 0) {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                    } else {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("rabatt")) + "%")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                        rabatttester = true;
                    }
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    zeilenZaehler = zeilenZaehler + 1;
                    if (zeilenZaehler == 16) {
                        seitenZaehler = 1;
                        linkedlistTabelleBestellungmR.add(new Table(bestellungmR));
                        linkedlistTabelleBestellungoR.add(new Table(bestellungoR));
                    }
                }
                if (seitenZaehler == 1) {
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    if (resultSetBestellung.getDouble("rabatt") == 0) {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                    } else {
                        linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getDouble("rabatt") + "%")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                        rabatttester = true;
                    }
                    linkedlistTabelleBestellungmR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(resultSetBestellung.getString("name"))).setBorder(Border.NO_BORDER));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("artikelId")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getInt("anzahl")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(resultSetBestellung.getDouble("preis")))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    linkedlistTabelleBestellungoR.get(seitenZaehler).addCell(new Cell().add(new ListItem(String.valueOf(totalArtikel))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                    if (zeilenZaehler == 32) {
                        seitenZaehler = seitenZaehler + 1;
                        zeilenZaehler = 0;
                        linkedlistTabelleBestellungmR.addLast(new Table(bestellungmR));
                        linkedlistTabelleBestellungoR.addLast(new Table(bestellungoR));
                    }
                }
            }
            resultSetBestellung.close();
            if (rabatttester == true) {
                for (int i = 0; i < seitenZaehler + 1; i++) {
                    document.add(tabelleTitelBestellungmR);
                    document.add(absatz);
                    document.add(linkedlistTabelleBestellungmR.get(i));
                }
                seitenZaehler = 0;
                rabatttester = false;
            } else {
                for (int i = 0; i < seitenZaehler + 1; i++) {
                    document.add(tabelleTitelBestellungoR);
                    document.add(absatz);
                    document.add(linkedlistTabelleBestellungoR.get(i));
                }
                seitenZaehler = 0;
            }
            document.close();
            System.out.println("pdf generated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}