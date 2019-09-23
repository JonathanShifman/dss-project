package fitnessEvaluation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.Random;

public class ErrorCalc {

    static String outputDir = "src/main/resources/gallery/dona/output/";
    static float minError = 0;
    static float maxError = 50;
    static float minA = 0f;
    static float maxA = 1f;

    public static void main(String[] args) throws Exception {
        String originalPath = "src/main/resources/gallery/eiffel/eiffel400.jpg";
        String generatedPath = "src/main/resources/gallery/output/final.jpg";
        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        BufferedImage generatedImage = ImageIO.read(new File(generatedPath));
        FitnessMapping.createFitnessMap(originalImage, generatedImage);
//        System.out.println(calculateCost(originalImage, generatedImage));
    }

    public static double calculateCost(BufferedImage originalImage, BufferedImage newImage) throws Exception {
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
//        int dataSize = originalData.getSize();
//        for (int i = 0; i < dataSize; i++) {
//            cost += Math.abs(originalData.getElem(i) - newData.getElem(i));
//        }

        return (double)cost / ((double)pixels * 3);
    }


    public static int calculateIntCost(BufferedImage originalImage, BufferedImage newImage, int sparsity) throws Exception {
        if (originalImage.getWidth() != newImage.getWidth() || originalImage.getHeight() != newImage.getHeight()) {
            throw new Exception("Not the same size");
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        DataBuffer originalData = originalImage.getRaster().getDataBuffer();
        DataBuffer newData = newImage.getRaster().getDataBuffer();

        int cost = 0;
        for (int i = 0; i < width; i += sparsity) {
            for (int j = 0; j < height; j += sparsity) {
                int start = i * width * 3 + (j * 3);
                for (int k = 0; k < 3; k++) {
                    cost += Math.abs(originalData.getElem(start + k) - newData.getElem(start + k));
                }
            }
        }
//        int dataSize = originalData.getSize();
//        for (int i = 0; i < dataSize; i++) {
//            cost += Math.abs(originalData.getElem(i) - newData.getElem(i));
//        }

        return cost * sparsity * sparsity;
    }

}
