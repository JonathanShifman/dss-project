import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ErrorCalc {

    static String outputDir = "src/main/resources/gallery/dona/output/";
    static float minError = 0;
    static float maxError = 50;
    static float minA = 0f;
    static float maxA = 1f;

    public static void main(String[] args) throws Exception {
        String originalPath = "src/main/resources/gallery/dona/dona400.jpg";
        String generatedPath = "src/main/resources/gallery/dona/output/expanded.jpg";
        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        BufferedImage generatedImage = ImageIO.read(new File(generatedPath));
//        calculateCostWithPics(originalImage, generatedImage);
        System.out.println(calculateCost(originalImage, generatedImage, 1));
    }

    public static double calculateCost(BufferedImage originalImage, BufferedImage newImage, int sparsity) throws Exception {
        if (originalImage.getWidth() != newImage.getWidth() || originalImage.getHeight() != newImage.getHeight()) {
            throw new Exception("Not the same size");
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int pixels = width * height / (sparsity * sparsity);

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

        return (double)cost / ((double)pixels * 3);
    }

    public static void calculateCostWithPics(BufferedImage originalImage, BufferedImage newImage) throws Exception {
        if (originalImage.getWidth() != newImage.getWidth() || originalImage.getHeight() != newImage.getHeight()) {
            throw new Exception("Not the same size");
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        double[][] errors = new double[width][height];

        DataBuffer originalData = originalImage.getRaster().getDataBuffer();
        DataBuffer newData = newImage.getRaster().getDataBuffer();

        int cost = 0;
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                int start = i * width * 3 + (j * 3);
                double error = 0;
                for (int k = 0; k < 3; k++) {
                    error += Math.abs(originalData.getElem(start + k) - newData.getElem(start + k));
                }
                errors[i][j] = error / 3;
            }
        }

        Random rand = new Random();
        for (int size = 8; size <= 256; size *= 2) {
            BufferedImage image = new BufferedImage(width, height, 5);
            Graphics2D graphics = (Graphics2D)image.getGraphics();
            graphics.setColor(new Color(1f, 1f, 1f, 1f));
            graphics.fillRect(0, 0, width, height);
            for (int i = 0; i < width; i+= size) {
                for (int j = 0; j < height; j += size) {
                    float error = 0;
                    int count = 0;
                    for (int m = i; m < Math.min(width, i + size); m++) {
                        for (int n = j; n < Math.min(height, j + size); n++) {
                            count++;
                            error += errors[m][n];
                        }
                    }
                    error /= count;
                    if (error < minError) error = minError;
                    if (error > maxError) error = maxError;
                    float factor = (error - minError) / (maxError - minError);
                    float a = maxA * factor;
                    graphics.setColor(new Color(1, 0, 0, a));
                    graphics.fillRect(i, j, size, size);
                }
            }

            ImageIO.write(image, "jpg", new File(outputDir + "error" + size + ".jpg"));
        }
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
