import java.awt.*;
import java.awt.image.BufferedImage;

public class EllipseRenderingManager implements ImageRenderingManager<Ellipse> {

    public void renderImage(SolutionState solutionState, BufferedImage image, int start, int end) {
        for (int i = start; i < end; i ++) {
            Ellipse ellipse = (Ellipse)solutionState.getShapes().get(i);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(ellipse.getRGBAColor().toColor());
//            graphics.rotate(Math.toRadians(ang));
            graphics.fillOval(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getHeight());
        }
    }
}
