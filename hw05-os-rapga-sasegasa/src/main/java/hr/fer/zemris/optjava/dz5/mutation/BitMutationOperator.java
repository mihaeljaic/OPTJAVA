package hr.fer.zemris.optjava.dz5.mutation;

import hr.fer.zemris.optjava.dz5.solution.BitVectorSolution;

import java.util.Random;

public class BitMutationOperator implements IMutationOperator<BitVectorSolution> {
    private double p;
    private Random random = new Random();

    public BitMutationOperator(double p) {
        this.p = p;
    }

    @Override
    public void mutation(BitVectorSolution solution) {
        Boolean[] value = solution.value;
        boolean change = false;
        for (int i = 0; i < value.length; i++) {
            if (random.nextDouble() < p) {
                value[i] ^= true;
                change = true;
            }
        }

        if (!change) {
            value[random.nextInt(value.length)] ^= true;
        }
    }
}
