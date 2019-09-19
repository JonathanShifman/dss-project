import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main5 {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/mona3.jpg";
        String outputDir = "src/main/resources/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int pixels = originalImage.getWidth() * originalImage.getHeight();
        System.out.println(pixels + " pixels");

        Random rand = new Random();

        int numOfRuns = 20;
        int shapesPerRun = 10;
        int gensPerRun = 4000;
        int improvements = 0;
        int improvementsModulo = 20;
        int parentCost;
        BufferedImage parentImage = new BufferedImage(width, height, 5);

        int[] dna = new int[0];
        for (int run = 1; run <= numOfRuns; run++) {
            int runsRemaining = numOfRuns - run + 1;
            double factor = (double)runsRemaining / (double)numOfRuns;
            int numOfShapes = run * shapesPerRun;
            int[] runDna = new int[numOfShapes * 10];
            for (int i = 0; i < dna.length; i++) {
                runDna[i] = dna[i];
            }
            for (int i = dna.length; i < runDna.length; i += 10) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                int ow = rand.nextInt(width);
                int oh = rand.nextInt(height);
//                int ow = 25;
//                int oh = 25;
                runDna[i] = x;
                runDna[i + 1] = y;
                runDna[i + 2] = ow;
                runDna[i + 3] = oh;
            }

            renderImage(runDna, parentImage,width, height, 5);
            parentCost = calculateCost(originalImage, parentImage);

            for (int gen = 1; gen <= gensPerRun; gen++) {
                int[] childDna = mutate(runDna, numOfShapes, shapesPerRun, width, height, factor);
                BufferedImage childImage = new BufferedImage(width, height, 5);
                renderImage(childDna, childImage, width, height, 5);
                int childCost = calculateCost(originalImage, childImage);
                if (childCost < parentCost) {
                    System.out.println((run-1)*gensPerRun + gen + ": " + (parentCost - childCost) + " " + childCost);
                    parentImage = childImage;
                    runDna = childDna;
                    parentCost = childCost;
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

    private static int[] mutate(int[] dna, int numOfShapes, int shapesPerRun, int width, int height, double factor) {
        int[] mutatedDna = dna.clone();
        Random rand = new Random();
        int mutationType = rand.nextInt(2);
        int i = rand.nextInt(shapesPerRun);
        i = numOfShapes - shapesPerRun + i;
        if (mutationType == 0) {
            double wBound = factor * (double)width;
            double hBound = factor * (double)height;
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            int ow = rand.nextInt((int)wBound);
            int oh = rand.nextInt((int)hBound);
            mutatedDna[i*10] = x;
            mutatedDna[i*10 + 1] = y;
            mutatedDna[i*10 + 2] = ow;
            mutatedDna[i*10 + 3] = oh;
        } else {
            int bound = 80;
            int r = rand.nextInt(bound);
            int g = rand.nextInt(bound);
            int b = rand.nextInt(bound);
            int a = rand.nextInt(bound);
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
