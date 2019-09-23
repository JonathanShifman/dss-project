package algorithm;

/**
 * Represents the possible shapes to choose from
 */
public enum EShapeType {
    ELLIPSE(new EllipseBasedAlgorithm()),
    CIRCLE(new CircleBasedAlgorithm()),
    TRIANGLE(new TriangleBasedAlgorithm());

    /**
     * The corresponding algorithm instance
     */
    private IRecreationAlgorithm algorithm;

    EShapeType(IRecreationAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public IRecreationAlgorithm getAlgorithm() {
        return algorithm;
    }
}
