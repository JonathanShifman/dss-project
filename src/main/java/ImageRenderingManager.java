import java.awt.*;
import java.awt.image.BufferedImage;

public interface ImageRenderingManager<K extends IShape> {

    void renderImage(SolutionState solutionState, BufferedImage image, int start, int end);

}
