import java.util.Random;

public class Ellipse implements IShape {

    private int x;
    private int y;
    private int width;
    private int height;
    private int angle;
    private RGBAColor color;

    public Ellipse() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.angle = 0;
        this.color = new RGBAColor(0, 0, 0, 0);
    }

    public static Ellipse generateRandom(double progressFactor, int imageWidth, int imageHeight) {
        Random rand = new Random();
        Ellipse ellipse = new Ellipse();
        double wBound = progressFactor * (double)imageWidth;
        double hBound = progressFactor * (double)imageHeight;
        int width = rand.nextInt((int)wBound);
        int height = rand.nextInt((int)hBound);
        int x = rand.nextInt(imageHeight + width) - width;
        int y = rand.nextInt(imageHeight + height) - height;
        int angle = rand.nextInt(180);
        ellipse.x = x;
        ellipse.y = y;
        ellipse.width = width;
        ellipse.height = height;
        ellipse.angle = angle;
        return ellipse;
    }

    @Override
    public void mutate(double progressFactor, int imageWidth, int imageHeight) {
        Random rand = new Random();
        int mutationType = rand.nextInt(2);
        if (mutationType == 0) {
            // Location based mutation
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
        } else {
            int r, g, b, a;
//            int colorSelectionMethod = rand.nextInt(2);
            int colorSelectionMethod = 0;
            if (colorSelectionMethod == 0) {
                int bound = 100;
                r = rand.nextInt(bound);
                g = rand.nextInt(bound);
                b = rand.nextInt(bound);
                a = rand.nextInt(bound);
            } else {
                r = 0;
                g = 0;
                b = 0;
                a = 0;
//                int x = mutatedDna[i*10] + (mutatedDna[i*10+2] / 2);
//                int y = mutatedDna[i*10 + 1] + (mutatedDna[i*10+3] / 2);
//                int start = (x * width + y) * 3;
            }
            this.color = new RGBAColor(r, g, b, a);
        }
    }

    @Override
    public void expand(int expansionFactor) {
        this.x *= expansionFactor;
        this.y *= expansionFactor;
        this.width *= expansionFactor;
        this.height *= expansionFactor;
    }

    @Override
    public IShape clone() {
        Ellipse cloned = new Ellipse();
        cloned.x = this.x;
        cloned.y = this.y;
        cloned.width = this.width;
        cloned.height = this.height;
        cloned.angle = this.angle;
        cloned.color = this.color.clone();
        return cloned;
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

    public RGBAColor getRGBAColor() {
        return color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setColor(RGBAColor color) {
        this.color = color;
    }
}
