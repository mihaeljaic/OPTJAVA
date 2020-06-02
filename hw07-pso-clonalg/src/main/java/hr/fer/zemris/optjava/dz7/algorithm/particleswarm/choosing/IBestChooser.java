package hr.fer.zemris.optjava.dz7.algorithm.particleswarm.choosing;

public interface IBestChooser {
    void update();
    double[] getBest(int index);
}
