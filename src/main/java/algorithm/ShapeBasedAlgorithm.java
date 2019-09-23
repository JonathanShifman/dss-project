package algorithm;

import etities.IShape;
import etities.SolutionState;
import fitnessEvaluation.ACECalculator;
import fitnessEvaluation.IFitnessCalculator;
import rendering.ImageRenderingManager;
import utils.ImageUtils;
import utils.SizeReducer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

/**
 * This class represents a general algorithm that recreated images using a bounded number of simple shapes
 */
public abstract class ShapeBasedAlgorithm implements IRecreationAlgorithm {

    /**
     * The rendering manager is responsible for rendering each shape according to its type.
     * To be initialized by the appropriate algorithm subclass.
     */
    protected ImageRenderingManager renderingManager;

    @Override
    public void recreateImage(BufferedImage originalImage) throws Exception {
        ImageIO.write(originalImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "image1.jpg"));

        // Reduce the image to a smaller size if necessary
        int blockSize = ImageUtils.calculateBlockSize(originalImage, AlgorithmConfig.MAX_FRAME_SIDE);
        BufferedImage reducedImage = SizeReducer.reduceSize(originalImage, blockSize);

        int imageWidth = reducedImage.getWidth();
        int imageHeight = reducedImage.getHeight();

        // Counts the amount of time a better solution was found. Used to decide when to output a progress frame.
        int progressStepsCounter = 0;

        // Stores the fitness of the current solution
        double fitness;

        BufferedImage blankImage = ImageUtils.createEmptyImage(originalImage);
        ImageIO.write(blankImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "out0.jpg"));

        IFitnessCalculator fitnessCalculator = new ACECalculator();

        int numOfEpochs = AlgorithmConfig.NUM_OF_EPOCHS;
        int shapesPerEpoch = AlgorithmConfig.SHAPES_PER_EPOCH;
        int iterationsPerRun = AlgorithmConfig.ITERATIONS_PER_EPOCH;
        int totalNumOfShapes = numOfEpochs * shapesPerEpoch;
        SolutionState solutionState = new SolutionState(totalNumOfShapes);
        for (int epoch = 0; epoch < numOfEpochs; epoch++) {
            System.out.println("Starting Epoch #" + (epoch + 1));

            // Calculate a factor depending on the stage we are at. Used to set a proper upper bound for shape sizes.
            int epochsRemaining = numOfEpochs - epoch;
            double factor = (double)epochsRemaining / (double)numOfEpochs;

            // Calculate the relevant segment for the current epoch
            int segmentStart = epoch * shapesPerEpoch;
            int segmentEnd = segmentStart + shapesPerEpoch;

            // The base image on which we will render the shapes of the relevant segment
            BufferedImage baseImage = ImageUtils.createEmptyImage(reducedImage);

            renderingManager.renderImage(solutionState, baseImage, 0, segmentStart);
            fitness = fitnessCalculator.calculateFitness(reducedImage, baseImage);

            // Initialize the shapes of the relevant segment randomly
            for (int i = 0; i < shapesPerEpoch; i++) {
                solutionState.getShapes().add(generateRandomShape(factor, imageWidth, imageHeight));
            }

            for (int iter = 1; iter <= iterationsPerRun; iter++) {
                BufferedImage newImage = ImageUtils.deepCopy(baseImage);
                SolutionState newState = solutionState.mutate(segmentStart, segmentEnd, factor, imageWidth, imageHeight);
                renderingManager.renderImage(newState, newImage, segmentStart, segmentEnd);
                double newFitness = fitnessCalculator.calculateFitness(reducedImage, newImage);
                if (newFitness < fitness) {
                    // A better solution has been found
                    int globalIterationIndex = epoch * iterationsPerRun + iter;
                    System.out.println("Iteration #" + globalIterationIndex + ": ACE = " + new DecimalFormat("#0.000000").format(newFitness));
                    solutionState = newState;
                    fitness = newFitness;
                    progressStepsCounter++;
                    if (AlgorithmConfig.SHOULD_OUTPUT_PROGRESS && (progressStepsCounter % AlgorithmConfig.PROGRESS_INTERVAL == 0)) {
                        // Output a progress frame
                        SolutionState solutionStateCopy = solutionState.copy();
                        solutionStateCopy.expand(blockSize);
                        BufferedImage outputImage = ImageUtils.createEmptyImage(originalImage);
                        renderingManager.renderImage(solutionStateCopy, outputImage, 0, segmentEnd);
                        ImageIO.write(outputImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "out" + globalIterationIndex + ".jpg"));
                    }
                }
            }
        }

        // Expand the solution to fit the original image size
        solutionState.expand(blockSize);

        // Output the final result
        BufferedImage finalImage = ImageUtils.createEmptyImage(originalImage);
        renderingManager.renderImage(solutionState, finalImage, 0, totalNumOfShapes);
        ImageIO.write(finalImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "image2.jpg"));
    }

    /**
     * @param factor The progress factor that determines the size upper bound
     * @return A randomly generated shape instance
     */
    protected abstract IShape generateRandomShape(double factor, int imageWidth, int imageHeight);
}
