package hr.fer.zemris.generic.ga.mutation;

import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class UniformMutation implements IMutation<IntegerArraySolution> {
    private int[] minValues;
    private int[] maxValues;
    private int sigma;
    private double probability;

    public UniformMutation(int[] minValues, int[] maxValues, int sigma, double probability) {
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.sigma = sigma;
        this.probability = probability;
    }

    @Override
    public void mutate(IntegerArraySolution solution, IRNG rng) {
        int n = solution.getN();
        int[] data = solution.getData();
        boolean change = false;
        for (int i = 0; i < n; i++) {
            if (rng.nextDouble() < probability) {
                int random = rng.nextInt(-sigma, sigma);
                while (random == 0) random = rng.nextInt(-sigma, sigma);
                int value = data[i] + random;
                value = Math.max(minValues[i % 5], value);
                value = Math.min(maxValues[i % 5], value);
                data[i] = value;
                change = true;
            }
        }

        if (!change) {
            int index = rng.nextInt(0, n - 1);
            int imodulo5 = index % 5;
            int random = rng.nextInt(-sigma, sigma);
            while (random == 0) random = rng.nextInt(-sigma, sigma);
            int value = data[index] + random;
            value = Math.max(minValues[imodulo5], value);
            value = Math.min(maxValues[imodulo5], value);
            data[index] = value;
        }
    }
}
