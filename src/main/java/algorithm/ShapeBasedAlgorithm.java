package algorithm;

import etities.IShape;
import etities.SolutionState;
import fitnessEvaluation.ErrorCalc;
import rendering.ImageRenderingManager;
import utils.ImageUtils;
import utils.SizeReducer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

public abstract class ShapeBasedAlgorithm implements IRecreationAlgorithm {

    protected ImageRenderingManager renderingManager;

    @Override
    public void recreateImage(BufferedImage originalImage) throws Exception {
        int blockSize = ImageUtils.calculateBlockSize(originalImage, AlgorithmConfig.MAX_FRAME_SIDE);
        BufferedImage reducedImage = SizeReducer.reduceSize(originalImage, blockSize);
        int imageWidth = reducedImage.getWidth();
        int imageHeight = reducedImage.getHeight();

        int progressStepsCounter = 0;
        double fitness;

        BufferedImage blankImage = ImageUtils.createEmptyImage(originalImage);
        ImageIO.write(blankImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "out0.jpg"));


        int numOfEpochs = AlgorithmConfig.NUM_OF_EPOCHS;
        int shapesPerEpoch = AlgorithmConfig.SHAPES_PER_EPOCH;
        int iterationsPerRun = AlgorithmConfig.ITERATIONS_PER_EPOCH;
        int totalNumOfShapes = numOfEpochs * shapesPerEpoch;
        SolutionState solutionState = new SolutionState(totalNumOfShapes);
        for (int epoch = 0; epoch < numOfEpochs; epoch++) {
            int epochsRemaining = numOfEpochs - epoch;
            double factor = (double)epochsRemaining / (double)numOfEpochs;
            int segmentStart = epoch * shapesPerEpoch;
            int segmentEnd = segmentStart + shapesPerEpoch;

            BufferedImage baseImage = ImageUtils.createEmptyImage(reducedImage);
            renderingManager.renderImage(solutionState, baseImage, 0, segmentStart);
            fitness = ErrorCalc.calculateCost(reducedImage, baseImage);

            for (int i = 0; i < shapesPerEpoch; i++) {
                solutionState.getShapes().add(generateRandomShape(factor, imageWidth, imageHeight));
            }

            for (int iter = 1; iter <= iterationsPerRun; iter++) {
                BufferedImage newImage = ImageUtils.deepCopy(baseImage);
                SolutionState newState = solutionState.mutate(segmentStart, segmentEnd, factor, imageWidth, imageHeight);
                renderingManager.renderImage(newState, newImage, segmentStart, segmentEnd);
                double newFitness = ErrorCalc.calculateCost(reducedImage, newImage);
                if (newFitness < fitness) {
                    int globalIterationIndex = epoch * iterationsPerRun + iter;
                    System.out.println(globalIterationIndex + ": " + new DecimalFormat("#0.000000").format(newFitness));
                    solutionState = newState;
                    fitness = newFitness;
                    progressStepsCounter++;
                    if (AlgorithmConfig.SHOULD_OUTPUT_PROGRESS && (progressStepsCounter % AlgorithmConfig.PROGRESS_INTERVAL == 0)) {
                        SolutionState solutionStateCopy = solutionState.copy();
                        solutionStateCopy.expand(blockSize);
                        BufferedImage outputImage = ImageUtils.createEmptyImage(originalImage);
                        renderingManager.renderImage(solutionStateCopy, outputImage, 0, segmentEnd);
                        ImageIO.write(outputImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "out" + globalIterationIndex + ".jpg"));
                    }
                }
            }
        }

        solutionState.expand(blockSize);
        BufferedImage finalImage = ImageUtils.createEmptyImage(originalImage);
        renderingManager.renderImage(solutionState, finalImage, 0, totalNumOfShapes);
        ImageIO.write(finalImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "final.jpg"));
    }

    protected abstract IShape generateRandomShape(double factor, int imageWidth, int imageHeight);
}
