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
import java.util.UUID;

public class HtmlToImageConverterFlyingSaucer {

    public static void main(String[] args) throws IOException, FontFormatException {
        CertificateGenerateDTO certificateGenerateDTO = new CertificateGenerateDTO();
        certificateGenerateDTO.setLogoSellerUrl("https://cdn-migi-2.laosedu.la/f/laosedu/4fc25ce6eb8c97f4cb1deb10be041ce3/1f0a2d5c2b9e171c8da66644086577cefa0f60b414784c8de2e31006ec3a2ffb/taoanhdep_chu_ky_85765.png");
        certificateGenerateDTO.setStudentName("Nguyễn Ngọc Hoàngພາສາລາວມີຕົວອັຫຼາຍແບບ \uD83C\uDF89\uD83C\uDF89");
//        certificateGenerateDTO.setStudentName("<td style=\"width:154.65pt; border:1pt solid #000000; padding-right:4.9pt; padding-left:4.9pt; vertical-align:top\"><p style=\"margin-top:0pt; margin-bottom:0pt; font-size:11pt\"><math xmlns=\"http://www.w3.org/1998/Math/MathML\"><msup><mrow><mfenced separators=\"|\"><mrow><mi mathcolor=\"101840\">x</mi><mo mathcolor=\"101840\">+</mo><mi mathcolor=\"101840\">a</mi></mrow></mfenced></mrow><mrow><mi mathcolor=\"101840\">n</mi></mrow></msup><mo mathcolor=\"101840\">=</mo><munderover><mo>∑</mo><mrow><mi mathcolor=\"101840\">k</mi><mo mathcolor=\"101840\">=</mo><mn mathcolor=\"101840\">0</mn></mrow><mrow><mi mathcolor=\"101840\">n</mi></mrow></munderover><mrow><mfenced separators=\"|\"><mrow><mfrac linethickness=\"0pt\"><mrow><mi mathcolor=\"101840\">n</mi></mrow><mrow><mi mathcolor=\"101840\">k</mi></mrow></mfrac></mrow></mfenced><msup><mrow><mi mathcolor=\"101840\">x</mi></mrow><mrow><mi mathcolor=\"101840\">k</mi></mrow></msup><msup><mrow><mi mathcolor=\"101840\">a</mi></mrow><mrow><mi mathcolor=\"101840\">n</mi><mo mathcolor=\"101840\">-</mo><mi mathcolor=\"101840\">k</mi></mrow></msup></mrow></math></p></td>");
        certificateGenerateDTO.setCourseName("Lập trình Java Lập trình Java Lập trình JavaLập trình JavaLập trình Java Lập trình Java Lập trình Java Lập trình Java");
        certificateGenerateDTO.setCertificateTime(Instant.now());
        // Chạy hàm chuyển đổi HTML sang hình ảnh
        generateCourseCertificate(certificateGenerateDTO);
    }

    private static void generateCourseCertificate(CertificateGenerateDTO certificateGenerateDTO) throws IOException, FontFormatException {

        String outputDirectory = "src/main/resources/";
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/NotoSans-Regular.ttf"));
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
            context.setVariable("certificateTitle", "Chứng nhận");
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
                nameFontSize = "20px";
            }
            context.setVariable("buyerNameSize", nameFontSize);
            context.setVariable("finishTitle", "Đã hoàn thành khóa học");
            context.setVariable("courseName", certificateGenerateDTO.getCourseName());
            context.setVariable("dateTitle", "Ngày hoàn thành");
            Instant instant = certificateGenerateDTO.getCertificateTime();
            ZoneId zoneId = ZoneId.systemDefault();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(zoneId);
            String formattedDate = formatter.format(instant);

            context.setVariable("certificateTime", formattedDate);

            context.setVariable("sellerLogo",certificateGenerateDTO.getLogoSellerUrl());

            // 4. Xử lý template với Thymeleaf
            String processedHtml = templateEngine.process("certificate-template", context);

            // 5. Tạo file HTML tạm thời để render
            File tempHtmlFile = File.createTempFile("certificate-", ".html");
            Files.write(tempHtmlFile.toPath(), processedHtml.getBytes(StandardCharsets.UTF_8));

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

            int dpi = 100;
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, dpi);

            ImageIO.write(bim, "png", new File(imageOutputPath));
            document.close();
            System.out.println("Image created successfully: " + imageOutputPath);
            boolean deleted = tempHtmlFile.delete();
            if (!deleted) {
                System.err.println("Cảnh báo: Không thể xóa file tạm " + tempHtmlFile.getPath());
                tempHtmlFile.deleteOnExit();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}