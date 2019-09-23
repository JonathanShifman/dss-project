package etities;

import java.awt.*;

/**
 * Represents a color with 3 primary color channels and an opacity value
 */
public class RGBAColor {

    /**
     * Red channel
     */
    private int r;

    /**
     * Green channel
     */
    private int g;

    /**
     * Blue channel
     */
    private int b;

    /**
     * Opacity value
     */
    private int a;

    public RGBAColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Deep Copy
     * @return A new instance with identical content
     */
    public RGBAColor copy() {
        return new RGBAColor(r, g, b, a);
    }

    /**
     * Converts the object to an instance of java.awt.Color
     */
    public Color toColor() {
        return new Color(
                0.01f * r,
                0.01f * g,
                0.01f * b,
                0.01f * a);
    }
}
