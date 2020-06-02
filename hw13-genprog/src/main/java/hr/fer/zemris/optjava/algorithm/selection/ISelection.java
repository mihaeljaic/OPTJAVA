package hr.fer.zemris.optjava.algorithm.selection;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;

import java.util.List;

public interface ISelection {

    AntSolution select(List<AntSolution> population);
}
