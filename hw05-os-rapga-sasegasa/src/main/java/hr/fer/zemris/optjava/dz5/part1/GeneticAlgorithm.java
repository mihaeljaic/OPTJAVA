package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.algorithm.RAPGA;
import hr.fer.zemris.optjava.dz5.crossover.BitCrossover;
import hr.fer.zemris.optjava.dz5.crossover.ICrossover;
import hr.fer.zemris.optjava.dz5.function.IFitnessFunction;
import hr.fer.zemris.optjava.dz5.function.MaxOneFitnessFunction;
import hr.fer.zemris.optjava.dz5.mutation.BitMutationOperator;
import hr.fer.zemris.optjava.dz5.mutation.IMutationOperator;
import hr.fer.zemris.optjava.dz5.selection.ISelectOperator;
import hr.fer.zemris.optjava.dz5.selection.Task1Selection;
import hr.fer.zemris.optjava.dz5.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz5.solution.BitVectorSolution;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected argument");
            return;
        }

        int n = Integer.parseInt(args[0]);
        IFitnessFunction<BitVectorSolution> function = new MaxOneFitnessFunction(n);
        TournamentSelection<BitVectorSolution> t = new TournamentSelection<>(10);
        ISelectOperator<BitVectorSolution> selectOperator = t;
        ICrossover<BitVectorSolution> crossoverOperator = new BitCrossover();
        IMutationOperator<BitVectorSolution> mutationOperator = new BitMutationOperator(0.005);

        int minPopulationSize = 2;
        int maxPopulationSize = 400;
        double compFactorLowerBound = 0;
        double compFactorUpperBound = 5;
        double compFactorChange = 0.02;
        double maxSelectionPressure = 1000000;
        int maxEffort = 500000;
        int maxGenerations = 5000;

        RAPGA<BitVectorSolution> algorithm = new RAPGA<>(createStartingPopulation(100, n), minPopulationSize, maxPopulationSize, compFactorLowerBound,
                compFactorUpperBound, compFactorChange, maxSelectionPressure, function, selectOperator, crossoverOperator, mutationOperator, maxEffort, maxGenerations);

        BitVectorSolution bestSolution = algorithm.run();
        System.out.println("Best solution: " + bestSolution + "; fitness: " + bestSolution.fitness);
    }

    private static List<BitVectorSolution> createStartingPopulation(int size, int n) {
        List<BitVectorSolution> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            BitVectorSolution solution = new BitVectorSolution(n);
            solution.randomize();
            population.add(solution);
        }

        return population;
    }
}
