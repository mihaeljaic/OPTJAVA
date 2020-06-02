package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.util.ISelectOperator;


public class RouletteWheelSelect implements ISelectOperator<DoubleArraySolution> {

    @Override
    public DoubleArraySolution select(DoubleArraySolution[] population, DoubleArraySolution... excluded) {
        double worstFitness = Double.MAX_VALUE;
        double fitnessSum = 0.0;
        int excludedIndex = -1;
        for (int i = 0; i < population.length; i++) {
            DoubleArraySolution solution = population[i];
            boolean notParticipating = false;
            for (DoubleArraySolution ex : excluded) {
                if (solution == ex) {
                    excludedIndex = i;
                    notParticipating = true;
                    break;
                }
            }
            if (notParticipating) continue;
            fitnessSum += solution.fitness;
            if (solution.fitness < worstFitness) {
                worstFitness = solution.fitness;
            }
        }

        fitnessSum -= population.length * worstFitness;
        double rand = Math.random();
        double sum = 0.0;
        for (int i = 0; i < population.length; i++) {
            if (i == excludedIndex) continue;
            DoubleArraySolution solution = population[i];
            sum += (solution.fitness - worstFitness) / fitnessSum;
            if (sum >= rand) {
                return solution;
            }
        }

        return population[population.length - 1];
    }

}
