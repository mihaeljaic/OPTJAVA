package hr.fer.zemris.optjava.dz7.algorithm.imune.selection;

import hr.fer.zemris.optjava.dz7.algorithm.imune.Antibody;

import java.util.Arrays;

public class ElitisticSelection implements IClonalSelection {

    @Override
    public Antibody[] select(Antibody[] population, int n) {
        Antibody[] selected = new Antibody[n];
        Arrays.sort(population, (a, b) -> Double.compare(b.getAffinity(), a.getAffinity()));
        for (int i = 0; i < n; i++) {
            selected[i] = population[i];
        }

        return selected;
    }
}
