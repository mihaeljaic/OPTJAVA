package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.util.IMutationOperator;

import java.util.Random;

public class NormalMutationOperator implements IMutationOperator<DoubleArraySolution> {
    private final double initialDeviation;
    private double deviation;
    private double fallout;
    private int devChange;
    private double p;
    private Random rand = new Random();
    private int mutationCounter = 0;

    public NormalMutationOperator(double deviation, double fallout, int devChange, double p) {
        initialDeviation = deviation;
        this.deviation = deviation;
        this.fallout = fallout;
        this.devChange = devChange;
        this.p = p;
    }

    @Override
    public DoubleArraySolution mutation(DoubleArraySolution solution) {
        DoubleArraySolution mutation = solution.duplicate();
        boolean change = false;
        for (int i = 0; i < mutation.values.length; i++) {
            if (rand.nextDouble() < p) {
                mutation.values[i] += rand.nextGaussian() * deviation;
                change = true;
            }
        }

        if (!change) {
            mutation.values[rand.nextInt(mutation.values.length)] += rand.nextGaussian() * deviation;
        }

        mutationCounter++;
        if (mutationCounter == devChange) {
            deviation /= fallout;
            mutationCounter = 0;
        }
        return mutation;
    }

    public void reset() {
        deviation = initialDeviation;
        mutationCounter = 0;
    }

}
