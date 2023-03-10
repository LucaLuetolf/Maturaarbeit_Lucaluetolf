package lucaluetolf.maturaarbeit_lucaluetolf;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;

public class generate {
    public static void createpdf() throws FileNotFoundException {
        String path = "invoice.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        float threecol=190;
        float twocol=285;
        float twocol150 = twocol+150;
        float twocolumnWidth[]={twocol150,twocol};
        float fullwidth[]={threecol*3};

        Table table = new Table(twocolumnWidth);
        table.addCell(new Cell().add(new ListItem("invoice")).setFontSize(20).setBorder(Border.NO_BORDER).setBold());
        Table nestedtable = new Table(new float[]{twocol/2,twocol/2});
        nestedtable.addCell(new Cell().add(new ListItem("Invoice No.")).setBold().setBorder(Border.NO_BORDER));
        nestedtable.addCell(new Cell().add(new ListItem("RK356748")).setBorder(Border.NO_BORDER));
        nestedtable.addCell(new Cell().add(new ListItem("Invoice Date")).setBold().setBorder(Border.NO_BORDER));
        nestedtable.addCell(new Cell().add(new ListItem("Invoice 7/4/2014")).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(ColorConstants.BLACK, 2);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);

        document.add(table);
        document.add(divider);
        document.close();
        String pdf = "pdf generated";
        System.out.println(pdf);
    }
}
