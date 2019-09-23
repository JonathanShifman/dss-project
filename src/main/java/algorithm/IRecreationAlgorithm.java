package algorithm;

import java.awt.image.BufferedImage;

/**
 * A general interface for an algorithm that should recreate a given image
 */
public interface IRecreationAlgorithm {

    /**
     * Perform the algorithm
     */
    void recreateImage(BufferedImage originalImage) throws Exception;

}
