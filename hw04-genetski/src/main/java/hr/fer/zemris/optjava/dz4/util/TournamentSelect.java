package hr.fer.zemris.optjava.dz4.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TournamentSelect<T extends Comparable<T>> implements ISelectOperator<T> {
    private int n;
    private boolean best;

    public TournamentSelect(int n, boolean best) {
        this.n = n;
        this.best = best;
    }

    @Override
    public T select(T[] population, T... excluded) {
        int populationSize = population.length;
        boolean[] cantCompete = new boolean[populationSize];
        for (int i = 0; i < populationSize; i++) {
            T unit = population[i];
            for (T ex : excluded) {
                if (unit.equals(ex)) {
                    cantCompete[i] = true;
                    break;
                }
            }
        }

        List<T> contenders = new ArrayList<>(n);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(populationSize);
            while (cantCompete[index]) {
                index = random.nextInt(populationSize);
            }
            cantCompete[index] = true;
            contenders.add(population[index]);
        }

        Collections.sort(contenders);
        return best ? contenders.get(contenders.size() - 1) : contenders.get(0);
    }

}
