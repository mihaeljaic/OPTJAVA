package hr.fer.zemris.optjava.dz7.algorithm.imune.selection;

import hr.fer.zemris.optjava.dz7.algorithm.imune.Antibody;

import java.util.Arrays;
import java.util.Random;

public class TournamentSelection implements IClonalSelection {
    private static final Random rand = new Random();

    @Override
    public Antibody[] select(Antibody[] population, int n) {
        Antibody[] antibodies = new Antibody[n];
        boolean[] selected = new boolean[population.length];
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(population.length);
            while (selected[index]) {
                index = rand.nextInt(population.length);
            }

            antibodies[i] = population[index];
            selected[index] = true;
        }

        Arrays.sort(antibodies, (a, b) -> Double.compare(b.getAffinity(), a.getAffinity()));
        return antibodies;
    }
}
