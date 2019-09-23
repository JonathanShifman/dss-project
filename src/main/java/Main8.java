import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main8 {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/gallery/earth/earth400.jpg";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        IRecreationAlgorithm algorithm = new EllipseRecreationAlgorithm();
        algorithm.recreateImage(originalImage);
    }

}
