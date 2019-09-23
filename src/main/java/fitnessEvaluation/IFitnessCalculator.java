package fitnessEvaluation;

import java.awt.image.BufferedImage;

public interface IFitnessCalculator {

    double calculateFitness(BufferedImage originalImage, BufferedImage generatedImage) throws Exception;

}
