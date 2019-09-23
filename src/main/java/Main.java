import algorithm.AlgorithmConfig;
import algorithm.IRecreationAlgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    /**
     * Run the main algorithm
     */
    public static void main(String[] args) throws Exception {
        File outputDir = new File(AlgorithmConfig.OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        String imagePath = "src/main/resources/gallery/eiffel.jpg"; // The path of the image to recreate
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        IRecreationAlgorithm algorithm = AlgorithmConfig.SHAPE_TO_USE.getAlgorithm();
        algorithm.recreateImage(originalImage);
    }

}
