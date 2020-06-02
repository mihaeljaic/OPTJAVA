package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.neural_network.IANN;

public class ErrorFunction {
    private Dataset dataset;
    private IANN neuralNetwork;

    public ErrorFunction(Dataset dataset, IANN neuralNetwork) {
        this.dataset = dataset;
        this.neuralNetwork = neuralNetwork;
    }

    public int getWeightsCount() {
        return neuralNetwork.getWeightsCount();
    }

    public double calculateError(double[] weights) {
        double error = 0.0;
        for (Dataset.InputOutput dataEntry : dataset) {
            double[] output = neuralNetwork.calculateOutputs(dataEntry.getInput(), weights);
            double[] trueOutput = dataEntry.getOutput();
            for (int i = 0; i < output.length; i++) {
                error += (output[i] - trueOutput[i]) * (output[i] - trueOutput[i]);
            }
        }

        return error / dataset.getDatasamplesCount();
    }
}
