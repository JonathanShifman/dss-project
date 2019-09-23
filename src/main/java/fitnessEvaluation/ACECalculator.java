package fitnessEvaluation;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

public class ACECalculator implements IFitnessCalculator {

    @Override
    public double calculateFitness(BufferedImage originalImage, BufferedImage newImage) throws Exception {
        if (originalImage.getWidth() != newImage.getWidth() || originalImage.getHeight() != newImage.getHeight()) {
            throw new Exception("Not the same size");
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int pixels = width * height;

        DataBuffer originalData = originalImage.getRaster().getDataBuffer();
        DataBuffer newData = newImage.getRaster().getDataBuffer();

        int cost = 0;
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                int start = i * width * 3 + (j * 3);
                for (int k = 0; k < 3; k++) {
                    cost += Math.abs(originalData.getElem(start + k) - newData.getElem(start + k));
                }
            }
        }

        return (double)cost / ((double)pixels * 3);
    }



}
