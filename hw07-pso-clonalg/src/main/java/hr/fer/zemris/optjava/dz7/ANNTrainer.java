package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.algorithm.IOptimizationAlgorithm;
import hr.fer.zemris.optjava.dz7.algorithm.imune.Antibody;
import hr.fer.zemris.optjava.dz7.algorithm.imune.CloningAlgorithm;
import hr.fer.zemris.optjava.dz7.algorithm.imune.mutation.UniformHyperMutation;
import hr.fer.zemris.optjava.dz7.algorithm.imune.selection.ElitisticSelection;
import hr.fer.zemris.optjava.dz7.algorithm.particleswarm.*;
import hr.fer.zemris.optjava.dz7.algorithm.particleswarm.choosing.GlobalBest;
import hr.fer.zemris.optjava.dz7.algorithm.particleswarm.choosing.RingBest;
import hr.fer.zemris.optjava.dz7.neural_network.FFANN;
import hr.fer.zemris.optjava.dz7.neural_network.function.ITransferFunction;
import hr.fer.zemris.optjava.dz7.neural_network.function.SigmoidTransferFunction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class ANNTrainer {
    private static final double individualityFactor = 2;
    private static final double socialComponent = 2.5;
    private static final double alpha = 0.99;
    private static final int changeRate = 100;
    private static final double min = 0.5;
    private static final Function<Integer, Double> inertion = x -> Math.max(min, Math.pow(alpha, x / changeRate));

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Expected 5 arguments");
            return;
        }

        Datasample[] datasamples = parseData(Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8));
        int inputSize = datasamples[0].getInputs().length;
        int outputSize = datasamples[0].getOutputs().length;
        FFANN flowerNeuralNetwork = new FFANN(new int[] {inputSize, 5, 3, outputSize}, new ITransferFunction[] {
                SigmoidTransferFunction.getFunctionInstance(), SigmoidTransferFunction.getFunctionInstance(),
                SigmoidTransferFunction.getFunctionInstance()
        });

        ErrorFunction errorFunction = new ErrorFunction(datasamples, flowerNeuralNetwork);
        int n = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);
        int dimensionCount = errorFunction.getWeightsCount();

        double[] positionMin = new double[dimensionCount];
        double[] positionMax = new double[dimensionCount];
        double[] velocityMin = new double[dimensionCount];
        double[] velocityMax = new double[dimensionCount];
        for (int i = 0; i < dimensionCount; i++) {
            positionMin[i] = -1;
            positionMax[i] = 1;
            velocityMin[i] = -1;
            velocityMax[i] = 1;
        }

        String algorithm = args[1].toLowerCase();
        IOptimizationAlgorithm alg;
        if (algorithm.equals("pso-a")) {
            Particle[] population = new Particle[n];
            alg = new PSO(errorFunction, dimensionCount, new GlobalBest(population, dimensionCount), individualityFactor, socialComponent,
                    positionMin, positionMax, velocityMin, velocityMax, maxIter, merr, population, inertion);

        } else if (algorithm.startsWith("pso-b")) {
            int d = Integer.parseInt(algorithm.substring(5));
            Particle[] population = new Particle[n];
            alg = new PSO(errorFunction, dimensionCount, new RingBest(population, d, dimensionCount),
                    individualityFactor, socialComponent, positionMin, positionMax, velocityMin, velocityMax, maxIter,
                    merr, population, inertion);

        } else if (algorithm.equals("clonalg")) {
            Antibody[] population = new Antibody[n];
            for (int i = 0; i < n; i++) {
                double[] position = new double[dimensionCount];
                for (int j = 0; j < dimensionCount; j++) {
                    position[j] = 2 * Math.random() - 1;
                }
                population[i] = new Antibody(position);
            }

            double[] delta = new double[dimensionCount];
            for (int i = 0; i < delta.length; i++) {
                delta[i] = 1;
            }

            alg = new CloningAlgorithm(errorFunction, maxIter, merr, dimensionCount, n, 1, 5, n / 3, population,
                    new ElitisticSelection(), new UniformHyperMutation(0.09, delta, x ->  x, 50000, x -> -1 / x), delta);
        } else {
            System.out.println("Unsupported algorithm");
            return;
        }

        double[] solution = alg.run();
        System.out.println("----------------------");
        System.out.println("Weights:");
        for (int i = 0; i < solution.length; i++) {
            System.out.println(solution[i]);
        }
        System.out.println("----------------------");
        System.out.println("Error: " + errorFunction.calculateError(solution));
    }

    public static Datasample[] parseData(List<String> lines) {
        Datasample[] datasamples = new Datasample[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i).split(":");
            if (line.length != 2) {
                System.err.println("Invalid data format");
                System.exit(1);
            }

            double[] input = parseNumbers(line[0].substring(1, line[0].length() - 1).split(","));
            double[] output = parseNumbers(line[1].substring(1, line[1].length() - 1).split(","));

            datasamples[i] = new Datasample(input, output);
        }

        return datasamples;
    }

    private static double[] parseNumbers(String[] string) {
        double[] values = new double[string.length];
        for (int i = 0; i < string.length; i++) {
            values[i] = Double.parseDouble(string[i]);
        }

        return values;
    }
}
