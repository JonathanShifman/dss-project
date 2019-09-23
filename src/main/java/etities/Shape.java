package etities;

import java.util.Random;

public abstract class Shape implements IShape {

    protected static Random rand = new Random();

    /**
     * The color of the shape
     */
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

    /**
     * Performs a location-based mutation (by mutating the location and size of the shape)
     * @param progressFactor The progress factor that determines the size upper bound
     */
    protected abstract void mutateLocation(double progressFactor, int imageWidth, int imageHeight);

    /**
     * Performs a color-based mutation by assigning a random new color to the shape
     */
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
