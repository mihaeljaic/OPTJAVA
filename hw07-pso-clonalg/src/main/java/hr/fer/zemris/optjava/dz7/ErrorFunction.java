package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.neural_network.FFANN;

public class ErrorFunction {
    private Datasample[] datasamples;
    private FFANN neuralNetwork;

    public ErrorFunction(Datasample[] datasamples, FFANN neuralNetwork) {
        this.datasamples = datasamples;
        this.neuralNetwork = neuralNetwork;
    }

    public int getWeightsCount() {
        return neuralNetwork.getWeightsCount();
    }

    public double calculateError(double[] weights) {
        double sum = 0.0;
        for (int i = 0; i < datasamples.length; i++) {
            double[] output = neuralNetwork.calculateOutputs(datasamples[i].getInputs(), weights);
            double[] trueOutput = datasamples[i].getOutputs();
            for (int j = 0; j < output.length; j++) {
                sum += (output[j] - trueOutput[j]) * (output[j] - trueOutput[j]);
            }
        }

        return sum / datasamples.length;
    }
}
