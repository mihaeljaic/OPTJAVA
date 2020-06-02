package hr.fer.zemris.optjava.dz10.algorithm.selection;

import hr.fer.zemris.optjava.dz10.algorithm.solution.Solution;

import java.util.List;

public interface ISelection {

    Solution select(List<Solution> population);

}
