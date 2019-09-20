import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Extra {

    static String outputDir = "src/main/resources/output/";
    static float minError = 0;
    static float maxError = 50;
    static float minA = 0f;
    static float maxA = 1f;
    static double offset = 16;

    public static void main(String[] args) throws Exception {
        String originalPath = "src/main/resources/output/dona200.jpg";
        String generatedPath = "src/main/resources/output/dona200pic2.jpg";
        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        BufferedImage generatedImage = ImageIO.read(new File(generatedPath));
        BufferedImage newImage = tryShape(originalImage, generatedImage, 72, 0);
//        drawShape(generatedImage, 48, 48);
//        showShape(generatedImage, 72, 0, 88, 40);
        ImageIO.write(newImage, "jpg", new File(outputDir + "extra.jpg"));
    }

    private static void showShape(BufferedImage image, int x, int y, int width, int height) throws IOException {
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        Random rand = new Random();
        float r = 1;
        float g = 1;
        float b = 1;
        float a = 0.5f;
        graphics.setColor(new Color(r, g, b, a));
        graphics.fillOval(x, y, width, height);
    }

    private static void drawShape(BufferedImage image, int x, int y) throws IOException {
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        float a = rand.nextFloat();
        x = (int) (x + rand.nextDouble()*(offset*2) - offset);
        y = (int) (y + rand.nextDouble()*(offset*2) - offset);
        int width = rand.nextInt(88);
        int height = rand.nextInt(40);
        graphics.setColor(new Color(r, g, b, a));
        graphics.fillOval(x, y, width, height);
    }

    private static BufferedImage tryShape(BufferedImage originalImage, BufferedImage generatedImage, int x, int y) throws Exception {
        double generatedCost = calculateCost(originalImage, generatedImage, 1);
        System.out.println(generatedCost);
        double minCost = Double.MAX_VALUE;
        BufferedImage minImage = null;
        for (int i = 0; i < 10000; i++) {
            BufferedImage newImage = deepCopy(generatedImage);
            drawShape(newImage, x, y);
            double newCost = calculateCost(originalImage, newImage, 1);
            if (newCost < minCost) {
                minCost = Math.min(minCost, newCost);
                minImage = newImage;
            }
        }
        System.out.println(minCost);
        return minImage;
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

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
