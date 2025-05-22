package renatius.import_export_accounting.Service.impl;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import org.springframework.stereotype.Service;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Entity.Request;
import renatius.import_export_accounting.Service.PdfGeneratorService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {
    @Override
    public byte[] generateRequestDocument(Request request) {
        PdfFont font;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/ArialRegular.ttf");
        if (fontStream == null) {
            throw new IllegalStateException("Шрифт не найден: fonts/ArialRegular.ttf");
        }

        byte[] fontBytes = null;
        try {
            fontBytes = fontStream.readAllBytes();
            font = PdfFontFactory.createFont(
                    fontBytes,
                    PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        doc.setFont(font);

        doc.add(new Paragraph("Заявка №" + request.getId()));
        doc.add(new Paragraph("Тип: " + request.getType().name()));
        doc.add(new Paragraph("Партнёр: " + request.getPartner().getNameOfCompany()));
        doc.add(new Paragraph("Дата: " + request.getCreatedAt()));

        doc.add(new Paragraph("Товары:"));
        for (Map.Entry<Product, Integer> entry : request.getProducts().entrySet()) {
            doc.add(new Paragraph(entry.getKey().getName() + " — " + entry.getValue() + " шт " + " Стоимость за единицу : " + entry.getKey().getBasePrice() + "$"));
        }

        doc.close();
        return baos.toByteArray();
    }
}
