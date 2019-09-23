import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public abstract class ShapeBasedAlgorithm implements IRecreationAlgorithm {

    @Override
    public void recreateImage(BufferedImage originalImage) throws Exception {
        int blockSize = ImageUtils.calculateBlockSize(originalImage, AlgorithmConfig.MAX_FRAME_SIDE);
        BufferedImage reducedImage = SizeReducer.reduceSize(originalImage, blockSize);
        int imageWidth = reducedImage.getWidth();
        int imageHeight = reducedImage.getHeight();

        int progressStepsCounter = 0;
        int fitness;

        int numOfEpochs = AlgorithmConfig.NUM_OF_EPOCHS;
        int shapesPerEpoch = AlgorithmConfig.SHAPES_PER_EPOCH;
        int iterationsPerRun = AlgorithmConfig.ITERATIONS_PER_EPOCH;
        int totalNumOfShapes = numOfEpochs * shapesPerEpoch;
        SolutionState solutionState = new SolutionState(totalNumOfShapes);
        ImageRenderingManager<Ellipse> renderingManager = new EllipseRenderingManager();
        for (int epoch = 0; epoch < numOfEpochs; epoch++) {
            int epochsRemaining = numOfEpochs - epoch;
            double factor = (double)epochsRemaining / (double)numOfEpochs;
            int segmentStart = epoch * shapesPerEpoch;
            int segmentEnd = segmentStart + shapesPerEpoch;

            BufferedImage baseImage = ImageUtils.createEmptyImage(reducedImage);
            renderingManager.renderImage(solutionState, baseImage, 0, segmentStart);
            fitness = ErrorCalc.calculateIntCost(reducedImage, baseImage, 1);

            for (int i = 0; i < totalNumOfShapes; i++) {
                solutionState.getShapes().add(Ellipse.generateRandom(factor, imageWidth, imageHeight));
            }

            for (int iter = 1; iter <= iterationsPerRun; iter++) {
                BufferedImage newImage = ImageUtils.deepCopy(baseImage);
                SolutionState newState = solutionState.mutate(segmentStart, segmentEnd, factor, imageWidth, imageHeight);
                renderingManager.renderImage(newState, newImage, segmentStart, segmentEnd);
                int newCost = ErrorCalc.calculateIntCost(reducedImage, newImage, 1);
                if (newCost < fitness) {
                    int globalIterationIndex = epoch * iterationsPerRun + iter;
                    System.out.println(globalIterationIndex + ": " + (fitness - newCost) + " " + newCost);
                    solutionState = newState;
                    fitness = newCost;
                    progressStepsCounter++;
                    if (AlgorithmConfig.SHOULD_OUTPUT_PROGRESS && (progressStepsCounter % AlgorithmConfig.PROGRESS_INTERVAL == 0)) {
                        ImageIO.write(newImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "out" + globalIterationIndex + ".jpg"));
                    }
                }
            }
        }

        solutionState.expand(blockSize);
        BufferedImage finalImage = ImageUtils.createEmptyImage(originalImage);
        renderingManager.renderImage(solutionState, finalImage, 0, totalNumOfShapes);
        ImageIO.write(finalImage, "jpg", new File(AlgorithmConfig.OUTPUT_DIR + "final.jpg"));
    }
}
