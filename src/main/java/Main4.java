import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Main4 {
    public static void main(String[] args) throws Exception {
        String imagePath = "src/main/resources/pic1.jpg";
        String outputDir = "src/main/resources/output/";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        int popSize = 200;
        int cutoff = 20;
        int numOfShapes = 50;
        int dnaLength = numOfShapes * 10;
        Ind[] pop = new Ind[popSize];
        Random rand = new Random();
        for (int i = 0; i < popSize; i++) {
            pop[i] = new Ind();
            pop[i].dna = new int[dnaLength];
            for (int j = 0; j < dnaLength; j+= 10) {
                for (int k = 0; k < 6; k += 2) {
                    int x = rand.nextInt(200) - 50;
                    int y = rand.nextInt(200) - 50;
                    pop[i].dna[j + k] = x;
                    pop[i].dna[j + k + 1] = y;
                }
                int bound = 80;
                int r = rand.nextInt(bound);
                int g = rand.nextInt(bound);
                int b = rand.nextInt(bound);
                int a = rand.nextInt(bound);
                pop[i].dna[j + 6] = r;
                pop[i].dna[j + 7] = g;
                pop[i].dna[j + 8] = b;
                pop[i].dna[j + 9] = a;
            }
        }

        Comparator<Ind> comparator = new Comparator<Ind>() {
            public int compare(Ind o1, Ind o2) {
                return Integer.compare(o1.cost, o2.cost);
            }
        };

        int numOfGens = 100;
        for (int gen = 1; gen <= numOfGens; gen++) {
            for (int i = 0; i < popSize ; i++) {
                BufferedImage newImage = new BufferedImage(100, 100, 5);
                renderImage(pop[i].dna, newImage);
                pop[i].calculateCost(originalImage, newImage);
            }
            Arrays.sort(pop, comparator);
            System.out.println("Gen " + gen + ": " + pop[0].cost);
            BufferedImage newImage = new BufferedImage(100, 100, 5);
            renderImage(pop[0].dna, newImage);
            ImageIO.write(newImage, "jpg", new File(outputDir + "out" + gen + ".jpg"));

            Ind[] newPop = new Ind[popSize];
            for (int i = 0; i < popSize; i++) {
                int index1 = rand.nextInt(cutoff);
                int index2 = rand.nextInt(cutoff);
                Ind ind1 = pop[index1];
                Ind ind2 = pop[index2];
                Ind child = combine(ind1, ind2);
                newPop[i] = child;
            }

            pop = newPop;
        }



    }

    private static Ind combine(Ind ind1, Ind ind2) {
        int dnaLength = ind1.dna.length;
        Random rand = new Random();
        int fifth = dnaLength / 5;
        int crossPoint = rand.nextInt(fifth);
        crossPoint = (dnaLength / 2) - (fifth / 2) + crossPoint;
        Ind child = new Ind();
        child.dna = new int[dnaLength];
        for (int i = 0; i < crossPoint; i++) {
            child.dna[i] = ind1.dna[i];
        }
        for (int i = crossPoint; i < dnaLength; i++) {
            child.dna[i] = ind2.dna[i];
        }
        return child;
    }

    private static BufferedImage renderImage(int[] dna, BufferedImage image) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
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

    static class Ind {
        int[] dna;
        int cost;

        void calculateCost(BufferedImage originalImage, BufferedImage newImage) throws Exception {
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

            this.cost = cost;
        }
    }
}
