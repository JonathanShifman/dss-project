package algorithm;

import etities.Circle;
import etities.IShape;
import rendering.CircleRenderingManager;

public class CircleBasedAlgorithm extends ShapeBasedAlgorithm {

    public CircleBasedAlgorithm() {
        this.renderingManager = new CircleRenderingManager();
    }

    @Override
    protected IShape generateRandomShape(double factor, int imageWidth, int imageHeight) {
        return Circle.generateRandom(factor, imageWidth, imageHeight);
    }
}
