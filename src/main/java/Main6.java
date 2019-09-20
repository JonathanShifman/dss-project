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

public class Main6 {

    static boolean newShape = false;

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/pic1.jpg";
        String outputDir = "src/main/resources/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Random rand = new Random();
        int numOfShapes = 50;
        int triesPerShape = 1000;
        double maxWidth = width;
        double minWidth = width / 10;

        BufferedImage currentImage = new BufferedImage(width, height, 5);
        int[] dna = new int[numOfShapes * 10];
        for (int shape = 0; shape < numOfShapes; shape++) {
            int runsRemaining = numOfShapes - shape;
            double factor = (double)runsRemaining / (double)numOfShapes;
            double shapeWidth = minWidth + (factor*(maxWidth - minWidth));

            int[] bestDna = null;
            BufferedImage bestImage = null;
            int bestCost = Integer.MAX_VALUE;
            for (int i = 0; i < triesPerShape; i++) {
                int[] newDna = mutate(dna, shape, width, height, shapeWidth, shapeWidth);
                BufferedImage newImage = deepCopy(currentImage);
                renderImage(newDna, newImage, width, height, 5);
                int childCost = calculateCost(originalImage, newImage);
                if (childCost < bestCost) {
                    bestCost = childCost;
                    bestDna = newDna;
                    bestImage = newImage;
                }
            }

            dna = bestDna;
            currentImage = bestImage;
            ImageIO.write(bestImage, "jpg", new File(outputDir + "out" + shape + ".jpg"));
        }

        ImageIO.write(currentImage, "jpg", new File(outputDir + "outfinal.jpg"));

    }
//    }

    private static int calculateCost(BufferedImage originalImage, BufferedImage newImage) throws Exception {
        if (originalImage.getWidth() != newImage.getWidth() || originalImage.getHeight() != newImage.getHeight()) {
            throw new Exception("Not the same size");
        }

        DataBuffer originalData = originalImage.getRaster().getDataBuffer();
        DataBuffer newData = newImage.getRaster().getDataBuffer();

        int cost = 0;
        int dataSize = originalData.getSize();
        for (int i = 0; i < dataSize; i++) {
            cost += Math.abs(originalData.getElem(i) - newData.getElem(i));
        }

        return cost;
    }

    private static BufferedImage renderImage(int[] dna, BufferedImage image, int width, int height, int type) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        graphics.setColor(new Color(0, 0, 0, 1));
//        Polygon p = new Polygon();
//        p.addPoint(0, 0);
//        p.addPoint(width, 0);
//        p.addPoint(width, height);
//        p.addPoint(0, height);
//        graphics.fillPolygon(p);
        for (int i = 0; i < dna.length; i += 10) {
            Polygon polygon = new Polygon();
            int x = dna[i];
            int y = dna[i + 1];
            int ow = dna[i + 2];
            int oh = dna[i + 3];
            float r = 0.01f * dna[i+6];
            float g = 0.01f * dna[i+7];
            float b = 0.01f * dna[i+8];
            float a = 0.01f * dna[i+9];
            graphics.setColor(new Color(r, g, b, a));
            graphics.fillOval(x, y, ow, oh);
        }
        return image;
    }

    private static int[] mutate(int[] dna, int i, int width, int height, double sw, double sh) {
        int[] mutatedDna = dna.clone();
        Random rand = new Random();
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        int ow = (int)sw;
        int oh = (int)sh;
        mutatedDna[i*10] = x;
        mutatedDna[i*10 + 1] = y;
        mutatedDna[i*10 + 2] = ow;
        mutatedDna[i*10 + 3] = oh;

        int bound = 100;
        int r = rand.nextInt(bound);
        int g = rand.nextInt(bound);
        int b = rand.nextInt(bound);
        int a = rand.nextInt(bound);
        mutatedDna[i*10 + 6] = r;
        mutatedDna[i*10 + 7] = g;
        mutatedDna[i*10 + 8] = b;
        mutatedDna[i*10 + 9] = a;
        return mutatedDna;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
