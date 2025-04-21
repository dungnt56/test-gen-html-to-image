import com.example.testimage.dto.CertificateGenerateDTO;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.w3c.dom.Element;
import org.xhtmlrenderer.simple.Graphics2DRenderer;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
public class HtmlToImageConverterFlyingSaucer {

    public static void main(String[] args) throws IOException, FontFormatException {
        CertificateGenerateDTO certificateGenerateDTO = new CertificateGenerateDTO();
//        certificateGenerateDTO.setLogoSellerUrl("https://cdn-migi-2.laosedu.la/f/laosedu/4fc25ce6eb8c97f4cb1deb10be041ce3/1f0a2d5c2b9e171c8da66644086577cefa0f60b414784c8de2e31006ec3a2ffb/taoanhdep_chu_ky_85765.png");
        certificateGenerateDTO.setLogoSellerUrl("https://cdn-migi-2.laosedu.la/f/laosedu/4fc25ce6eb8c97f4cb1deb10be041ce3/23e3057babac25c05189bc787922825250250df99b3fc0f12ebf0170cf898794/image001.png");
//        certificateGenerateDTO.setLogoSellerUrl("https://cdn-migi-2.laosedu.la/f/laosedu/4fc25ce6eb8c97f4cb1deb10be041ce3/e66112c96b2b0e7926942d22ebd48f300924daf3fcb9d7564c5e5be94c81af96/logo UNICA modo 1 Nuabrdo Coy.jpg");
//        certificateGenerateDTO.setLogoSellerUrl("data:image/png;base64,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");
        certificateGenerateDTO.setStudentName("IUNC - Liên minh Bảo tồn Thiên nhiên Quốc tế");
//        certificateGenerateDTO.setStudentName("IUNC");
//        certificateGenerateDTO.setStudentName("າຍແບບ");
//        certificateGenerateDTO.setCourseName("Khóa Học Tiếng Anh Cơ Bản - \"Tiếng Anh Cho Người Mới Bắt Đầu\"");
        certificateGenerateDTO.setCourseName("IUNC - Liên");
        certificateGenerateDTO.setCertificateTime(Instant.now());
        // Chạy hàm chuyển đổi HTML sang hình ảnh
        generateCourseCertificate(certificateGenerateDTO);
    }

    private static void generateCourseCertificate(CertificateGenerateDTO certificateGenerateDTO) throws IOException, FontFormatException {

        String outputDirectory = "src/main/resources/";
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/PlayfairDisplay-Regular.ttf"));
//        Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/NotoSansLao-Regular.ttf"));
//
//        System.out.println("Font loaded: " + font.getFontName());
//        System.out.println("Font loaded: " + font1.getFontName());
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        ge.registerFont(font);
//        ge.registerFont(font1);

        try {
            // 1. Khởi tạo Thymeleaf TemplateEngine
            TemplateEngine templateEngine = new TemplateEngine();
            FileTemplateResolver templateResolver = new FileTemplateResolver();
            templateResolver.setPrefix("src/main/resources/templates/");
            templateResolver.setSuffix(".html");
            templateEngine.setTemplateResolver(templateResolver);

            // 2. Tạo context và thêm các biến
            Context context = new Context();
//            context.setVariable("certificateTitle", "ໃບຢັ້ງຢືນ");
            context.setVariable("certificateTitle", "Chứng nhận");
//            context.setVariable("certificateObject", "ທ້າວ / ນາງ");
            context.setVariable("certificateObject", "ông / bà");
            context.setVariable("buyerName", certificateGenerateDTO.getStudentName());

            String buyerName = certificateGenerateDTO.getStudentName();
            int nameLength = buyerName.length();
            String nameFontSize;
            if (nameLength <= 30) {
                nameFontSize = "54px";
            } else if (nameLength <= 60) {
                nameFontSize = "32px";
            } else if (nameLength <= 100) {
                nameFontSize = "25px";
            } else {
                nameFontSize = "18px";
            }
            context.setVariable("buyerNameSize", nameFontSize);
//            context.setVariable("finishTitle", "ໄດ້ສໍາເລັດຫຼັກສູດ");
            context.setVariable("finishTitle", "Đã hoàn thành khóa học");
            context.setVariable("courseName", certificateGenerateDTO.getCourseName());
//            context.setVariable("dateTitle", "ວັນທີີສຳເລັດ");
            context.setVariable("dateTitle", "Ngày hoàn thành");
            Instant instant = certificateGenerateDTO.getCertificateTime();
            ZoneId zoneId = ZoneId.systemDefault();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(zoneId);
            String formattedDate = formatter.format(instant);

            context.setVariable("certificateTime", formattedDate);

            context.setVariable("sellerLogo",certificateGenerateDTO.getLogoSellerUrl());

            // 4. Xử lý template với Thymeleaf
            String processedHtml = templateEngine.process("certificate-template", context);


            File dir = new File(outputDirectory);
            File[] oldImages = dir.listFiles((d, name) -> name.startsWith("certificate-"));
            if (oldImages != null) {
                for (File oldImage : oldImages) {
                    if (oldImage.delete()) {
//                        System.out.println("Deleted old image: " + oldImage.getName());
                    } else {
//                        System.err.println("Failed to delete: " + oldImage.getName());
                    }
                }
            }
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String randomId = UUID.randomUUID().toString().substring(0, 8);

            String imageOutputPath = outputDirectory + "certificate-" + timestamp + "-" + randomId + ".png";

            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

            ConverterProperties properties = new ConverterProperties();
            File baseDir = new File("src/main/resources/");
            properties.setBaseUri(baseDir.toURI().toString());

            PdfWriter writer = new PdfWriter(pdfOutputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);

            pdfDoc.setDefaultPageSize(new PageSize(842, 600));

            HtmlConverter.convertToPdf(processedHtml, pdfDoc, properties);
            pdfDoc.close();

            byte[] pdfBytes = pdfOutputStream.toByteArray();
            PDDocument document = PDDocument.load(pdfBytes);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            int dpi = 300;
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, dpi);

            ImageIO.write(bim, "png", new File(imageOutputPath));
            document.close();
            System.out.println("Image created successfully: " + imageOutputPath);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}