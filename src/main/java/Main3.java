import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main3 {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/pic1.jpg";
        String outputDir = "src/main/resources/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));

        int pixels = originalImage.getWidth() * originalImage.getHeight();
        System.out.println(pixels + " pixels");

        Random rand = new Random();

        int numOfRuns = 8;
        int shapesPerRun = 25;
        int gensPerRun = 10000;
        int improvements = 0;
        int improvementsModulo = 20;
        int parentCost;
        BufferedImage parentImage = new BufferedImage(100, 100, 5);
        double radius = 100;

        byte[] dna = new byte[0];
        for (int run = 1; run <= numOfRuns; run++) {
            int numOfShapes = run * shapesPerRun;
            int runsRemaining = numOfRuns - run + 1;
            double runRadius = radius * (double)runsRemaining;
            byte[] runDna = new byte[numOfShapes * 10];
            for (int i = 0; i < dna.length; i++) {
                runDna[i] = dna[i];
            }
            for (int i = dna.length; i < runDna.length; i += 10) {
                for (int j = 0; j < 6; j += 2) {
                    byte x = (byte)(rand.nextInt(200) - 50);
                    byte y = (byte)(rand.nextInt(200) - 50);
                    runDna[i + j] = x;
                    runDna[i + j + 1] = y;
                }
            }

            renderImage(runDna, parentImage,100, 100, 5);
            parentCost = calculateCost(originalImage, parentImage);

            for (int gen = 1; gen <= gensPerRun; gen++) {
                byte[] childDna = mutate(runDna, numOfShapes, shapesPerRun, runRadius);
                BufferedImage childImage = new BufferedImage(100, 100, 5);
                renderImage(childDna, childImage, 100, 100, 5);
                int childCost = calculateCost(originalImage, childImage);
                if (childCost < parentCost) {
                    parentImage = childImage;
                    runDna = childDna;
                    parentCost = childCost;
                    System.out.println((run-1)*gensPerRun + gen);
                    improvements++;
                    if (improvements % improvementsModulo == 0) {
                        ImageIO.write(childImage, "jpg", new File(outputDir + "out" + ((run-1)*gensPerRun + gen) + ".jpg"));
                    }
                }
            }

            dna = runDna;
        }

        ImageIO.write(parentImage, "jpg", new File(outputDir + "outfinal.jpg"));

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

    private static BufferedImage renderImage(byte[] dna, BufferedImage image, int width, int height, int type) {

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        for (int i = 0; i < dna.length; i += 10) {
            Polygon polygon = new Polygon();
            for (int j = 0; j < 6; j += 2) {
                byte x = dna[i + j];
                byte y = dna[i + j + 1];
                polygon.addPoint(x, y);
            }
            float r = 0.01f * dna[i+6];
            float g = 0.01f * dna[i+7];
            float b = 0.01f * dna[i+8];
            float a = 0.01f * dna[i+9];
            graphics.setColor(new Color(r, g, b, a));
            graphics.fillPolygon(polygon);
        }
        return image;
    }

    private static byte[] mutate(byte[] dna, int numOfShapes, int shapesPerRun, double radius) {
        byte[] mutatedDna = dna.clone();
        Random rand = new Random();
        int mutationType = rand.nextInt(2);
        int i = rand.nextInt(shapesPerRun);
        i = numOfShapes - shapesPerRun + i;
        if (mutationType == 0) {
            byte x1 = (byte)(rand.nextInt(200) - 50);
            byte y1 = (byte)(rand.nextInt(200) - 50);
            double ang1 = rand.nextDouble() * Math.PI;
            double ang2 = rand.nextDouble() * Math.PI;
            double rad1 = rand.nextDouble() * radius;
            double rad2 = rand.nextDouble() * radius;
            int x2 = (int)Math.round(x1 + Math.cos(ang1) * rad1);
            int y2 = (int)Math.round(y1 + Math.sin(ang1) * rad1);
            int x3 = (int)Math.round(x1 + Math.cos(ang2) * rad2);
            int y3 = (int)Math.round(y1 + Math.sin(ang2) * rad2);
            mutatedDna[i*10] = x1;
            mutatedDna[i*10 + 1] = y1;
            mutatedDna[i*10 + 2] = (byte)x2;
            mutatedDna[i*10 + 3] = (byte)y2;
            mutatedDna[i*10 + 4] = (byte)x3;
            mutatedDna[i*10 + 5] = (byte)y3;
        } else {
            int bound = 80;
            byte r = (byte)rand.nextInt(bound);
            byte g = (byte)rand.nextInt(bound);
            byte b = (byte)rand.nextInt(bound);
            byte a = (byte)rand.nextInt(bound);
            mutatedDna[i*10 + 6] = r;
            mutatedDna[i*10 + 7] = g;
            mutatedDna[i*10 + 8] = b;
            mutatedDna[i*10 + 9] = a;
        }
        return mutatedDna;
    }

    private static int getEffectiveValue(int value) {
        if (value < 0) {
            return 256 - value;
        }
        return value;
    }

}
