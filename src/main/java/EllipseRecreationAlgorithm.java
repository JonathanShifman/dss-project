import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Random;

public class EllipseRecreationAlgorithm implements IRecreationAlgorithm {

    private Random rand = new Random();

    @Override
    public void recreate(BufferedImage originalImage) throws Exception {
        String outputDir = "src/main/resources/gallery/output/";
        int blockSize = calculateBlockSize(originalImage);
        originalImage = SizeReducer.reduceSize(originalImage, blockSize);
        ImageIO.write(originalImage, "jpg", new File(outputDir + "reduced.jpg"));
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

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

            BufferedImage baseImage = createEmptyImage(originalImage);
            renderingManager.renderImage(solutionState, baseImage, 0, segmentStart);
            fitness = ErrorCalc.calculateIntCost(originalImage, baseImage, 1);

            for (int i = 0; i < totalNumOfShapes; i++) {
                solutionState.getShapes().add(Ellipse.generateRandom(factor, imageWidth, imageHeight));
            }

            for (int iter = 1; iter <= iterationsPerRun; iter++) {
                BufferedImage newImage = deepCopy(baseImage);
                SolutionState newState = solutionState.mutate(segmentStart, segmentEnd, factor, imageWidth, imageHeight);
                renderingManager.renderImage(newState, newImage, segmentStart, segmentEnd);
                int newCost = ErrorCalc.calculateIntCost(originalImage, newImage, 1);
                if (newCost < fitness) {
                    int globalIterationIndex = epoch * iterationsPerRun + iter;
                    System.out.println(globalIterationIndex + ": " + (fitness - newCost) + " " + newCost);
                    solutionState = newState;
                    fitness = newCost;
                    progressStepsCounter++;
                    if (progressStepsCounter % AlgorithmConfig.PROGRESS_INTERVAL == 0) {
                        ImageIO.write(newImage, "jpg", new File(outputDir + "out" + globalIterationIndex + ".jpg"));
                    }
                }
            }
        }

        BufferedImage finalImage = createEmptyImage(originalImage);
        renderingManager.renderImage(solutionState, finalImage, 0, totalNumOfShapes);
        solutionState.expand(blockSize);
        ImageIO.write(finalImage, "jpg", new File(outputDir + "final.jpg"));
    }

    private static int calculateBlockSize(BufferedImage image) {
        int blockSizeByWidth = (int)Math.round((double)image.getWidth() / (double)AlgorithmConfig.MAX_FRAME_SIDE);
        int blockSizeByHeight = (int)Math.round((double)image.getHeight() / (double)AlgorithmConfig.MAX_FRAME_SIDE);
        return Math.max(blockSizeByWidth, blockSizeByHeight);
    }

    private static BufferedImage createEmptyImage(BufferedImage originalImage) {
        return new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
    }



    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
