import java.util.Random;

public abstract class Shape implements IShape {

    protected static Random rand = new Random();
    protected int x;
    protected int y;
    protected RGBAColor color;

    @Override
    public void mutate(double progressFactor, int imageWidth, int imageHeight) {
        int mutationType = rand.nextInt(2);
        if (mutationType == 0) {
            mutateLocation(progressFactor, imageWidth, imageHeight);
        } else {
            mutateColor();
        }
    }

    protected abstract void mutateLocation(double progressFactor, int imageWidth, int imageHeight);

    protected void mutateColor() {
        int bound = 100;
        int r = rand.nextInt(bound);
        int g = rand.nextInt(bound);
        int b = rand.nextInt(bound);
        int a = rand.nextInt(bound);
        this.color = new RGBAColor(r, g, b, a);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public void setColor(RGBAColor color) {
        this.color = color;
    }
}
