package hr.fer.zemris.optjava.dz8.algorithm.DE.base_selection;

import hr.fer.zemris.optjava.dz8.algorithm.Solution;

import java.util.Random;

public class RandomBaseSelect implements IBaseSelect {
    private Random random = new Random();

    @Override
    public Solution select(Solution[] population) {
        return population[random.nextInt(population.length)];
    }
}
