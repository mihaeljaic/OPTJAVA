package hr.fer.zemris.optjava.dz7.algorithm.imune.selection;

import hr.fer.zemris.optjava.dz7.algorithm.imune.Antibody;

public interface IClonalSelection {
    Antibody[] select(Antibody[] population, int n);
}
