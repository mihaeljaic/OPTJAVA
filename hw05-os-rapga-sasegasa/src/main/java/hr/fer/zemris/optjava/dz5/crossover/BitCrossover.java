package hr.fer.zemris.optjava.dz5.crossover;

import hr.fer.zemris.optjava.dz5.solution.BitVectorSolution;

import java.util.Random;

public class BitCrossover implements ICrossover<BitVectorSolution> {
    private Random random = new Random();

    @Override
    public BitVectorSolution crossover(BitVectorSolution parent1, BitVectorSolution parent2) {
        Boolean[] p1 = parent1.value;
        Boolean[] p2 = parent2.value;
        BitVectorSolution offspring = new BitVectorSolution(p1.length);
        Boolean[] value = offspring.value;
        for (int i = 0; i < value.length; i++) {
            if (random.nextBoolean()) {
                value[i] = p1[i];
            } else {
                value[i] = p2[i];
            }
        }

        return offspring;
    }
}
