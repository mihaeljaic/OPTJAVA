package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.algorithm.NSGA;
import hr.fer.zemris.optjava.dz9.algorithm.SharingFunction;
import hr.fer.zemris.optjava.dz9.algorithm.crossover.BLX_alphaCrossover;
import hr.fer.zemris.optjava.dz9.algorithm.mutation.NormalMutation;
import hr.fer.zemris.optjava.dz9.algorithm.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.algorithm.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MOOP {
    public static final double sigma = 1.0;
    public static final double alpha = 2.0;
    public static final int nBest = 10;
    public static final double alphaCrossover = 0.1;
    public static final double deviation = 1.0;
    public static final double changeProbability = 0.15;
    public static final double initialFitness = 100.0;
    public static final double stepFitness = 10.0;

    public static final String decisionSpacePath = "./izlaz-dec";
    public static final String objectiveSpacePath = "./izlaz-obj";

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Expected 4 arguments.");
            return;
        }

        int populationSize = Integer.parseInt(args[1]);
        int maxIterations = Integer.parseInt(args[3]);

        String type = args[2].trim().toLowerCase();
        SharingFunction sharingFunction;
        if (type.equals("decision-space")) {
            sharingFunction = new SharingFunction(sigma, alpha, true);
        } else if (type.equals("objective-space")) {
            sharingFunction = new SharingFunction(sigma, alpha, false);
        } else {
            System.out.println("Invalid type: " + args[2]);
            return;
        }

        List<Function<double[], Double>> functions = new ArrayList<>();
        int decisionSpaceDim;
        int objectiveSpaceDim;
        double[] minValues;
        double[] maxValues;
        if (args[0].equals("1")) {
            functions.add(x -> x[0] * x[0]);
            functions.add(x -> x[1] * x[1]);
            functions.add(x -> x[2] * x[2]);
            functions.add(x -> x[3] * x[3]);
            decisionSpaceDim = 4;
            objectiveSpaceDim = 4;

            minValues = new double[decisionSpaceDim];
            maxValues = new double[decisionSpaceDim];
            for (int i = 0; i < decisionSpaceDim; i++) {
                minValues[i] = -5;
                maxValues[i] = 5;
            }
        } else if (args[0].equals("2")) {
            functions.add(x -> x[0]);
            functions.add(x -> (1 + x[1]) / x[0]);
            decisionSpaceDim = 2;
            objectiveSpaceDim = 2;

            minValues = new double[decisionSpaceDim];
            maxValues = new double[decisionSpaceDim];
            minValues[0] = 0.1;
            maxValues[0] = 1;
            minValues[1] = 0;
            maxValues[1] = 5;
        } else {
            System.out.println("Invalid function.");
            return;
        }

        NSGA nsga = new NSGA(new MOOPProblem(functions), maxIterations, decisionSpaceDim, sharingFunction, new TournamentSelection(nBest, true),
                new BLX_alphaCrossover(alphaCrossover), new NormalMutation(deviation, changeProbability), minValues, maxValues,
                initialFitness, stepFitness);

        List<List<Solution>> paretoFronts = nsga.run(generateStartingPopulation(populationSize, decisionSpaceDim, minValues, maxValues, objectiveSpaceDim));

        File decisionSpaceout = new File(decisionSpacePath + args[0]);
        if (!decisionSpaceout.exists()) {
            decisionSpaceout.createNewFile();
        }
        Files.write(decisionSpaceout.toPath(), printDecisionSpace(paretoFronts), StandardOpenOption.WRITE);

        File objectiveSpaceout = new File(objectiveSpacePath + args[0]);
        if (!objectiveSpaceout.exists()) {
            objectiveSpaceout.createNewFile();
        }
        Files.write(objectiveSpaceout.toPath(), printObjectiveSpace(paretoFronts), StandardOpenOption.WRITE);
    }

    private static List<Solution> generateStartingPopulation(int populationSize, int decisionSpaceDim, double[] minValues,
                                                             double[] maxValues, int objectiveSpaceDim) {
        List<Solution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            double[] decisionSpace = new double[decisionSpaceDim];
            for (int j = 0; j < decisionSpaceDim; j++) {
                decisionSpace[j] = minValues[j] + (maxValues[j] - minValues[j]) * Math.random();
            }
            population.add(new Solution(decisionSpace, objectiveSpaceDim));
        }

        return population;
    }

    private static List<String> printDecisionSpace(List<List<Solution>> paretoFronts) {
        List<String> output = new ArrayList<>();
        for (int i = 0; i < paretoFronts.size(); i++) {
            output.add("pareto front #" + (i + 1));
            List<Solution> paretoFront = paretoFronts.get(i);
            for (int j = 0; j < paretoFront.size(); j++) {
                output.add(paretoFront.get(j).decisionSpacetoString());
            }
        }

        return output;
    }

    private static List<String> printObjectiveSpace(List<List<Solution>> paretoFronts) {
        List<String> output = new ArrayList<>();
        for (int i = 0; i < paretoFronts.size(); i++) {
            output.add("pareto front #" + (i + 1));
            List<Solution> paretoFront = paretoFronts.get(i);
            for (int j = 0; j < paretoFront.size(); j++) {
                output.add(paretoFront.get(j).objectiveSpacetoString());
            }
        }

        return output;
    }

}
