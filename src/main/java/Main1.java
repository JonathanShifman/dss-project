import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main1 {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/pic1.jpg";
        String outputDir = "src/main/resources/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));

        int pixels = originalImage.getWidth() * originalImage.getHeight();
        System.out.println(pixels + " pixels");
        int numOfShapes = 50;

        BufferedImage parentImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        byte[] parentDna = new byte[numOfShapes * 10];
        Random rand = new Random();
        for (int i = 0; i < numOfShapes * 10; i += 10) {
            for (int j = 0; j < 6; j += 2) {
                byte x = (byte)rand.nextInt(100);
                byte y = (byte)rand.nextInt(100);
                parentDna[i + j] = x;
                parentDna[i + j + 1] = y;
            }
        }

        renderImage(parentImage, parentDna);
        int parentCost = calculateCost(originalImage, parentImage);
        ImageIO.write(parentImage, "jpg", new File(outputDir + "out0.jpg"));

        int improvements = 0;
        int improvementsModulo = 10;
        int numOfGens = 10000;
        for (int gen = 1; gen <= numOfGens; gen++) {
//            System.out.println("Processing gen " + gen);
            byte[] childDna = mutate(parentDna);
            BufferedImage childImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
            renderImage(childImage, childDna);
            int childCost = calculateCost(originalImage, childImage);
            if (childCost < parentCost) {
                parentImage = childImage;
                parentDna = childDna;
                parentCost = childCost;
                System.out.println(gen);
                improvements++;
                if (improvements % improvementsModulo == 0) {
                    ImageIO.write(childImage, "jpg", new File(outputDir + "out" + gen + ".jpg"));
                }
            }
        }

    }

//    private static int calculateCost(BufferedImage originalImage, byte[] dna) throws Exception {
//        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
//        renderImage(newImage, dna);
//        return calculateCost(originalImage, newImage);
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

    private static void renderImage(BufferedImage image, byte[] dna) {
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
    }

    private static byte[] mutate(byte[] dna) {
        byte[] mutatedDna = dna.clone();
        Random rand = new Random();
        int mutationType = rand.nextInt(2);
        int i = rand.nextInt(50);
        if (mutationType == 0) {
            int j = rand.nextInt(3);
            byte x = (byte)rand.nextInt(100);
            byte y = (byte)rand.nextInt(100);
            mutatedDna[i*10 + j*2] = x;
            mutatedDna[i*10 + j*2 + 1] = y;
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

//    private static byte getEffectiveValue(byte value) {
//        if (value < 0) {
//
//        }
//    }

}
