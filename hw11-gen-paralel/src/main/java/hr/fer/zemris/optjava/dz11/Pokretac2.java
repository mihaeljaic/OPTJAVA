package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.generic.ga.GeneticGAParallelization2;
import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.crossover.UniformCrossover;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.mutation.UniformMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.generic.ga.selection.TournamentSelect;
import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;

import java.io.IOException;

public class Pokretac2 {
    private static final int contendersCount = 20;
    private static final double probability = 0.05;
    private static final int sigma = 10;
    private static final int tasksCount = 25;
    private static final int childrenToProduce = 4;

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

        GeneticGAParallelization2 algorithm = new GeneticGAParallelization2(populationSize,maxGenerations, -minError,
                selection, crossover, mutation);


        IntegerArraySolution best = algorithm.run(Util.createStartingPopulation(minValues, maxValues, populationSize,
                rectangleCount), tasksCount, childrenToProduce);

        Util.saveParameters(args[5], rectangleCount, populationSize, maxGenerations, minError, contendersCount, probability, sigma);
        Util.exportImage(best, args[6]);
    }
}
