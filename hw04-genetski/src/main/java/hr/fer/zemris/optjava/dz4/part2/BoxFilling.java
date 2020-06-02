package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.util.ISelectOperator;
import hr.fer.zemris.optjava.dz4.util.TournamentSelect;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Sticks are indexed. Solution is array of stick indexes in order they are put into the box.
 * If stick doesn't fit by it's height it is put in another row.<br>
 * Parameters: ./kutije/problem-20-50-1.dat 100 6 6 false 400000 50
 *
 * @author Mihael Jaić
 */

public class BoxFilling {

    public static void main(String[] args) throws IOException {
        if (args.length != 7) {
            System.out.println("Arguments...");
            return;
        }

        int populationSize = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        boolean p = Boolean.parseBoolean(args[4]);
        int maxIterations = Integer.parseInt(args[5]);
        int minLength = Integer.parseInt(args[6]);

        int h = Integer.parseInt(args[0].split("-")[1]);
        BoxFunction function = new BoxFunction(h, parseSticks(Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8)));

        BoxSolution best = steadyState(function, populationSize, n, m, p, maxIterations, minLength);
        System.out.println("Best solution: " + best);
        System.out.println("Width: " + function.getWidth(best));
    }

    private static List<Stick> parseSticks(List<String> lines) {
        String first = lines.get(0);
        String[] lengths = first.substring(1, first.length() - 1).split(",\\s*");
        List<Stick> sticks = new ArrayList<>();
        int i = 0;
        for (String s : lengths) {
            sticks.add(new Stick(i++, Integer.parseInt(s)));
        }

        return sticks;
    }

    private static BoxSolution steadyState(BoxFunction function, int populationSize, int n, int m, boolean p, int maxIterations, int minLength) {
        BoxSolution[] population = generatePopulation(populationSize, function.getSticks().size());
        BoxSolution bestSolution = evaluate(function, population);
        if (function.getWidth(bestSolution) <= minLength) {
            return bestSolution;
        }

        ISelectOperator<BoxSolution> tournamentBest = new TournamentSelect<>(n, true);
        ISelectOperator<BoxSolution> tournamentWorst = new TournamentSelect<>(m, false);
        BoxMutation boxMutation = new BoxMutation(1);
        int it = 0;
        while (it++ < maxIterations) {
            BoxSolution parent1 = tournamentBest.select(population);
            BoxSolution parent2 = tournamentBest.select(population, parent1);
            BoxSolution[] offspring = CycleCrossover.crossover(parent1, parent2);
            boxMutation.mutation(offspring[0]);
            boxMutation.mutation(offspring[1]);

            BoxSolution temp = evaluate(function, offspring);
            if (temp.fitness > bestSolution.fitness) {
                bestSolution = temp;
                System.out.println("Found new best solution. Fitness: " + bestSolution.fitness);
                if (function.getWidth(bestSolution) <= minLength) {
                    return bestSolution;
                }
            }

            BoxSolution worst1 = tournamentWorst.select(population);
            if (!p || offspring[0].fitness >= worst1.fitness) {
                replace(worst1, offspring[0], population);
            }
        }

        return bestSolution;
    }

    private static void replace(BoxSolution dead, BoxSolution next, BoxSolution[] population) {
        for (int i = 0; i < population.length; i++) {
            if (population[i] == dead) {
                population[i] = next;
                return;
            }
        }
    }

    private static BoxSolution[] generatePopulation(int populationSize, int numberOfSticks) {
        BoxSolution[] population = new BoxSolution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = new BoxSolution(numberOfSticks);
            population[i].randomize();
        }

        return population;
    }

    private static BoxSolution evaluate(BoxFunction function, BoxSolution... population) {
        BoxSolution bestSolution = null;
        for (BoxSolution sol : population) {
            sol.fitness = function.evaluateSolution(sol);
            if (bestSolution == null || sol.fitness > bestSolution.fitness) {
                bestSolution = sol;
            }
        }

        return bestSolution;
    }

}
