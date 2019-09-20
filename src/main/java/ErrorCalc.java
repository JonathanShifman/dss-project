import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ErrorCalc {

    public static void main(String[] args) throws Exception {
        String originalPath = "src/main/resources/starry1.jpg";
        String generatedPath = "src/main/resources/starry2.png";
        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        BufferedImage generatedImage = ImageIO.read(new File(generatedPath));
        System.out.println(calculateCost(originalImage, generatedImage, 4));
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
