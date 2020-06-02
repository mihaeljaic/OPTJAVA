package hr.fer.zemris.optjava.algorithm.crossover;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;

public interface ICrossover {

    AntSolution[] crossover(AntSolution parent1, AntSolution parent2);
}
