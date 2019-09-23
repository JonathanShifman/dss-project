package utils;

import java.awt.image.*;

public class SizeReducer {

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
