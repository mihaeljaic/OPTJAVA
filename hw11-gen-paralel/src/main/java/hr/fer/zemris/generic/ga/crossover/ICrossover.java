package hr.fer.zemris.generic.ga.crossover;

import hr.fer.zemris.optjava.rng.IRNG;

public interface ICrossover<T> {

    T crossover(T parent1, T parent2, IRNG rng);
}
