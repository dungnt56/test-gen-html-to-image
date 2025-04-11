package com.example.testimage;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;

@SpringBootApplication
public class TestimageApplicationJavaFx extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		// 1. Load template ảnh (PNG/JPG)
		Image template = new Image("src/main/resources/templates/certificate_course.png");
		ImageView imageView = new ImageView(template);

		// 2. Tạo Pane để thêm text lên ảnh
		Pane root = new Pane();
		root.getChildren().add(imageView);

		// 3. Thêm text (tiếng Việt, Anh, Lào)
		Text nameText = new Text("Nguyễn Văn A"); // Tên người nhận
		nameText.setFont(Font.font("Arial Unicode MS", 24)); // Font hỗ trợ Unicode
		nameText.setFill(Color.BLACK);
		nameText.setX(200); // Vị trí X
		nameText.setY(300); // Vị trí Y
		root.getChildren().add(nameText);

		// 4. Render ảnh và lưu ra file
		WritableImage outputImage = root.snapshot(new SnapshotParameters(), null);
		File outputFile = new File("certificate_output.png");
		ImageIO.write(SwingFXUtils.fromFXImage(outputImage, null), "png", outputFile);

		System.out.println("Đã tạo ảnh chứng chỉ thành công!");
		System.exit(0); // Tắt ứng dụng sau khi render xong
	}

	public static void main(String[] args) {
		launch(args);
	}

}
