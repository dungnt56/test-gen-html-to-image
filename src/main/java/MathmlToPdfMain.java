import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.*;

public class MathmlToPdfMain {

    public static void main(String[] args) {
        try {
            // 1. Tạo Thymeleaf engine
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML");
            resolver.setCharacterEncoding("UTF-8");

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);

            // 2. Gán dữ liệu công thức
            Context context = new Context();
//            context.setVariable("formulaCheck", "<td style=\"width:154.65pt; border:1pt solid #000000; padding-right:4.9pt; padding-left:4.9pt; vertical-align:top\"><p style=\"margin-top:0pt; margin-bottom:0pt; font-size:11pt\"><math xmlns=\"http://www.w3.org/1998/Math/MathML\"><msup><mrow><mfenced separators=\"|\"><mrow><mi mathcolor=\"101840\">x</mi><mo mathcolor=\"101840\">+</mo><mi mathcolor=\"101840\">a</mi></mrow></mfenced></mrow><mrow><mi mathcolor=\"101840\">n</mi></mrow></msup><mo mathcolor=\"101840\">=</mo><munderover><mo>∑</mo><mrow><mi mathcolor=\"101840\">k</mi><mo mathcolor=\"101840\">=</mo><mn mathcolor=\"101840\">0</mn></mrow><mrow><mi mathcolor=\"101840\">n</mi></mrow></munderover><mrow><mfenced separators=\"|\"><mrow><mfrac linethickness=\"0pt\"><mrow><mi mathcolor=\"101840\">n</mi></mrow><mrow><mi mathcolor=\"101840\">k</mi></mrow></mfrac></mrow></mfenced><msup><mrow><mi mathcolor=\"101840\">x</mi></mrow><mrow><mi mathcolor=\"101840\">k</mi></mrow></msup><msup><mrow><mi mathcolor=\"101840\">a</mi></mrow><mrow><mi mathcolor=\"101840\">n</mi><mo mathcolor=\"101840\">-</mo><mi mathcolor=\"101840\">k</mi></mrow></msup></mrow></math></p></td>");
//            context.setVariable("formulaCheck", "<td style=\"width:154.65pt; border:1pt solid #000000; padding-right:4.9pt; padding-left:4.9pt; vertical-align:top\"><p style=\"margin-top:0pt; margin-bottom:0pt; font-size:11pt\"><math xmlns=\"http://www.w3.org/1998/Math/MathML\"><msup><mrow><mfenced separators=\"|\"><mrow><mi mathcolor=\"101840\">x</mi><mo mathcolor=\"101840\">+</mo><mi mathcolor=\"101840\">a</mi></mrow></mfenced></mrow><mrow><mi mathcolor=\"101840\">n</mi></mrow></msup><mo mathcolor=\"101840\">=</mo><munderover><mo>∑</mo><mrow><mi mathcolor=\"101840\">k</mi><mo mathcolor=\"101840\">=</mo><mn mathcolor=\"101840\">0</mn></mrow><mrow><mi mathcolor=\"101840\">n</mi></mrow></munderover><mrow><mfenced separators=\"|\"><mrow><mfrac linethickness=\"0pt\"><mrow><mi mathcolor=\"101840\">n</mi></mrow><mrow><mi mathcolor=\"101840\">k</mi></mrow></mfrac></mrow></mfenced><msup><mrow><mi mathcolor=\"101840\">x</mi></mrow><mrow><mi mathcolor=\"101840\">k</mi></mrow></msup><msup><mrow><mi mathcolor=\"101840\">a</mi></mrow><mrow><mi mathcolor=\"101840\">n</mi><mo mathcolor=\"101840\">-</mo><mi mathcolor=\"101840\">k</mi></mrow></msup></mrow></math></p></td>");

            // 3. Render HTML từ template
            String renderedHtml = templateEngine.process("math-template", context);

            // 4. Xuất PDF từ HTML
            try (OutputStream os = new FileOutputStream("src/main/resources/output-math.pdf")) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(renderedHtml, null);
                builder.toStream(os);
                builder.run();
                System.out.println("✅ Đã tạo file PDF tại src/main/resources/output-math.pdf");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
