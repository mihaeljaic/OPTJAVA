package hr.fer.zemris.optjava.dz8.neural_network;

import java.util.function.Function;

public class ElmansNN implements IANN {
    private Neuron[][] layers;
    private Neuron[] context;
    private int weightsCount;

    public ElmansNN(int[] layerSizes, Function<Double, Double>[] transferFunctions) {
        if (transferFunctions.length != layerSizes.length - 1) {
            throw new IllegalArgumentException();
        }

        layers = new Neuron[layerSizes.length][];
        for (int i = 0; i < layerSizes.length; i++) {
            layers[i] = new Neuron[layerSizes[i]];
            for (int j = 0; j < layerSizes[i]; j++) {
                layers[i][j] = i == 0 ? new Neuron(x -> x) : new Neuron(transferFunctions[i-1]);
            }

            if (i < layerSizes.length - 1) {
                weightsCount += layerSizes[i] * layerSizes[i + 1] + layerSizes[i + 1];
            }
        }

        context = new Neuron[layerSizes[1]];
        for (int i = 0; i < context.length; i++) {
            context[i] = new Neuron(x -> x);
        }
        weightsCount += context.length * context.length + context.length;
    }

    @Override
    public double[] calculateOutputs(double[] inputs, double[] weights) {
        if (inputs.length != layers[0].length || weights.length != weightsCount) {
            throw new IllegalArgumentException();
        }

        // Resets net values and sets input and context neurons' net values
        int weightIndex = initiateNeurons(inputs, weights);
        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                double net = 0;
                for (int k = 0; k < layers[i - 1].length; k++) {
                    net += weights[weightIndex++] * layers[i - 1][k].getNet();
                }
                net += weights[weightIndex++];

                if (i == 1) {
                    for (int k = 0; k < context.length; k++) {
                        net += weights[weightIndex++] * context[k].getNet();
                    }
                    context[j].setNet(layers[i][j].getTransferFunction().apply(net));
                }

                layers[i][j].setNet(layers[i][j].getTransferFunction().apply(net));
            }
        }

        double[] outputs = new double[layers[layers.length - 1].length];
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = layers[layers.length - 1][i].getNet();
        }

        return outputs;
    }

    private int initiateNeurons(double[] inputs, double[] weights) {
        for (int i = 0; i < layers[0].length; i++) {
            layers[0][i].setNet(inputs[i]);
        }

        int weightsIndex = 0;
        for (int i = 0; i < context.length; i++) {
            context[i].setNet(weights[weightsIndex++]);
        }

        return weightsIndex;
    }

    @Override
    public int getWeightsCount() {
        return weightsCount;
    }


}
