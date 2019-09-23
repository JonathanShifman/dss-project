package utils;

import java.awt.image.*;

public class SizeReducer {

    /**
     * Reduces the image size.
     * For example, if the original image is of size 400x400, and blockSize is 4, the output image will
     * be of size 100x100
     * @return
     */
    public static BufferedImage reduceSize(BufferedImage originalImage, int blockSize) {
        if (blockSize == 1) {
            return originalImage;
        }
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        width -= width % blockSize;
        height -= height % blockSize;
        int widthBlocks = width / blockSize;
        int heightBlocks = height / blockSize;
        BufferedImage res = new BufferedImage(widthBlocks, heightBlocks, originalImage.getType());

        for (int i = 0; i < widthBlocks; i++) {
            for (int j = 0; j < heightBlocks; j++) {
                int baseI = i * blockSize;
                int baseJ = j * blockSize;
                int rgb = originalImage.getRGB(baseI, baseJ);
                res.setRGB(i, j, rgb);
            }
        }
        return res;


    }

}
