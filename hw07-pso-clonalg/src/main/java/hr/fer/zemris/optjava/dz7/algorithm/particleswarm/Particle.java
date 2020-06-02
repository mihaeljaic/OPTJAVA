package hr.fer.zemris.optjava.dz7.algorithm.particleswarm;

public class Particle {
    private double[] position;
    private double[] velocity;
    private double[] personalBest;
    private double bestFitness = Double.MAX_VALUE;
    private double fitness;

    public Particle(double[] position, double[] velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public double getFitness() {
        return fitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        if (fitness < bestFitness) {
            bestFitness = fitness;
            personalBest = position;
        }
    }

    public double[] getPosition() {
        return position;
    }

    public double[] getPersonalBest() {
        return personalBest;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void updatePosition() {
        for (int i = 0; i < position.length; i++) {
            position[i] += velocity[i];
        }
    }
}
