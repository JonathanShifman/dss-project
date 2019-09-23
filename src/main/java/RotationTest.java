import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RotationTest {

    public static void main(String[] args) throws IOException {
        String outputDir = "src/main/resources/gallery/";
        BufferedImage image = new BufferedImage(200, 200, 5);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.rotate(Math.toRadians(45));
//        Rectangle rectangle = new Rectangle(80, 80, 40, 40);
        graphics.fillRect(80, 80, 40, 40);

        ImageIO.write(image, "jpg", new File(outputDir + "rotated.jpg"));
    }
}
