package hr.fer.zemris.optjava.dz8.algorithm.DE.crossover;

import hr.fer.zemris.optjava.dz8.algorithm.Solution;

import java.util.Random;

public class ExponentialCrossover implements ICrossover {
    private double crProbability;
    private int dimensionCount;
    private Random random = new Random();

    public ExponentialCrossover(double crProbability, int dimensionCount) {
        this.crProbability = crProbability;
        this.dimensionCount = dimensionCount;
    }

    @Override
    public Solution crossover(Solution targetVector, Solution mutatedVector) {
        double[] probeVector = new double[dimensionCount];
        int start = random.nextInt(dimensionCount);
        int index = start;
        while (true) {
            probeVector[index] = mutatedVector.getVector()[index];
            index++;
            index %= dimensionCount;
            if (Math.random() > crProbability) {
                break;
            }
        }

        while (index != start) {
            probeVector[index] = targetVector.getVector()[index];
            index++;
            index %= dimensionCount;
        }

        return new Solution(probeVector);
    }
}
