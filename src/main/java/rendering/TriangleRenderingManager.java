package rendering;

import etities.SolutionState;
import etities.Triangle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Renders triangles onto an image
 */
public class TriangleRenderingManager implements ImageRenderingManager {

    @Override
    public void renderImage(SolutionState solutionState, BufferedImage image, int start, int end) {
        for (int i = start; i < end; i ++) {
            Triangle triangle = (Triangle)solutionState.getShapes().get(i);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(triangle.getRGBAColor().toColor());

            Polygon polygon = new Polygon();
            polygon.addPoint(triangle.getA().x, triangle.getA().y);
            polygon.addPoint(triangle.getB().x, triangle.getB().y);
            polygon.addPoint(triangle.getC().x, triangle.getC().y);
            graphics.fillPolygon(polygon);
        }

    }
}
