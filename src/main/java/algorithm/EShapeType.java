package algorithm;

public enum EShapeType {
    ELLIPSE(new EllipseBasedAlgorithm()),
    CIRCLE(new CircleBasedAlgorithm()),
    TRIANGLE(new TriangleBasedAlgorithm());


    private IRecreationAlgorithm algorithm;

    EShapeType(IRecreationAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public IRecreationAlgorithm getAlgorithm() {
        return algorithm;
    }
}
