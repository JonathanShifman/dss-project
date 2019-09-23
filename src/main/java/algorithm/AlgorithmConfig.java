package algorithm;

public class AlgorithmConfig {

    /**
     * The directory to write output files to
     */
    public static final String OUTPUT_DIR = "src/main/resources/output/";

    /**
     * The number of epochs for the algorithm to run
     */
    public static final int NUM_OF_EPOCHS = 50;

    /**
     * The number of shapes to be considered in each epoch
     */
    public static final int SHAPES_PER_EPOCH = 4;

    /**
     * The number of iterations to perform in each epoch
     */
    public static final int ITERATIONS_PER_EPOCH = 25_000;

    /**
     * The shape for the algorithm to use
     */
    public static final EShapeType SHAPE_TO_USE = EShapeType.ELLIPSE;

    /**
     * The maximum desired frame side size to work on.
     * For example, if set to 100, all images will be sized down to 100x100 before the process begins
     */
    public static final int MAX_FRAME_SIDE = 100;

    /**
     * Indicates whether or not progress output frames should be produced
     */
    public static final boolean SHOULD_OUTPUT_PROGRESS = true;

    /**
     * Indicates the frequency in which the output frames should be generated
     */
    public static final int PROGRESS_INTERVAL = 20;

}
