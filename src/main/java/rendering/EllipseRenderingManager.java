package rendering;

import etities.Ellipse;
import etities.SolutionState;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EllipseRenderingManager implements ImageRenderingManager {

    private static final boolean ALLOW_ELLIPSE_ROTATION = false;

    public void renderImage(SolutionState solutionState, BufferedImage image, int start, int end) {
        for (int i = start; i < end; i ++) {
            Ellipse ellipse = (Ellipse)solutionState.getShapes().get(i);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(ellipse.getRGBAColor().toColor());

            graphics.translate(ellipse.getX() + (ellipse.getWidth() / 2), ellipse.getY() + (ellipse.getHeight() / 2));
            if (ALLOW_ELLIPSE_ROTATION) {
                graphics.rotate(Math.toRadians(ellipse.getAngle()));
            }
            graphics.fillOval(-(ellipse.getWidth() / 2), -(ellipse.getHeight() / 2), ellipse.getWidth(), ellipse.getHeight());
        }
    }
}
