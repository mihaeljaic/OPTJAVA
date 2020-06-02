package hr.fer.zemris.optjava.dz8.algorithm.DE.crossover;

import hr.fer.zemris.optjava.dz8.algorithm.Solution;

public interface ICrossover {

    Solution crossover(Solution targetVector, Solution mutatedVector);

}
