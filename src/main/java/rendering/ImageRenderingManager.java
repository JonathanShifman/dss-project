package rendering;

import etities.SolutionState;

import java.awt.image.BufferedImage;

public interface ImageRenderingManager {

    void renderImage(SolutionState solutionState, BufferedImage image, int start, int end);

}
