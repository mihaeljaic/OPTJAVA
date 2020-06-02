package hr.fer.zemris.generic.ga.selection;

import hr.fer.zemris.optjava.rng.IRNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentSelect<T extends Comparable<T>> implements ISelection<T> {
    private int contendersCount;
    private boolean best;

    public TournamentSelect(int contendersCount, boolean best) {
        this.contendersCount = contendersCount;
        this.best = best;
    }

    @Override
    public T select(List<T> population, IRNG random, T... excluded) {
        final int populationSize = population.size();
        boolean[] cantCompete = new boolean[populationSize];
        for (int i = 0; i < populationSize; i++) {
            T unit = population.get(i);
            for (T ex : excluded) {
                if (unit.equals(ex)) {
                    cantCompete[i] = true;
                    break;
                }
            }
        }

        List<T> contenders = new ArrayList<>(contendersCount);
        for (int i = 0; i < contendersCount; i++) {
            int index = random.nextInt(0, populationSize - 1);
            while (cantCompete[index]) {
                index = random.nextInt(0, populationSize - 1);
            }
            cantCompete[index] = true;
            contenders.add(population.get(index));
        }

        Collections.sort(contenders);
        return best ? contenders.get(0) : contenders.get(contenders.size() - 1);
    }
}
