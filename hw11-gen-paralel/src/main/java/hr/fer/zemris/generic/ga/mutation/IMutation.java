package hr.fer.zemris.generic.ga.mutation;

import hr.fer.zemris.generic.ga.solution.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public interface IMutation<T extends GASolution> {

    void mutate(T solution, IRNG rng);
}
