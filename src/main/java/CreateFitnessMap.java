import fitnessEvaluation.FitnessMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class CreateFitnessMap {

    private static final String originalImagePath = "src/main/resources/output_heisenberg1/image1.jpg";
    private static final String generatedImagePath = "src/main/resources/output_heisenberg1/image2.jpg";

    /**
     * Create a fitness map for a generated image
     */
    public static void main(String[] args) throws Exception {
        BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
        BufferedImage generatedImage = ImageIO.read(new File(generatedImagePath));
        FitnessMapping.createFitnessMap(originalImage, generatedImage);
    }

}
