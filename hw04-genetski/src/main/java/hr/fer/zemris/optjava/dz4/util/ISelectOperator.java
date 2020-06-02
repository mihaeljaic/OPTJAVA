package hr.fer.zemris.optjava.dz4.util;

public interface ISelectOperator<T> {

    T select(T[] population, T... excluded);

}
