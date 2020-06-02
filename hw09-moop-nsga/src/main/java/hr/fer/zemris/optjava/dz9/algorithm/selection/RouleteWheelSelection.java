package hr.fer.zemris.optjava.dz9.algorithm.selection;

import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

import java.util.List;

public class RouleteWheelSelection implements ISelect {

    @Override
    public Solution select(List<Solution> population, int excluded) {
        double worstFitness = Double.MAX_VALUE;
        double fitnessSum = 0.0;
        for (int i = 0; i < population.size(); i++) {
            Solution solution = population.get(i);
            if (i == excluded) {
                continue;
            }
            fitnessSum += solution.getFitness();
            if (solution.getFitness() < worstFitness) {
                worstFitness = solution.getFitness();
            }
        }

        fitnessSum -= population.size() * worstFitness;
        double rand = Math.random();
        double sum = 0.0;
        for (int i = 0; i < population.size(); i++) {
            if (i == excluded) {
                continue;
            }
            Solution solution = population.get(i);
            sum += (solution.getFitness() - worstFitness) / fitnessSum;
            if (sum >= rand) {
                return solution;
            }
        }

        return population.get(population.size() - 1);
    }

}
