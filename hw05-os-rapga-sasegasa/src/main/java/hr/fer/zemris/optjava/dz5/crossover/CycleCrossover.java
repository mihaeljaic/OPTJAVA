package hr.fer.zemris.optjava.dz5.crossover;

import hr.fer.zemris.optjava.dz5.solution.IndexPermutation;

import java.util.Random;

public class CycleCrossover implements ICrossover<IndexPermutation> {
    private Random random = new Random();

    @Override
    public IndexPermutation crossover(IndexPermutation parent1, IndexPermutation parent2) {
        int n = parent1.value.length;
        IndexPermutation offspring = new IndexPermutation(n);

        boolean[] taken = new boolean[n];
        int i = 0;
        boolean first = random.nextBoolean();
        while (i < n) {
            int start = 0;
            while (taken[start]) start++;
            int index = start;
            do {
                int par1 = parent1.value[index];
                int par2 = parent2.value[index];
                taken[index] = true;
                offspring.value[index] = first ? par1 : par2;
                i++;
                for (int j = 0; j < n; j++) {
                    if (parent1.value[j] == par2) {
                        index = j;
                        break;
                    }
                }
            } while (index != start);
            first = !first;
        }

        return offspring;
    }
}
