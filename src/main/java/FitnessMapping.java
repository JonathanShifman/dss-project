import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.Random;

public class FitnessMapping {

    private static final int MIN_BLOCK_SIZE = 8;
    private static final int MAX_BLOCK_SIZE = 512;
    private static final String OUTPUT_PATH = "src/main/resources/gallery/output/";
    private static final int MAX_ERROR = 128;

    public static void createFitnessMap(BufferedImage originalImage, BufferedImage generatedImage) throws Exception {
        if (originalImage.getWidth() != generatedImage.getWidth() || originalImage.getHeight() != generatedImage.getHeight()) {
            throw new Exception("Not the same size");
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        double[][] errors = new double[width][height];

        DataBuffer originalData = originalImage.getRaster().getDataBuffer();
        DataBuffer generatedData = generatedImage.getRaster().getDataBuffer();

        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                int start = i * width * 3 + (j * 3);
                double error = 0;
                for (int k = 0; k < 3; k++) {
                    error += Math.abs(originalData.getElem(start + k) - generatedData.getElem(start + k));
                }
                errors[i][j] = error / 3;
            }
        }

        for (int size = MIN_BLOCK_SIZE; size <= MAX_BLOCK_SIZE; size *= 2) {
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
                    if (error < 0) error = 0;
                    if (error > MAX_ERROR) error = MAX_ERROR;
                    float a = error / (float)MAX_ERROR;
                    graphics.setColor(new Color(1, 0, 0, a));
                    graphics.fillRect(j, i, size, size);
                }
            }

            ImageIO.write(image, "jpg", new File(OUTPUT_PATH + "error" + size + ".jpg"));
        }

    }
}
