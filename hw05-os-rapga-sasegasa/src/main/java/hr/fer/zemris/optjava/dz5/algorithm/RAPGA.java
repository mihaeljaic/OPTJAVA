package hr.fer.zemris.optjava.dz5.algorithm;

import hr.fer.zemris.optjava.dz5.crossover.ICrossover;
import hr.fer.zemris.optjava.dz5.function.IFitnessFunction;
import hr.fer.zemris.optjava.dz5.mutation.IMutationOperator;
import hr.fer.zemris.optjava.dz5.selection.ISelectOperator;
import hr.fer.zemris.optjava.dz5.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class RAPGA<T extends Solution> implements IOptAlgorithm<T> {
    private List<T> population;
    private int minPopulationSize;
    private int maxPopulationSize;
    private double comparisonFactor;
    private double compFactorUpperBound;
    private double compFactorChange;
    private double compFactorLowerBound;
    private double maxSelectionPressure;
    private int compFactorGenerationChange = 100;
    private IFitnessFunction<T> function;
    private ISelectOperator<T> selectOperator;
    private ICrossover<T> crossoverOperator;
    private IMutationOperator<T> mutationOperator;
    private int maxEffort;
    private int maxGenerations;
    private double actualSelectionPressure;
    private T best;

    public RAPGA(List<T> population, int minPopulationSize, int maxPopulationSize, double compFactorLowerBound, double compFactorUpperBound,
                 double compFactorChange, double maxSelectionPressure, IFitnessFunction<T> function, ISelectOperator<T> selectOperator,
                 ICrossover<T> crossoverOperator, IMutationOperator<T> mutationOperator, int maxEffort, int maxGenerations) {
        this.population = population;
        this.minPopulationSize = minPopulationSize;
        this.maxPopulationSize = maxPopulationSize;
        this.comparisonFactor = compFactorLowerBound;
        this.compFactorUpperBound = compFactorUpperBound;
        this.compFactorChange = compFactorChange;
        this.maxSelectionPressure = maxSelectionPressure;
        this.function = function;
        this.selectOperator = selectOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.maxEffort = maxEffort;
        this.maxGenerations = maxGenerations;
        this.compFactorLowerBound = compFactorLowerBound;
    }

    @Override
    public T run() {
        evaluatePopulation();
        int i = 0;
        while (i < maxGenerations && actualSelectionPressure < maxSelectionPressure
                && population.size() >= minPopulationSize) {
            List<T> nextGen = new ArrayList<>();
            int effort = 0;
            while (effort < maxEffort && nextGen.size() < maxPopulationSize) {
                T parent1 = selectOperator.select(population);
                T parent2 = selectOperator.select(population);
                T offspring = crossoverOperator.crossover(parent1, parent2);
                mutationOperator.mutation(offspring);

                offspring.fitness = function.calculateFitness(offspring);
                if (offspring.fitness > best.fitness) {
                    best = offspring;
                }
                if (offspring.fitness >= calculateThreshold(parent1, parent2) && !offspring.equals(parent1)
                        && !offspring.equals(parent2) && !nextGen.contains(offspring)) {
                    nextGen.add(offspring);
                }
                effort++;
            }

            actualSelectionPressure = effort / (double) population.size();
            population = nextGen;
            i++;
            if (i % compFactorGenerationChange == 0) {
                comparisonFactor += compFactorChange;
                if (comparisonFactor >= compFactorUpperBound) {
                    comparisonFactor = compFactorUpperBound;
                }
            }
            System.out.println("Best fitness in generation " + i + ": " + best.fitness);
        }

        return best;
    }

    private void evaluatePopulation() {
        for (T solution : population) {
            solution.fitness = function.calculateFitness(solution);
            if (best == null || solution.fitness > best.fitness) {
                best = solution;
            }
        }
    }

    private double calculateThreshold(T p1, T p2) {
        double fMin = Math.min(p1.fitness, p2.fitness);
        double fMax = Math.max(p1.fitness, p2.fitness);
        return fMin + (fMax - fMin) * comparisonFactor;
    }

}
