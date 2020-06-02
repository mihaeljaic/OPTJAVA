package hr.fer.zemris.optjava.dz5.selection;

import hr.fer.zemris.optjava.dz5.solution.Solution;

import java.util.List;

public interface ISelectOperator<T extends Solution> {
    T select(List<T> population);
}
