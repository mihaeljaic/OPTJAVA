package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.generic.ga.GeneticGAParallelization1;
import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.crossover.UniformCrossover;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.mutation.UniformMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.generic.ga.selection.TournamentSelect;
import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;

import java.io.IOException;

public class Pokretac1 {
    private static final int contendersCount = 75;
    private static final double probability = 0.2;
    private static final int sigma = 2;

    public static void main(String[] args) throws IOException {
        if (args.length != 7) {
            System.out.println("Expected 7 arguments");
            return;
        }

        int rectangleCount = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        int maxGenerations = Integer.parseInt(args[3]);
        double minError = Double.parseDouble(args[4]);

        int[] minValues = new int[5];
        int[] maxValues = new int[5];
        Util.loadTemplateImage(args[0], minValues, maxValues);

        ISelection<IntegerArraySolution> selection = new TournamentSelect<>(contendersCount, true);
        ICrossover<IntegerArraySolution> crossover = new UniformCrossover();
        IMutation<IntegerArraySolution> mutation = new UniformMutation(minValues, maxValues, sigma, probability);

        GeneticGAParallelization1 algorithm = new GeneticGAParallelization1(populationSize,maxGenerations, -minError,
                selection, crossover, mutation);


        IntegerArraySolution best = algorithm.run(Util.createStartingPopulation(minValues, maxValues, populationSize, rectangleCount));

        Util.saveParameters(args[5], rectangleCount, populationSize, maxGenerations, minError, contendersCount, probability, sigma);
        Util.exportImage(best, args[6]);
    }

}
