import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolutionState {

    private final List<IShape> shapes;

    public SolutionState(int numOfShapes) {
        shapes = new ArrayList<>(numOfShapes);
    }

    public SolutionState clone() {
        SolutionState cloned = new SolutionState(this.shapes.size());
        for (int i = 0; i < this.shapes.size(); i++) {
            cloned.shapes.add(this.shapes.get(i).clone());
        }
        return cloned;
    }

    public SolutionState mutate(int start, int end, double factor, int imageWidth, int imageHeight) {
        SolutionState mutatedState = this.clone();
        Random rand = new Random();
        int i = start + rand.nextInt(end - start);
        IShape shape = mutatedState.shapes.get(i);
        shape.mutate(factor, imageWidth, imageHeight);
        return mutatedState;
    }

    public void expand(int expansionFactor) {
        for (IShape shape: shapes) {
            shape.expand(expansionFactor);
        }
    }

    public List<IShape> getShapes() {
        return shapes;
    }
}
