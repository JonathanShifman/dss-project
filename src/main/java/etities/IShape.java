package etities;

/**
 * Represents a general shape
 */
public interface IShape {

    /**
     * Mutate the shape slightly (by changing either the location and size or the color)
     * @param progressFactor The progress factor that determines the size upper bound
     */
    void mutate(double progressFactor, int imageWidth, int imageHeight);

    /**
     * Expand the shape by a certain factor to fit a bigger frame
     * @param expansionFactor the factor to expand by
     */
    void expand(int expansionFactor);

    /**
     * Deep Copy
     * @return A new instance with identical content
     */
    IShape copy();

}
