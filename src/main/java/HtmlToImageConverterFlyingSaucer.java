import org.xhtmlrenderer.simple.Graphics2DRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class HtmlToImageConverterFlyingSaucer {

    public static void main(String[] args) throws IOException, FontFormatException {
        String templatePath = "src/main/resources/templates/certificate-template.html";
        String outputDirectory = "src/main/resources/"; // Thư mục lưu ảnh
        Font laoFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Phetsarath_OT.ttf"));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(laoFont);

        try {
            // Đọc file ảnh và chuyển thành Base64
            byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/templates/certificate_course.png"));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Đọc nội dung HTML và thay thế placeholder
            String htmlContent = new String(Files.readAllBytes(Paths.get(templatePath)))
                    .replace("${base64Image}", base64Image);

            // Gán giá trị cho các biến
            String certificateTitle = "ຮັບຮອງ";
            String certificateObject = "ທ້າວ / ນາງ";
            String buyerName = "Nguyễn Văn A";
            String finishTitle = "ໄດ້ສໍາເລັດຫຼັກສູດ";

            // Thay thế placeholder bằng giá trị thực tế
            htmlContent = htmlContent.replace("${certificateTitle}", certificateTitle)
                    .replace("${certificateObject}", certificateObject)
                    .replace("${buyerName}", buyerName)
                    .replace("${finishTitle}", finishTitle);
            // Tạo file HTML tạm thời để render
            File tempHtmlFile = File.createTempFile("certificate-", ".html");
            Files.write(tempHtmlFile.toPath(), htmlContent.getBytes());
            Files.write(tempHtmlFile.toPath(), htmlContent.getBytes(StandardCharsets.UTF_8));
            // Cấu hình kích thước hình ảnh (chiều rộng và chiều cao)
            int width = 842; // Chiều rộng hình ảnh
            int height = 600; // Chiều cao hình ảnh


            // Render HTML thành hình ảnh bằng Flying Saucer
            BufferedImage image = Graphics2DRenderer.renderToImageAutoSize(
                    tempHtmlFile.toURI().toURL().toExternalForm(),
                    width,
                    BufferedImage.TYPE_INT_ARGB
            );

            // Tạo tên file đầu ra với timestamp và UUID
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String randomId = UUID.randomUUID().toString().substring(0, 8);
            String outputPath = outputDirectory + "certificate-" + timestamp + "-" + randomId + ".png";

            // Lưu hình ảnh ra file PNG
            ImageIO.write(image, "png", new File(outputPath));

            System.out.println("Image path created: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
