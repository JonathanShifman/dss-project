import fitnessEvaluation.ACECalculator;
import fitnessEvaluation.IFitnessCalculator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class CompareImages {

    /**
     * Used to manually compare an original image with a generated one and output the ACE value
     */
    public static void main(String[] args) throws Exception {
        String originalPath = "src/main/resources/output_dona1/image1.jpg";
        String generatedPath = "src/main/resources/output_dona1/image2.jpg";
        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        BufferedImage generatedImage = ImageIO.read(new File(generatedPath));
        IFitnessCalculator fitnessCalculator = new ACECalculator();
        System.out.println(fitnessCalculator.calculateFitness(originalImage, generatedImage));
    }
}
