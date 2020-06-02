package hr.fer.zemris.generic.ga.crossover;

import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class UniformCrossover implements ICrossover<IntegerArraySolution> {

    @Override
    public IntegerArraySolution crossover(IntegerArraySolution parent1, IntegerArraySolution parent2, IRNG rng) {
        int n = parent1.getN();
        IntegerArraySolution offspring = new IntegerArraySolution(n);

        int[] data = offspring.getData();
        int[] parent1data = parent1.getData();
        int[] parent2data = parent2.getData();
        for (int i = 0; i < n; i++) {
            if (rng.nextDouble() < 0.5) {
                data[i] = parent1data[i];
            } else {
                data[i] = parent2data[i];
            }
        }

        return offspring;
    }
}
