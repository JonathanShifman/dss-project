import java.awt.*;

public class RGBAColor {

    private int r;
    private int g;
    private int b;
    private int a;

    public RGBAColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public RGBAColor copy() {
        return new RGBAColor(r, g, b, a);
    }

    public Color toColor() {
        return new Color(
                0.01f * r,
                0.01f * g,
                0.01f * b,
                0.01f * a);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setA(int a) {
        this.a = a;
    }
}
