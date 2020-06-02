package hr.fer.zemris.optjava.dz9.algorithm.selection;

import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

import java.util.List;

public interface ISelect {

    Solution select(List<Solution> population, int excluded);

}
