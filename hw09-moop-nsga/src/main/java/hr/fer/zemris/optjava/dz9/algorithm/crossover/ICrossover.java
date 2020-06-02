package hr.fer.zemris.optjava.dz9.algorithm.crossover;

import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

public interface ICrossover {

    Solution crossover(Solution parent1, Solution parent2);

}
