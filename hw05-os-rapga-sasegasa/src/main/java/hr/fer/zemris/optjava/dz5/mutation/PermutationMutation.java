package hr.fer.zemris.optjava.dz5.mutation;

import hr.fer.zemris.optjava.dz5.solution.IndexPermutation;

import java.util.Random;

public class PermutationMutation implements IMutationOperator<IndexPermutation> {
    private Random random = new Random();
    private int n;

    public PermutationMutation(int n) {
        this.n = n;
    }

    @Override
    public void mutation(IndexPermutation solution) {
        int varCount = solution.value.length;
        for (int i = 0; i < n; i++) {
            int rand1 = random.nextInt(varCount);
            int rand2 = random.nextInt(varCount);
            while (rand2 == rand1) rand2 = random.nextInt(varCount);

            int temp = solution.value[rand1];
            solution.value[rand1] = solution.value[rand2];
            solution.value[rand2] = temp;
        }
    }
}
