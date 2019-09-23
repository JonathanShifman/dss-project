package utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageUtils {

    public static int calculateBlockSize(BufferedImage image, int maxFrameSize) {
        int blockSizeByWidth = (int)Math.round((double)image.getWidth() / (double)maxFrameSize);
        int blockSizeByHeight = (int)Math.round((double)image.getHeight() / (double)maxFrameSize);
        return Math.max(blockSizeByWidth, blockSizeByHeight);
    }

    public static BufferedImage createEmptyImage(BufferedImage originalImage) {
        return new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
