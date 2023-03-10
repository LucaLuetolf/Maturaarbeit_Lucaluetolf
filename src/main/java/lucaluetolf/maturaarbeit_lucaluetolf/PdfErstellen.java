package lucaluetolf.maturaarbeit_lucaluetolf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Table;

import java.io.FileNotFoundException;

public class PdfErstellen {

    public static void main(String[] args) throws FileNotFoundException {

        int rechnungsnummer = 0;

        String pfad = "Rechnungen\\Kunde1\\" + rechnungsnummer+ ".pdf";
        PdfWriter writer = new PdfWriter(pfad);
        PdfDocument rechnung = new PdfDocument(writer);
        rechnung.setDefaultPageSize(PageSize.A4);
        Document document = new Document(rechnung);

        float ganzeseite = 570F;
        float halbeseite = 285F;
        float drittel = 190F;
        float viertel = 142.5F;
        float einezeile[] = {ganzeseite};
        float zweizeilen[] = {halbeseite,halbeseite};
        float dreizeilen[] = {drittel, drittel, drittel};
        float vierzeilen[] = {viertel, viertel, viertel, viertel};


        Table eins = new Table(einezeile);
        Table zwei = new Table(zweizeilen);
        Table drei = new Table(dreizeilen);
        Table vier = new Table(vierzeilen);

        eins.addCell(new Cell().add(new ListItem("570")).setFontSize(20).setBold());
        eins.addCell(new Cell().add(new ListItem("570")).setFontSize(20).setBold());
        zwei.addCell(new Cell().add(new ListItem("285")).setFontSize(20).setBold());
        zwei.addCell(new Cell().add(new ListItem("285")).setFontSize(20).setBold());
        drei.addCell(new Cell().add(new ListItem("190")).setFontSize(20).setBold());
        drei.addCell(new Cell().add(new ListItem("190")).setFontSize(20).setBold());
        drei.addCell(new Cell().add(new ListItem("190")).setFontSize(20).setBold());
        vier.addCell(new Cell().add(new ListItem("142.5")).setFontSize(20).setBold());
        vier.addCell(new Cell().add(new ListItem("142.5")).setFontSize(20).setBold());
        vier.addCell(new Cell().add(new ListItem("142.5")).setFontSize(20).setBold());
        vier.addCell(new Cell().add(new ListItem("142.5")).setFontSize(20).setBold());

        document.add(eins);
        document.add(zwei);
        document.add(drei);
        document.add(vier);
        document.close();
        String pdf = "pdf generated";
        System.out.println(pdf);

    }
}
