package hr.fer.zemris.optjava.algorithm.creators;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;

import java.util.List;

public interface IPopulationCreator {

    List<AntSolution> create(int populationSize);

    AntSolution create(int maxNodes, int maxDepth, int minNodes);
}
