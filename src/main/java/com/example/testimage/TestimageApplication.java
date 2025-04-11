package com.example.testimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class TestimageApplication {

	public static void main(String[] args) {
		String templatePath = "src/main/resources/templates/certificate-template.html";
		String outputDirectory = "src/main/resources/"; // Thư mục lưu ảnh

		try {
			byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/templates/certificate_course.png"));
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);



			try {
				Font laoFont = Font.createFont(Font.TRUETYPE_FONT,
						new File("src/main/resources/fonts/Phetsarath_OT.ttf")).deriveFont(12f);
				GraphicsEnvironment.getLocalGraphicsEnvironment()
						.registerFont(laoFont);
			} catch (Exception e) {
				e.printStackTrace();
			}


			Dimension screenSize = new Dimension(842, 600);

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
			HtmlImageGenerator generator = new HtmlImageGenerator();
			generator.setSize(screenSize);
			generator.loadHtml(htmlContent);

			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String randomId = UUID.randomUUID().toString().substring(0, 8);
			String outputPath = outputDirectory + "certificate-" + timestamp + "-" + randomId + ".png";

			generator.saveAsImage(outputPath);

			System.out.println("image path created: " + outputPath);
		} catch (IOException e) {
			System.err.println("loi ne: " + e.getMessage());
		}
	}

}
