package algorithm;

import etities.Ellipse;
import etities.IShape;
import rendering.EllipseRenderingManager;

public class EllipseBasedAlgorithm extends ShapeBasedAlgorithm {

    public EllipseBasedAlgorithm() {
        this.renderingManager = new EllipseRenderingManager();
    }

    @Override
    protected IShape generateRandomShape(double factor, int imageWidth, int imageHeight) {
        return Ellipse.generateRandom(factor, imageWidth, imageHeight);
    }
}
