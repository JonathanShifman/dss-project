import algorithm.AlgorithmConfig;
import algorithm.IRecreationAlgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/gallery/dona.jpg";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        IRecreationAlgorithm algorithm = AlgorithmConfig.SHAPE_TO_USE.getAlgorithm();
        algorithm.recreateImage(originalImage);
    }

}
