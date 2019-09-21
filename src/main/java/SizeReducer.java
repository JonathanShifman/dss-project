import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SizeReducer {

    public static void main(String[] args) throws IOException {
        String originalPath = "src/main/resources/gallery/dona/dona200.jpg";
        String outputPath = "src/main/resources/gallery/dona/donaReduced.jpg";
        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        BufferedImage resizedImage = reduceSize(originalImage, 2);
        ImageIO.write(resizedImage, "jpg", new File(outputPath));
    }

    private static BufferedImage reduceSize(BufferedImage originalImage, int blockSize) throws IOException {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        width -= width % blockSize;
        height -= height % blockSize;
        int widthBlocks = width / blockSize;
        int heightBlocks = height / blockSize;
        int pixelsPerBlock = blockSize * blockSize;
        DataBuffer dataRaster = originalImage.getRaster().getDataBuffer();
        BufferedImage res = new BufferedImage(widthBlocks, heightBlocks, originalImage.getType());

        byte[] data = new byte[widthBlocks * heightBlocks * 3];
        int index = 0;
        for (int i = 0; i < widthBlocks; i++) {
            for (int j = 0; j < heightBlocks; j++) {
                int baseI = i * blockSize;
                int baseJ = j * blockSize;
                int rgb = originalImage.getRGB(baseI, baseJ);
//                for (int m = baseI; m < baseI + blockSize; m++) {
//                    for (int n = baseJ; n < baseJ + blockSize; n++) {
//                        rgb += originalImage.getRGB(m, n);
//                    }
//                }
//                rgb /= pixelsPerBlock;
                res.setRGB(i, j, rgb);
            }
        }
        return res;


    }

}
