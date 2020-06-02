package hr.fer.zemris.optjava.dz9.algorithm.selection;

import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

import java.util.*;

public class TournamentSelection implements ISelect {
    private int n;
    private boolean best;
    private Random random = new Random();

    public TournamentSelection(int n, boolean best) {
        this.n = n;
        this.best = best;
    }

    @Override
    public Solution select(List<Solution> population, int excluded) {
        List<Solution> contenders = new ArrayList<>();
        boolean[] cantCompete = new boolean[population.size()];
        if (excluded != -1) {
            cantCompete[excluded] = true;
        }

        for (int i = 0; i < n; i++) {
            int index = random.nextInt(population.size());
            while (cantCompete[index]) {
                index = random.nextInt(population.size());
            }
            cantCompete[index] = true;
            contenders.add(population.get(index));
        }

        Collections.sort(contenders, Comparator.comparing(Solution::getFitness));
        return best ? contenders.get(contenders.size() - 1) : contenders.get(0);
    }

}
