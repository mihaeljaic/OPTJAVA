package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.util.ISelectOperator;
import hr.fer.zemris.optjava.dz4.util.TournamentSelect;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Elitistic genetic algorithm that solves function in zad-prijenosna.txt.
 * Run with arguments:<br>
 * <ul>
 *     <li>population size</li>
 *     <li>wanted fitness(is negative because it is punishment function)</li>
 *     <li>max number of generations</li>
 *     <li>selection operator. supported roulette wheel or tournament. Tournament has to be in format tournament:n</li>
 *     <li>sigma parameter for crossover</li>
 * </ul>
 * Some parameters: 100 -0.00000000000000000000000001 30000 tournament:10 0.1
 *
 * @author Mihael Jaić
 */

public class GeneticAlgorithm {
    private static double mutationDeviation = 0.4; // deviation when changing solution.
    private static double mutationProbability = 0.2; // probability of variable to change value.
    private static double fallout = 5; // reducement of mutation deviation after certain number of generations
    private static int deviationChange = 1000; // number of generations to change deviation.

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Arguments...");
            return;
        }

        int populationSize;
        try {
            populationSize = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            System.out.println("Population has to be integer.");
            return;
        }

        double wantedFitness;
        try {
            wantedFitness = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Fitness has to be number.");
            return;
        }

        int maxGenerations;
        try {
            maxGenerations = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            System.out.println("Max generatation has to be integer.");
            return;
        }

        double sigma;
        try {
            sigma = Double.parseDouble(args[4]);
        } catch (NumberFormatException ex) {
            System.out.println("Sigma has to be number");
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get("./zad-prijenosna.txt"), StandardCharsets.UTF_8);
        Function function = parseFunction(lines);

        ISelectOperator<DoubleArraySolution> selectOperator;
        if (args[3].toLowerCase().equals("roulettewheel")) {
            selectOperator = new RouletteWheelSelect();
        } else if (args[3].toLowerCase().matches("tournament:[1-9][0-9]*")) {
            int n;
            try {
                n = Integer.parseInt(args[3].split(":")[1]);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid format for tournament selection.");
                return;
            }
            selectOperator = new TournamentSelect<>(n, true);
        } else {
            System.out.println("Invalid selection operator");
            return;
        }

        GeneticElitistic algorithm = new GeneticElitistic(selectOperator, populationSize, function, wantedFitness, maxGenerations,
                new NormalMutationOperator(mutationDeviation, fallout, populationSize * deviationChange, mutationProbability),
                new BLX_alphaCrossoverOperator(sigma));

        DoubleArraySolution result = algorithm.run();
        System.out.println("Best solution: " + result + "; fitness: " + result.fitness);
    }

    private static Function parseFunction(List<String> lines) {
        List<String[]> temp = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }

            temp.add(line.substring(1, line.length() - 1).split(",\\s*"));
        }

        int rows = temp.size();
        int columns = temp.get(0).length;
        double[][] values = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            int j = 0;
            for (String s : temp.get(i)) {
                values[i][j++] = Double.parseDouble(s);
            }
        }

        return new Function(values);
    }

}
