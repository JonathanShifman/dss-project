package utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageUtils {

    /**
     * Calculates how many pixels should be contained in each block when reducing the image frame size
     */
    public static int calculateBlockSize(BufferedImage image, int maxFrameSize) {
        int blockSizeByWidth = (int)Math.round((double)image.getWidth() / (double)maxFrameSize);
        int blockSizeByHeight = (int)Math.round((double)image.getHeight() / (double)maxFrameSize);
        return Math.max(blockSizeByWidth, blockSizeByHeight);
    }

    /**
     * Creates a new blank image with the dimensions of the original one
     */
    public static BufferedImage createEmptyImage(BufferedImage originalImage) {
        return new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
    }

    /**
     * Deep copies an image
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isRasterPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isRasterPremultiplied, null);
    }
}
