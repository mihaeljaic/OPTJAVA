package hr.fer.zemris.optjava.dz5.function;

import hr.fer.zemris.optjava.dz5.solution.Solution;

public interface IFitnessFunction <T extends Solution> {
    double calculateFitness(T solution);
    int getVariableCount();
}
