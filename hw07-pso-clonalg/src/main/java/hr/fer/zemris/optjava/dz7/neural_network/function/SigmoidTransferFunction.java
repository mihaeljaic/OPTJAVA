package hr.fer.zemris.optjava.dz7.neural_network.function;

public class SigmoidTransferFunction implements ITransferFunction {
    private static SigmoidTransferFunction functionInstance = new SigmoidTransferFunction();

    private SigmoidTransferFunction() {
    }

    public static SigmoidTransferFunction getFunctionInstance() {
        return functionInstance;
    }

    public double calculate(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
}
