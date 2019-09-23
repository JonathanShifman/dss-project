package etities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a current solution, as defined by a list of shapes
 */
public class SolutionState {

    /**
     * The list of shapes defining the solution
     */
    private final List<IShape> shapes;

    public SolutionState(int numOfShapes) {
        shapes = new ArrayList<>(numOfShapes);
    }

    /**
     * Deep copy
     * @return A new instance with identical contents
     */
    public SolutionState copy() {
        SolutionState copied = new SolutionState(this.shapes.size());
        for (int i = 0; i < this.shapes.size(); i++) {
            copied.shapes.add(this.shapes.get(i).copy());
        }
        return copied;
    }

    /**
     * Mutate exactly one shape (at random) from the relevant segment
     * @param start The start of the relevant segment
     * @param end The end of the relevant segment
     * @param factor The progress factor that determines the size upper bound
     * @return
     */
    public SolutionState mutate(int start, int end, double factor, int imageWidth, int imageHeight) {
        SolutionState mutatedState = this.copy();
        Random rand = new Random();
        int i = start + rand.nextInt(end - start);
        IShape shape = mutatedState.shapes.get(i);
        shape.mutate(factor, imageWidth, imageHeight);
        return mutatedState;
    }

    /**
     * Expand all shapes in the solution by a certain factor
     */
    public void expand(int expansionFactor) {
        for (IShape shape: shapes) {
            shape.expand(expansionFactor);
        }
    }

    public List<IShape> getShapes() {
        return shapes;
    }
}
