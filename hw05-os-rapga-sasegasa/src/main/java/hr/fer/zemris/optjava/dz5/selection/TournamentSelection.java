package hr.fer.zemris.optjava.dz5.selection;

import hr.fer.zemris.optjava.dz5.solution.Solution;

import java.util.*;

public class TournamentSelection<T extends Solution> implements ISelectOperator<T> {
    private int n;
    private Random random = new Random();

    public TournamentSelection(int n) {
        this.n = n;
    }

    @Override
    public T select(List<T> population) {
        if (population.size() <= n) {
            Collections.sort(population);
            return population.get(population.size() - 1);
        }

        boolean[] taken = new boolean[population.size()];
        List<T> contenders = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(population.size());
            while (taken[index]) index = random.nextInt(population.size());
            contenders.add(population.get(index));
            taken[index] = true;
        }

        Collections.sort(contenders);
        return contenders.get(contenders.size()-1);
    }
}
