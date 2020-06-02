package hr.fer.zemris.optjava.algorithm.mutation;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;

public interface IMutation {

    AntSolution mutate(AntSolution solution);
}
