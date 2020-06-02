package hr.fer.zemris.optjava.dz10.algorithm.crossover;

import hr.fer.zemris.optjava.dz10.algorithm.solution.Solution;

public interface ICrossover {

    Solution crossover(Solution parent1, Solution parent2);

}
