public interface IShape {

    void mutate(double progressFactor, int imageWidth, int imageHeight);

    void expand(int expansionFactor);

    IShape clone();

}
