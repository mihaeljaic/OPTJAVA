package hr.fer.zemris.optjava.dz7.algorithm.imune.mutation;

import hr.fer.zemris.optjava.dz7.algorithm.imune.Antibody;

import java.util.Random;
import java.util.function.Function;

public class UniformHyperMutation implements IHyperMutation {
    private static final Random random = new Random();
    private double ro;
    private double[] delta;
    private Function<Double, Double> deltaChange;
    private int deltaChangeRate;
    private Function<Double, Double> affinityNormalization;

    private int iterationCounter = 0;

    public UniformHyperMutation(double ro, double[] delta) {
        this(ro, delta, x -> x, Integer.MAX_VALUE, x -> x);
    }

    public UniformHyperMutation(double ro, double[] delta, Function<Double, Double> deltaChange, int deltaChangeRate) {
        this(ro, delta, deltaChange, deltaChangeRate, x -> x);
    }

    public UniformHyperMutation(double ro, double[] delta, Function<Double, Double> deltaChange, int deltaChangeRate,
                                Function<Double, Double> affinityNormalization) {
        this.ro = ro;
        this.delta = delta;
        this.deltaChange = deltaChange;
        this.deltaChangeRate = deltaChangeRate;
        this.affinityNormalization = affinityNormalization;
    }

    @Override
    public void hyperMutate(Antibody antibody) {
        double p = Math.max(0.05, Math.exp(-ro * affinityNormalization.apply(antibody.getAffinity())));
        boolean change = false;
        double[] position = antibody.getPosition();
        for (int d = 0; d < position.length; d++) {
            if (Math.random() < p) {
                position[d] += Math.random() * 2 * delta[d] - delta[d];
                change = true;
            }
        }

        if (!change) {
            int index = random.nextInt(position.length);
            position[index] += Math.random() * 2 * delta[index] - delta[index];
        }

        antibody.setEvaluated(false);

        iterationCounter++;
        if (iterationCounter == deltaChangeRate) {
            iterationCounter = 0;
            for (int i = 0; i < delta.length; i++) {
                delta[i] = deltaChange.apply(delta[i]);
            }
        }
    }
}
