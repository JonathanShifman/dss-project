package etities;

public class Circle extends Shape {

    private int size;

    public Circle() {
        this.x = 0;
        this.y = 0;
        this.size = 0;
        this.color = new RGBAColor(0, 0, 0, 0);
    }

    public static Circle generateRandom(double progressFactor, int imageWidth, int imageHeight) {
        Circle circle = new Circle();
        double sizeBound = progressFactor * (double)imageWidth;
        int size = rand.nextInt((int)sizeBound);
        int x = rand.nextInt(imageHeight + size) - size;
        int y = rand.nextInt(imageHeight + size) - size;
        circle.x = x;
        circle.y = y;
        circle.size = size;
        return circle;
    }

    @Override
    protected void mutateLocation(double progressFactor, int imageWidth, int imageHeight) {
        double sizeBound = progressFactor * (double)imageWidth;
        int size = rand.nextInt((int)sizeBound);
        int x = rand.nextInt(imageHeight + size) - size;
        int y = rand.nextInt(imageHeight + size) - size;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public void expand(int expansionFactor) {
        this.x *= expansionFactor;
        this.y *= expansionFactor;
        this.size *= expansionFactor;
    }

    @Override
    public IShape copy() {
        Circle copied = new Circle();
        copied.x = this.x;
        copied.y = this.y;
        copied.size = this.size;
        copied.color = this.color.copy();
        return copied;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
