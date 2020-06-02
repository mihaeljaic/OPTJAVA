package hr.fer.zemris.generic.ga.selection;

import hr.fer.zemris.optjava.rng.IRNG;

import java.util.List;

public interface ISelection<T> {

    T select(List<T> population, IRNG rng, T... excluded);
}
