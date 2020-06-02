package hr.fer.zemris.optjava.dz10;

import hr.fer.zemris.optjava.dz10.algorithm.ElitistNSGA;
import hr.fer.zemris.optjava.dz10.algorithm.crossover.BLX_alphaCrossover;
import hr.fer.zemris.optjava.dz10.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.dz10.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.dz10.algorithm.mutation.NormalMutation;
import hr.fer.zemris.optjava.dz10.algorithm.selection.CrowdingTournamentSelection;
import hr.fer.zemris.optjava.dz10.algorithm.selection.ISelection;
import hr.fer.zemris.optjava.dz10.algorithm.solution.Solution;
import hr.fer.zemris.optjava.dz10.problem.MOOPProblem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MOOP {
    private static final double alpha = 2.0;
    private static final double deviation = 0.5;
    private static final double probability = 0.15;
    private static final int contendersCount = 15;

    private static final String decisionSpacePath = "./izlaz-dec";
    private static final String objectiveSpacePath = "./izlaz-obj";

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Expected 3 arguments");
            return;
        }

        final int populationSize = Integer.parseInt(args[1]);
        final int maxIterations = Integer.parseInt(args[2]);

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
            System.out.println("Unsupported function.");
            return;
        }

        MOOPProblem problem = new MOOPProblem(functions);
        ISelection selection = new CrowdingTournamentSelection(contendersCount);
        ICrossover crossover = new BLX_alphaCrossover(alpha);
        IMutation mutation = new NormalMutation(deviation, probability);

        ElitistNSGA nsga = new ElitistNSGA(populationSize, maxIterations, problem, decisionSpaceDim, minValues,
                maxValues, selection, crossover, mutation);

        List<List<Solution>> paretoFronts = nsga.run(generateStartingPopulation(populationSize, decisionSpaceDim, minValues, maxValues, objectiveSpaceDim));

        File decisionSpaceout = new File(decisionSpacePath + args[0]);
        if (decisionSpaceout.exists()) {
            Files.delete(decisionSpaceout.toPath());
        }
        decisionSpaceout.createNewFile();
        Files.write(decisionSpaceout.toPath(), printDecisionSpace(paretoFronts), StandardOpenOption.WRITE);

        File objectiveSpaceout = new File(objectiveSpacePath + args[0]);
        if (objectiveSpaceout.exists()) {
            Files.delete(objectiveSpaceout.toPath());
        }
        objectiveSpaceout.createNewFile();
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
            System.out.println("Pareto front " + i + " : " + paretoFront.size() +
                    (paretoFront.size() == 1 ? " solution" : " solutions"));
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
            for (Solution solution : paretoFront) {
                output.add(solution.objectiveSpacetoString());
            }
        }

        return output;
    }


}
