package etities;

public class Ellipse extends Shape {

    /**
     * Location X value
     */
    private int x;

    /**
     * Location Y value
     */
    private int y;

    /**
     * Bounding rectangle width
     */
    private int width;

    /**
     * Bounding rectangle height
     */
    private int height;

    /**
     * Rotation angle [deg]
     */
    private int angle;

    public Ellipse() {
        super();
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.angle = 0;
    }

    /**
     * @param progressFactor The progress factor that determines the size upper bound
     * @return A randomly generated ellipse
     */
    public static Ellipse generateRandom(double progressFactor, int imageWidth, int imageHeight) {
        Ellipse ellipse = new Ellipse();
        ellipse.mutateLocation(progressFactor, imageWidth, imageHeight);
        return ellipse;
    }

    @Override
    protected void mutateLocation(double progressFactor, int imageWidth, int imageHeight) {
        double wBound = progressFactor * (double)imageWidth;
        double hBound = progressFactor * (double)imageHeight;
        int width = rand.nextInt((int)wBound);
        int height = rand.nextInt((int)hBound);
        int x = rand.nextInt(imageHeight + width) - width;
        int y = rand.nextInt(imageHeight + height) - height;
        int angle = rand.nextInt(180);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    @Override
    public void expand(int expansionFactor) {
        this.x *= expansionFactor;
        this.y *= expansionFactor;
        this.width *= expansionFactor;
        this.height *= expansionFactor;
    }

    @Override
    public IShape copy() {
        Ellipse copied = new Ellipse();
        copied.x = this.x;
        copied.y = this.y;
        copied.width = this.width;
        copied.height = this.height;
        copied.angle = this.angle;
        copied.color = this.color.copy();
        return copied;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAngle() {
        return angle;
    }
}
