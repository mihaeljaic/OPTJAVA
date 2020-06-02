package hr.fer.zemris.optjava.dz7.algorithm.particleswarm.choosing;

import hr.fer.zemris.optjava.dz7.algorithm.particleswarm.Particle;

public class GlobalBest implements IBestChooser {
    private Particle[] particles;
    private double[] globalBest;
    private double fitness = Double.MAX_VALUE;

    public GlobalBest(Particle[] particles, int dimensionCount) {
        this.particles = particles;
        globalBest = new double[dimensionCount];
    }

    @Override
    public void update() {
        for (Particle particle : particles) {
            if (particle.getFitness() < fitness) {
                double[] solution = particle.getPosition();
                for (int i = 0; i < solution.length; i++) {
                    globalBest[i] = solution[i];
                }

                fitness = particle.getFitness();
            }
        }
    }

    @Override
    public double[] getBest(int index) {
        return globalBest;
    }
}
