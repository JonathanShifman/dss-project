package algorithm;

import etities.IShape;
import etities.Triangle;
import rendering.TriangleRenderingManager;

public class TriangleBasedAlgorithm extends ShapeBasedAlgorithm {

    public TriangleBasedAlgorithm() {
        this.renderingManager = new TriangleRenderingManager();
    }

    @Override
    protected IShape generateRandomShape(double factor, int imageWidth, int imageHeight) {
        return Triangle.generateRandom(factor, imageWidth, imageHeight);
    }
}
