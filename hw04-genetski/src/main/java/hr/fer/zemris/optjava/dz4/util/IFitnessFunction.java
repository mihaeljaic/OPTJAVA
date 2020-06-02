package hr.fer.zemris.optjava.dz4.util;

public interface IFitnessFunction<T, R> {

    R calculateFitness(T solution);

    int variableCount();

}
