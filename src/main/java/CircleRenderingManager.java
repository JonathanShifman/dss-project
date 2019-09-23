import java.awt.*;
import java.awt.image.BufferedImage;

public class CircleRenderingManager implements ImageRenderingManager {

    @Override
    public void renderImage(SolutionState solutionState, BufferedImage image, int start, int end) {
        for (int i = start; i < end; i ++) {
            Circle circle = (Circle)solutionState.getShapes().get(i);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(circle.getRGBAColor().toColor());
            graphics.fillOval(circle.getX(), circle.getY(), circle.getSize(), circle.getSize());
        }
    }

}
