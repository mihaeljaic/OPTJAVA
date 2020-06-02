package hr.fer.zemris.optjava.dz8.algorithm.DE.crossover;

import hr.fer.zemris.optjava.dz8.algorithm.Solution;

import java.util.Random;

public class UniformCrossover implements ICrossover {
    private double crProbability;
    private int dimensionCount;
    private Random random = new Random();

    public UniformCrossover(double crProbability, int dimensionCount) {
        this.crProbability = crProbability;
        this.dimensionCount = dimensionCount;
    }

    @Override
    public Solution crossover(Solution targetVector, Solution mutatedVector) {
        double[] probeVector = new double[dimensionCount];
        int index = random.nextInt(dimensionCount);
        int end = (index + dimensionCount - 1) % dimensionCount;
        while (index != end) {
            probeVector[index] = Math.random() <= crProbability ? mutatedVector.getVector()[index] : targetVector.getVector()[index];
            index++;
            index %= dimensionCount;
        }

        return new Solution(probeVector);
    }
}
