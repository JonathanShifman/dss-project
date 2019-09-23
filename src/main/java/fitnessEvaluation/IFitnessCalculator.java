package fitnessEvaluation;

import java.awt.image.BufferedImage;

/**
 * Represents a class that calculates the quality of a recreated image with respect to the original one
 * according to certain criteria
 */
public interface IFitnessCalculator {

    /**
     * Calculates the fitness value
     */
    double calculateFitness(BufferedImage originalImage, BufferedImage generatedImage) throws Exception;

}
