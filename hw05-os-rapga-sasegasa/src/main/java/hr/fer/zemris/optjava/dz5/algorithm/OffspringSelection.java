package hr.fer.zemris.optjava.dz5.algorithm;

import hr.fer.zemris.optjava.dz5.crossover.ICrossover;
import hr.fer.zemris.optjava.dz5.function.IFitnessFunction;
import hr.fer.zemris.optjava.dz5.mutation.IMutationOperator;
import hr.fer.zemris.optjava.dz5.selection.ISelectOperator;
import hr.fer.zemris.optjava.dz5.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class OffspringSelection<T extends Solution> {
    private double comparisonFactor;
    private double maxSelPress;
    private double successRatio;
    private double selectionPressure;
    private IFitnessFunction<T> function;
    private ISelectOperator<T> selectOperator;
    private ICrossover<T> crossoverOperator;
    private IMutationOperator<T> mutationOperator;

    public OffspringSelection(double comparisonFactor, double maxSelPress, double successRatio, IFitnessFunction<T> function,
                              ISelectOperator<T> selectOperator, ICrossover<T> crossoverOperator, IMutationOperator<T> mutationOperator) {
        this.comparisonFactor = comparisonFactor;
        this.maxSelPress = maxSelPress;
        this.successRatio = successRatio;
        this.function = function;
        this.selectOperator = selectOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
    }

    public void setComparisonFactor(double comparisonFactor) {
        this.comparisonFactor = comparisonFactor;
    }

    public double getSelectionPressure() {
        return selectionPressure;
    }

    public T processPopulation(List<T> population) {
        T best = null;
        List<T> nextGen = new ArrayList<>();
        List<T> pool = new ArrayList<>();
        int successSize = Math.max(1, (int) Math.round(successRatio * population.size()));
        int maxPoolsize = population.size() - successSize;
        int effort = 0;
        while ((nextGen.size() + pool.size() < population.size()) && effort++ < population.size() * maxSelPress) {
            T parent1 = selectOperator.select(population);
            T parent2 = selectOperator.select(population);
            T offspring = crossoverOperator.crossover(parent1, parent2);
            mutationOperator.mutation(offspring);

            double offspringFitness = function.calculateFitness(offspring);
            offspring.fitness = offspringFitness;
            if (best == null || offspringFitness > best.fitness) {
                best = offspring;
            }
            if (offspringFitness > calculateThreshold(parent1, parent2) && !offspring.equals(parent1)
                    && !offspring.equals(parent2) && !nextGen.contains(offspring)) {
                nextGen.add(offspring);
            } else if (pool.size() < maxPoolsize){
                pool.add(offspring);
            }

        }

        successRatio = (double) nextGen.size() / effort;
        population.clear();
        for (T solution : nextGen) {
            population.add(solution);
        }
        for (T solution : pool) {
            population.add(solution);
        }

        selectionPressure = (double) effort / population.size();
        return best;
    }

    private double calculateThreshold(T parent1, T parent2) {
        double fMin = Math.min(parent1.fitness, parent2.fitness);
        double fMax = Math.max(parent1.fitness, parent2.fitness);
        return fMin + (fMax - fMin) * comparisonFactor;
    }

}
