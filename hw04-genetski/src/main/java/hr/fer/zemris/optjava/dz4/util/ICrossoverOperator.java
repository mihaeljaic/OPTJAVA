package hr.fer.zemris.optjava.dz4.util;

public interface ICrossoverOperator<T> {

    T crossover(T parent1, T parent2);

}
