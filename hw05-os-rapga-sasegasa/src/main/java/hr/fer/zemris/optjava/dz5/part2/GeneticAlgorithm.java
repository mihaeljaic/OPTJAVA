package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.algorithm.SASEGASA;
import hr.fer.zemris.optjava.dz5.crossover.CycleCrossover;
import hr.fer.zemris.optjava.dz5.function.FactoryFitnessFunction;
import hr.fer.zemris.optjava.dz5.function.IFitnessFunction;
import hr.fer.zemris.optjava.dz5.mutation.PermutationMutation;
import hr.fer.zemris.optjava.dz5.selection.TournamentSelection;
import hr.fer.zemris.optjava.dz5.solution.IndexPermutation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Expected 3 arguments.");
            return;
        }

        IFitnessFunction<IndexPermutation> function = parseFunction(Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8));
        int populationSize = Integer.parseInt(args[1]);
        int numberOfSubpopulations = Integer.parseInt(args[2]);

        SASEGASA sasegasa = new SASEGASA(generateStartingPopulation(populationSize, function.getVariableCount()), numberOfSubpopulations,
                2000, 1000, 0, 5, 0.5, 0.005,
                function, new TournamentSelection<>(5), new CycleCrossover(), new PermutationMutation(1));

        IndexPermutation bestSolution = sasegasa.run();
        System.out.println("Best solution: " + bestSolution + "; fitness: " + bestSolution.fitness);
    }

    private static List<IndexPermutation> generateStartingPopulation(int populationSize, int n) {
        List<IndexPermutation> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            IndexPermutation solution = new IndexPermutation(n);
            solution.randomize();
            population.add(solution);
        }

        return population;
    }

    private static IFitnessFunction<IndexPermutation> parseFunction(List<String> lines) {
        int n = Integer.parseInt(lines.get(0).trim());
        double[][] distanceMatrix = new double[n][n];
        int position = 0;
        int i = 1;
        while(position < n) {
            String line = lines.get(i);
            i++;
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] data = line.trim().split("\\s+");
            for (int j = 0; j < n; j++) {
                distanceMatrix[position][j] = Double.parseDouble(data[j]);
            }
            position++;
        }

        int[][] quantityMatrix = new int[n][n];
        position = 0;
        while (position < n) {
            String line = lines.get(i);
            i++;
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] data = line.trim().split("\\s+");
            for (int j = 0; j < n; j++) {
                quantityMatrix[position][j] = Integer.parseInt(data[j]);
            }
            position++;
        }

        return new FactoryFitnessFunction(n, distanceMatrix, quantityMatrix);
    }



}
