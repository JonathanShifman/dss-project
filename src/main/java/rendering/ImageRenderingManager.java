package rendering;

import etities.SolutionState;

import java.awt.image.BufferedImage;

/**
 * Represents an object that is responsible for rendering a certain shape kind on an image
 */
public interface ImageRenderingManager {

    /**
     * Renders the relevant segment of the solution onto an image
     * @param solutionState The solution
     * @param image The image to render onto
     * @param start The start of the relevant segment
     * @param end The end of the relevant segment
     */
    void renderImage(SolutionState solutionState, BufferedImage image, int start, int end);

}
