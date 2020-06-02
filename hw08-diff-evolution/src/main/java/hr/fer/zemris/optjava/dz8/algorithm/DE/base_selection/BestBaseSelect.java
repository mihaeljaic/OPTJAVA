package hr.fer.zemris.optjava.dz8.algorithm.DE.base_selection;

import hr.fer.zemris.optjava.dz8.algorithm.Solution;

public class BestBaseSelect implements IBaseSelect {

    @Override
    public Solution select(Solution[] population) {
        Solution best = population[0];
        for (int i = 1; i < population.length; i++) {
            if (population[i].getFitness() < best.getFitness()) {
                best = population[i];
            }
        }

        return best;
    }
}
