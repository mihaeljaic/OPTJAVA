package hr.fer.zemris.optjava.dz5.crossover;

import hr.fer.zemris.optjava.dz5.solution.Solution;

public interface ICrossover<T extends Solution> {
    T crossover(T parent1, T parent2);
}
