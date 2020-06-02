package hr.fer.zemris.optjava.dz8;


import hr.fer.zemris.optjava.dz8.algorithm.DE.DifferentialEvolutionAlgorithm;
import hr.fer.zemris.optjava.dz8.algorithm.DE.base_selection.IBaseSelect;
import hr.fer.zemris.optjava.dz8.algorithm.DE.base_selection.RandomBaseSelect;
import hr.fer.zemris.optjava.dz8.algorithm.DE.crossover.ExponentialCrossover;
import hr.fer.zemris.optjava.dz8.algorithm.DE.crossover.ICrossover;
import hr.fer.zemris.optjava.dz8.algorithm.DE.crossover.UniformCrossover;
import hr.fer.zemris.optjava.dz8.algorithm.IOptalgorithm;
import hr.fer.zemris.optjava.dz8.algorithm.Solution;
import hr.fer.zemris.optjava.dz8.neural_network.ElmansNN;
import hr.fer.zemris.optjava.dz8.neural_network.IANN;
import hr.fer.zemris.optjava.dz8.neural_network.TDNN;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ANNTrainer {
    //region parameters
    private static final int trainingDataSize = 1000;
    private static final double crProbability = 0.95;
    private static final IBaseSelect baseSelect = new RandomBaseSelect();
    private static final int linearCombinations = 1;
    private static final double f = 0.95;
    private static final BiFunction<Solution, Solution, Solution> selectOperator =
            (target, probe) ->  probe.getFitness() <= target.getFitness() ? probe : target;
    //endregion

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Expected 5 arguments");
            return;
        }

        int populationSize = Integer.parseInt(args[2]);
        int maxIterations = Integer.parseInt(args[4]);
        double targetError = Double.parseDouble(args[3]);

        IOptalgorithm algorithm = parseAlgorithm(args[1], args[0], populationSize, maxIterations, targetError);
        Solution bestSolution = algorithm.run();

        System.out.println("Best solution: " + bestSolution);
        System.out.println("Error: " + bestSolution.getFitness());
    }

    private static IOptalgorithm parseAlgorithm(String input, String file, int populationSize, int maxIterations, double targetError) throws IOException {
        String[] temp = input.trim().toLowerCase().split("-");
        if (temp.length != 2 || !temp[1].matches("[0-9]+x[0-9]+(x[0-9]+)*")) {
            System.out.println("Invalid algorithm input: " + input);
            System.exit(1);
        }

        int[] layers = getLayers(temp[1]);
        if (layers[layers.length - 1] != 1) {
            System.out.println("Invalid neural netowrk architecture. Output must contain one neuron");
            System.exit(1);
        }

        Function<Double, Double> hyperbolicTangent = net -> (1 - Math.exp(-net)) / (1 + Math.exp(-net));
        Function<Double, Double>[] functions = new Function[layers.length-1];
        for (int i = 0; i < functions.length; i++) {
            functions[i] = hyperbolicTangent;
        }

        ErrorFunction errorFunction = null;
        if (temp[0].equals("tdnn")) {
            IANN neuralNetork = new TDNN(layers, functions);
            errorFunction = new ErrorFunction(loadData(file, layers[0], trainingDataSize), neuralNetork);

        } else if (temp[0].equals("elman")) {
            if (layers[0] != 1) {
                System.out.println("Invalid elman neural network architecture. Input must contain one neuron.");
                System.exit(1);
            }
            IANN neuralNetowrk = new ElmansNN(layers, functions);
            errorFunction = new ErrorFunction(loadData(file, trainingDataSize), neuralNetowrk);

        } else {
            System.out.println("Unsupported neural network: " + temp[0]);
            System.exit(1);
        }

        double[] vectorMin = setValues(errorFunction.getWeightsCount(), -1);
        double[] vectorMax = setValues(errorFunction.getWeightsCount(), 1);

        return new DifferentialEvolutionAlgorithm(populationSize, maxIterations, errorFunction.getWeightsCount(),
                targetError, vectorMin, vectorMax, errorFunction, new ExponentialCrossover(crProbability, errorFunction.getWeightsCount()),
                baseSelect, linearCombinations, f, selectOperator);
    }

    private static int[] getLayers(String input) {
        String[] temp = input.split("x");
        int[] layers = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            layers[i] = Integer.parseInt(temp[i]);
        }

        return layers;
    }

    private static Dataset loadData(String fileName, int length, int trainingDataSize) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        double[] values = new double[trainingDataSize < 0 || trainingDataSize > lines.size() ? lines.size() : trainingDataSize];
        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(lines.get(i));
        }

        return new Dataset(values, length, 1);
    }

    private static Dataset loadData(String filename, int trainingDataSize) throws IOException {
        return loadData(filename, 1, trainingDataSize);
    }

    private static double[] setValues(int dimensionCount, double value) {
        double[] result = new double[dimensionCount];
        for (int i = 0; i < dimensionCount; i++) {
            result[i] = value;
        }

        return result;
    }

}
