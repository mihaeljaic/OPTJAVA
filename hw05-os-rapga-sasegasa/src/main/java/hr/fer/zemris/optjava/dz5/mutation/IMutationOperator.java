package hr.fer.zemris.optjava.dz5.mutation;

import hr.fer.zemris.optjava.dz5.solution.Solution;

public interface IMutationOperator<T extends Solution> {
    void mutation(T solution);
}
