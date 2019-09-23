import algorithm.CircleBasedAlgorithm;
import algorithm.EllipseBasedAlgorithm;
import algorithm.IRecreationAlgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/gallery/federer/federer400.jpg";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        IRecreationAlgorithm algorithm = new EllipseBasedAlgorithm();
        algorithm.recreateImage(originalImage);
    }

}
