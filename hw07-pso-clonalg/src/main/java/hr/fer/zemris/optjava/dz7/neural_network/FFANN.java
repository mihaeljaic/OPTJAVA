package hr.fer.zemris.optjava.dz7.neural_network;

import hr.fer.zemris.optjava.dz7.neural_network.function.ITransferFunction;

public class FFANN {
    private Neuron[][] layers;
    private int weightsCount;

    public FFANN(int[] layerSizes, ITransferFunction[] transferFunctions) {
        if (transferFunctions.length != layerSizes.length - 1) {
            throw new IllegalArgumentException();
        }

        layers = new Neuron[layerSizes.length][];
        for (int i = 0; i < layerSizes.length; i++) {
            layers[i] = new Neuron[layerSizes[i]];
            for (int j = 0; j < layerSizes[i]; j++) {
                layers[i][j] = i == 0 ? new Neuron(null) : new Neuron(transferFunctions[i-1]);
            }

            if (i < layerSizes.length - 1) {
                weightsCount += layerSizes[i] * layerSizes[i + 1] + layerSizes[i + 1];
            }
        }
    }

    public int getWeightsCount() {
        return weightsCount;
    }

    public double[] calculateOutputs(double[] inputs, double[] weights) {
        if (inputs.length != layers[0].length || weights.length != weightsCount) {
            throw new IllegalArgumentException();
        }

        // Resets net values and sets input neurons net values
        initiateNeurons(inputs);

        int weightIndex = 0;
        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                double net = 0;
                for (int k = 0; k < layers[i - 1].length; k++) {
                    net += weights[weightIndex++] * layers[i - 1][k].net;
                }
                net += weights[weightIndex++];
                layers[i][j].net = layers[i][j].transferFunction.calculate(net);
            }
        }

        double[] outputs = new double[layers[layers.length - 1].length];
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = layers[layers.length - 1][i].net;
        }

        return outputs;
    }

    private void initiateNeurons(double[] inputs) {
        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                layers[i][j].net = i == 0 ? inputs[j] : 0;
            }
        }
    }

    private static class Neuron {
        private double net;
        private ITransferFunction transferFunction;

        public Neuron(ITransferFunction transferFunction) {
            this.transferFunction = transferFunction;
        }
    }

}
