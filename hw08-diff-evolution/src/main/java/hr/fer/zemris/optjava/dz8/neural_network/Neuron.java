package hr.fer.zemris.optjava.dz8.neural_network;


import java.util.function.Function;

public class Neuron {
    private double net;
    private Function<Double, Double> transferFunction;

    public Neuron(Function<Double, Double> transferFunction) {
        this.transferFunction = transferFunction;
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }

    public Function<Double, Double> getTransferFunction() {
        return transferFunction;
    }
}
