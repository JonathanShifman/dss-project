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

public class Main7 {

    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/gallery/donald/donald200.jpg";
        String outputDir = "src/main/resources/gallery/donald/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        int blockSize = 4;
        if (originalImage.getWidth() > 200) {
            originalImage = SizeReducer.reduceSize(originalImage, blockSize);
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int pixels = originalImage.getWidth() * originalImage.getHeight();
        System.out.println(pixels + " pixels");

        Random rand = new Random();

        int numOfRuns = 25;
        int shapesPerRun = 8;
        int gensPerRun = 10000;
//        int numOfRuns = 20;
//        int shapesPerRun = 10;
//        int gensPerRun = 1000;
        int sparsity = 1;
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
                double wBound = factor * (double)width;
                double hBound = factor * (double)height;
                int ow = rand.nextInt((int)wBound);
                int oh = rand.nextInt((int)hBound);
                int x = rand.nextInt(width + ow) - ow;
                int y = rand.nextInt(height + oh) - oh;
                int ang = rand.nextInt(180);
                runDna[i] = x;
                runDna[i + 1] = y;
                runDna[i + 2] = ow;
                runDna[i + 3] = oh;
                runDna[i + 4] = ang;
            }

            BufferedImage baseImage = new BufferedImage(width, height, 5);
            parentImage = baseImage;
            renderImage(runDna, baseImage, 0, dna.length);
            parentCost = ErrorCalc.calculateIntCost(originalImage, baseImage, sparsity);

            for (int gen = 1; gen <= gensPerRun; gen++) {
                BufferedImage childImage = deepCopy(baseImage);
                int[] childDna = mutate(runDna, numOfShapes, shapesPerRun, width, height, factor, originalImage, childImage);
                renderImage(childDna, childImage, dna.length, runDna.length);
                int childCost = ErrorCalc.calculateIntCost(originalImage, childImage, sparsity);
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

//        System.out.println(clean(originalImage, dna));
        ImageIO.write(parentImage, "jpg", new File(outputDir + "outfinal.jpg"));
        expandDNA(dna, blockSize);
        BufferedImage expandedImage = new BufferedImage(width * blockSize, height * blockSize, 5);
        renderImage(dna, expandedImage, 0, dna.length);
        ImageIO.write(expandedImage, "jpg", new File(outputDir + "expanded.jpg"));
    }

    private static BufferedImage renderImage(int[] dna, BufferedImage image, int start, int end) {
        for (int i = start; i < end; i += 10) {
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            int x = dna[i];
            int y = dna[i + 1];
            int ow = dna[i + 2];
            int oh = dna[i + 3];
            int ang = dna[i + 4];
            float r = 0.01f * dna[i+6];
            float g = 0.01f * dna[i+7];
            float b = 0.01f * dna[i+8];
            float a = 0.01f * dna[i+9];
            graphics.setColor(new Color(r, g, b, a));
//            graphics.rotate(Math.toRadians(ang));
            graphics.fillOval(x, y, ow, oh);
        }
        return image;
    }

    private static int[] mutate(int[] dna, int numOfShapes, int shapesPerRun, int width, int height, double factor, BufferedImage originalImage, BufferedImage childImage) {
        int[] mutatedDna = dna.clone();
        Random rand = new Random();
        int mutationType = rand.nextInt(2);
        int i = rand.nextInt(shapesPerRun);
        i = numOfShapes - shapesPerRun + i;
//        int i = rand.nextInt(numOfShapes);
        if (mutationType == 0) {
            double wBound = factor * (double)width;
            double hBound = factor * (double)height;
            int ow = rand.nextInt((int)wBound);
            int oh = rand.nextInt((int)hBound);
            int x = rand.nextInt(width + ow) - ow;
            int y = rand.nextInt(height + oh) - oh;
            int ang = rand.nextInt(180);
            mutatedDna[i*10] = x;
            mutatedDna[i*10 + 1] = y;
            mutatedDna[i*10 + 2] = ow;
            mutatedDna[i*10 + 3] = oh;
            mutatedDna[i*10 + 4] = ang;
        } else {
            int r, g, b, a;
//            int colorSelectionMethod = rand.nextInt(2);
            int colorSelectionMethod = 0;
            if (colorSelectionMethod == 0) {
                int bound = 100;
                r = rand.nextInt(bound);
                g = rand.nextInt(bound);
                b = rand.nextInt(bound);
                a = rand.nextInt(bound);
            } else {
                r = 0;
                g = 0;
                b = 0;
                a = 0;
//                int x = mutatedDna[i*10] + (mutatedDna[i*10+2] / 2);
//                int y = mutatedDna[i*10 + 1] + (mutatedDna[i*10+3] / 2);
//                int start = (x * width + y) * 3;
            }
            mutatedDna[i*10 + 6] = r;
            mutatedDna[i*10 + 7] = g;
            mutatedDna[i*10 + 8] = b;
            mutatedDna[i*10 + 9] = a;
        }
        return mutatedDna;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    static int clean(BufferedImage originalImage, int[] dna) throws Exception {
        BufferedImage generatedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        renderImage(dna, generatedImage, 0, dna.length);
        int generatedCost = ErrorCalc.calculateIntCost(originalImage, generatedImage, 1);
        System.out.println(generatedCost);

        int[] newDna = dna.clone();
        int cleaned = 0;
        for (int i = 0; i < dna.length; i += 10) {
            for (int j = 0; j < 10; j++) {
                newDna[i + j] = 0;
            }

            BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
            renderImage(newDna, newImage, 0, dna.length);
            int newCost = ErrorCalc.calculateIntCost(originalImage, newImage, 1);
            System.out.println(newCost);
            if (newCost < generatedCost) {
                System.out.println("CLEANED");
                cleaned++;
            }

            for (int j = 0; j < 10; j++) {
                newDna[i + j] = dna[i + j];
            }
        }

        return cleaned;
    }

    static void expandDNA(int[] dna, int factor) {
        for (int i = 0; i < dna.length; i += 10) {
            dna[i] = dna[i] * factor;
            dna[i + 1] = dna[i + 1] * factor;
            dna[i + 2] = dna[i + 2] * factor;
            dna[i + 3] = dna[i + 3] * factor;
        }
    }

}
