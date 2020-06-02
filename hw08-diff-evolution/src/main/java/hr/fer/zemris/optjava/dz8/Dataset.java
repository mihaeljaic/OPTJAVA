package hr.fer.zemris.optjava.dz8;

import java.util.Iterator;

public class Dataset implements Iterable<Dataset.InputOutput> {
    private double[] values;
    private int inputSize;
    private int outputSize;

    public Dataset(double[] values, int inputSize, int outputSize) {
        this.values = values;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public int getDatasamplesCount() {
        return values.length - inputSize - outputSize + 1;
    }

    @Override
    public Iterator<InputOutput> iterator() {
        return new DatasetIterator();
    }

    private class DatasetIterator implements Iterator<InputOutput> {
        int position = 0;

        @Override
        public boolean hasNext() {
            return position + inputSize + outputSize <= values.length;
        }

        @Override
        public InputOutput next() {
            double[] input = new double[inputSize];
            for (int i = 0; i < inputSize; i++) {
                input[i] = values[position + i];
            }

            double[] output = new double[outputSize];
            for (int i = 0; i < outputSize; i++) {
                output[i] = values[position + inputSize + i];
            }

            position++;
            return new InputOutput(input, output);
        }
    }

    public static class InputOutput {
        private double[] input;
        private double[] output;

        public InputOutput(double[] input, double[] output) {
            this.input = input;
            this.output = output;
        }

        public double[] getInput() {
            return input;
        }

        public double[] getOutput() {
            return output;
        }
    }
}
