package hr.fer.zemris.optjava.rng.rngimpl;

import hr.fer.zemris.optjava.rng.IRNG;

import java.util.Random;

public class RNGRandomImpl implements IRNG {
    private final Random random;

    public RNGRandomImpl() {
        this.random = new Random();
    }

    public double nextDouble() {
        return random.nextDouble();
    }

    public double nextDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public float nextFloat() {
        return random.nextFloat();
    }

    public float nextFloat(float min, float max) {
        return min + (max - min) * random.nextFloat();
    }

    public int nextInt() {
        return random.nextInt();
    }

    public int nextInt(int min, int max) {
        return min + (int) Math.round((max - min) * random.nextDouble());
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    public double nextGaussian() {
        return random.nextGaussian();
    }
}
