package hr.fer.zemris.optjava.dz7;

public class Datasample {
    private double[] inputs;
    private double[] outputs;

    public Datasample(double[] inputs, double[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public double[] getInputs() {
        return inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }
}
