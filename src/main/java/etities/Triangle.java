package etities;

import java.awt.*;

public class Triangle extends Shape {

    private Point a;
    private Point b;
    private Point c;

    public Triangle() {
        super();
        this.a = new Point(0, 0);
        this.b = new Point(0, 0);
        this.c = new Point(0, 0);
    }

    public static Triangle generateRandom(double progressFactor, int imageWidth, int imageHeight) {
        Triangle triangle = new Triangle();
        int sizeBound = (int)(progressFactor * (double)imageWidth);
        int xA = rand.nextInt(imageHeight + sizeBound) - sizeBound;
        int yA = rand.nextInt(imageHeight + sizeBound) - sizeBound;
        triangle.a = new Point(xA, yA);

        int distanceB = rand.nextInt(sizeBound);
        int distanceC = rand.nextInt(sizeBound);
        double angleB = rand.nextDouble() * 2 * Math.PI;
        double angleC = rand.nextDouble() * 2 * Math.PI;
        triangle.b = calculatePointAtDistance(triangle.a, distanceB, angleB);
        triangle.c = calculatePointAtDistance(triangle.a, distanceC, angleC);
        return triangle;
    }

    private static Point calculatePointAtDistance(Point p, int distance, double angle) {
        int x = p.x + (int)((double)distance * Math.cos(angle));
        int y = p.y + (int)((double)distance * Math.sin(angle));
        return new Point(x, y);
    }

    @Override
    protected void mutateLocation(double progressFactor, int imageWidth, int imageHeight) {
        int sizeBound = (int)(progressFactor * (double)imageWidth);
        int xA = rand.nextInt(imageHeight + sizeBound) - sizeBound;
        int yA = rand.nextInt(imageHeight + sizeBound) - sizeBound;
        this.a = new Point(xA, yA);

        int distanceB = rand.nextInt(sizeBound);
        int distanceC = rand.nextInt(sizeBound);
        double angleB = rand.nextDouble() * 2 * Math.PI;
        double angleC = rand.nextDouble() * 2 * Math.PI;
        this.b = calculatePointAtDistance(this.a, distanceB, angleB);
        this.c = calculatePointAtDistance(this.a, distanceC, angleC);
    }

    @Override
    public void expand(int expansionFactor) {
        this.a = expandPoint(a, expansionFactor);
        this.b = expandPoint(b, expansionFactor);
        this.c = expandPoint(c, expansionFactor);
    }

    private Point expandPoint(Point p, int expansionFactor) {
        return new Point(p.x * expansionFactor, p.y * expansionFactor);
    }

    @Override
    public IShape copy() {
        Triangle copied = new Triangle();
        copied.a = (Point)this.a.clone();
        copied.b = (Point)this.b.clone();
        copied.c = (Point)this.c.clone();
        copied.color = this.color.copy();
        return copied;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Point getC() {
        return c;
    }
}
