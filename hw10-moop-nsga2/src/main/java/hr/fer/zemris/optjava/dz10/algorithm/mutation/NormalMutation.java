package hr.fer.zemris.optjava.dz10.algorithm.mutation;

import hr.fer.zemris.optjava.dz10.algorithm.solution.Solution;

import java.util.Random;
import java.util.function.Function;

public class NormalMutation implements IMutation {
    private double deviation;
    private double changeProbability;
    private int iteration;
    private Function<Integer, Double> deviationChange;
    private Random rand = new Random();

    public NormalMutation(double deviation, double changeProbability) {
        this(deviation, changeProbability, iteration -> deviation);
    }

    public NormalMutation(double deviation, double changeProbability, Function<Integer, Double> deviationChange) {
        this.deviation = deviation;
        this.changeProbability = changeProbability;
        this.deviationChange = deviationChange;
    }

    @Override
    public void mutate(Solution solution) {
        boolean change = false;
        double[] decisionSpace = solution.getDecisionSpace();
        for (int i = 0; i < decisionSpace.length; i++) {
            if (rand.nextDouble() < changeProbability) {
                decisionSpace[i] += rand.nextGaussian() * deviation;
                change = true;
            }
        }

        if (!change) {
            int randomIndex = rand.nextInt(decisionSpace.length);
            decisionSpace[randomIndex] += rand.nextGaussian() * deviation;
        }

        deviation = deviationChange.apply(++iteration);
    }
}
