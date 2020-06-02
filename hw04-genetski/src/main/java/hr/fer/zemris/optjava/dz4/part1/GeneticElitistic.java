package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.util.*;

import java.util.Arrays;
import java.util.Random;

public class GeneticElitistic implements IOptAlgorithm<DoubleArraySolution> {
    private ISelectOperator<DoubleArraySolution> selectOperator;
    private int populationSize;
    private IFitnessFunction<DoubleArraySolution, Double> function;
    private double wantedFitness;
    private int maxGenerations;
    private NormalMutationOperator mutationOperator;
    private ICrossoverOperator<DoubleArraySolution> crossoverOperator;
    private DoubleArraySolution bestSolution;

    private final int MIN_VALUE = -10;
    private final int MAX_VALUE = 10;

    // Used for dealing when stuck with bad solutions.
    private final int maxLocalOptimumCount = 2500;
    private int localOptimaCounter = 0;

    private DoubleArraySolution[] population;
    private Random random = new Random();

    public GeneticElitistic(ISelectOperator<DoubleArraySolution> selectOperator, int populationSize, IFitnessFunction<DoubleArraySolution, Double> function,
                                      double wantedFitness, int maxGenerations, NormalMutationOperator mutationOperator, ICrossoverOperator<DoubleArraySolution> crossoverOperator) {
        this.selectOperator = selectOperator;
        this.populationSize = populationSize;
        this.function = function;
        this.wantedFitness = wantedFitness;
        this.maxGenerations = maxGenerations;
        this.mutationOperator = mutationOperator;
        this.crossoverOperator = crossoverOperator;
    }

    @Override
    public DoubleArraySolution run() {
        createPopulation();
        if (evaluate() >= wantedFitness) {
            return bestSolution;
        }

        int it = 0;
        localOptimaCounter = 0;
        while (it++ < maxGenerations) {
            DoubleArraySolution[] newGeneration = new DoubleArraySolution[populationSize];

            newGeneration[0] = population[populationSize - 1];
            newGeneration[1] = population[populationSize - 2];
            for (int i = 2; i < populationSize; i++) {
                DoubleArraySolution parent1 = selectOperator.select(population);
                DoubleArraySolution parent2 = selectOperator.select(population, parent1);
                DoubleArraySolution offspring = crossoverOperator.crossover(parent1, parent2);
                offspring = mutationOperator.mutation(offspring);
                newGeneration[i] = offspring;
            }

            population = newGeneration;
            double currentFitness;
            if ((currentFitness = evaluate()) >= wantedFitness) {
                return bestSolution;
            }
            if (Math.abs(currentFitness - bestSolution.fitness) < 1e-12 || currentFitness < bestSolution.fitness) {
                localOptimaCounter++;
                if (localOptimaCounter == maxLocalOptimumCount && currentFitness < -10) {
                    createPopulation();
                    mutationOperator.reset();
                    System.out.println("Algorithm stuck in local optimum. Reseting population.");
                    localOptimaCounter = 0;
                } else if (localOptimaCounter == maxLocalOptimumCount) {
                    localOptimaCounter = 0;
                }
            }

            //System.out.println("Best solution in generation " + it + ": " + population[0] + "; fitness: " + currentFitness);
        }

        return bestSolution;
    }

    private void createPopulation() {
        population = new DoubleArraySolution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = new DoubleArraySolution(function.variableCount());
            population[i].randomize(random, MIN_VALUE, MAX_VALUE);
        }
    }

    private double evaluate() {
        double best = -Double.MAX_VALUE;
        for (DoubleArraySolution solution : population) {
            solution.fitness = function.calculateFitness(solution);
            if (solution.fitness > best) {
                best = solution.fitness;
            }
            if (bestSolution == null || solution.fitness > bestSolution.fitness) {
                if (bestSolution == null || solution.fitness - bestSolution.fitness > 1e-6) localOptimaCounter = 0;
                bestSolution = solution;
                System.out.println("Found new best solution: " + bestSolution + "; Fitness: " + bestSolution.fitness);
            }
        }

        Arrays.sort(population);
        return best;
    }

}
