package hr.fer.zemris.optjava.dz7.neural_network.function;

public class LinearTransferFunction implements ITransferFunction {
    private int coefficient;
    private int translation;

    public LinearTransferFunction(int coefficient, int translation) {
        this.coefficient = coefficient;
        this.translation = translation;
    }

    public double calculate(double x) {
        return coefficient * x + translation;
    }
}
