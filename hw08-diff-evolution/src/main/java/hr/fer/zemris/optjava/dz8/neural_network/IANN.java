package hr.fer.zemris.optjava.dz8.neural_network;

public interface IANN {

    double[] calculateOutputs(double[] inputs, double[] weights);

    int getWeightsCount();

}
