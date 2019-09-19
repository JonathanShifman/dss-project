import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main2 {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/dona.jpg";
        String outputDir = "src/main/resources/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int pixels = originalImage.getWidth() * originalImage.getHeight();
        System.out.println(pixels + " pixels");

        Random rand = new Random();

        int numOfRuns = 8;
        int shapesPerRun = 25;
        int gensPerRun = 10000;
        int improvements = 0;
        int improvementsModulo = 20;
        int parentCost;
        BufferedImage parentImage = new BufferedImage(width, height, 5);

        int[] dna = new int[0];
        for (int run = 1; run <= numOfRuns; run++) {
            int numOfShapes = run * shapesPerRun;
            int[] runDna = new int[numOfShapes * 10];
            for (int i = 0; i < dna.length; i++) {
                runDna[i] = dna[i];
            }
            for (int i = dna.length; i < runDna.length; i += 10) {
                for (int j = 0; j < 6; j += 2) {
                    int x = rand.nextInt(width + 100) - 50;
                    int y = rand.nextInt(height + 100) - 50;
                    runDna[i + j] = x;
                    runDna[i + j + 1] = y;
                }
            }

            renderImage(runDna, parentImage,width, height, 5);
            parentCost = calculateCost(originalImage, parentImage);

            for (int gen = 1; gen <= gensPerRun; gen++) {
                int[] childDna = mutate(runDna, numOfShapes, shapesPerRun, width, height);
                BufferedImage childImage = new BufferedImage(width, height, 5);
                renderImage(childDna, childImage, width, height, 5);
                int childCost = calculateCost(originalImage, childImage);
                if (childCost < parentCost) {
                    System.out.println((run-1)*gensPerRun + gen + ": " + (parentCost - childCost));
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
        graphics.setColor(new Color(0, 0, 0, 1));
        Polygon p = new Polygon();
        p.addPoint(0, 0);
        p.addPoint(width, 0);
        p.addPoint(width, height);
        p.addPoint(0, height);
        graphics.fillPolygon(p);
        for (int i = 0; i < dna.length; i += 10) {
            Polygon polygon = new Polygon();
            for (int j = 0; j < 6; j += 2) {
                int x = dna[i + j];
                int y = dna[i + j + 1];
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

    private static int[] mutate(int[] dna, int numOfShapes, int shapesPerRun, int width, int height) {
        int[] mutatedDna = dna.clone();
        Random rand = new Random();
        int mutationType = rand.nextInt(2);
        int i = rand.nextInt(shapesPerRun);
        i = numOfShapes - shapesPerRun + i;
        if (mutationType == 0) {
            int j = rand.nextInt(3);
            int x = rand.nextInt(width + 100) - 50;
            int y = rand.nextInt(height + 100) - 50;
            mutatedDna[i*10 + j*2] = x;
            mutatedDna[i*10 + j*2 + 1] = y;
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
