package etities;

import java.util.Random;

public abstract class Shape implements IShape {

    protected static Random rand = new Random();
    protected RGBAColor color;

    public Shape() {
        this.color = new RGBAColor(0, 0, 0, 0);
    }

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

    public RGBAColor getRGBAColor() {
        return color;
    }

}
