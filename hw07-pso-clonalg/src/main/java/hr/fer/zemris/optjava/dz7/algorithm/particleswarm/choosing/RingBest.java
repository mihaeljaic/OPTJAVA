package hr.fer.zemris.optjava.dz7.algorithm.particleswarm.choosing;

import hr.fer.zemris.optjava.dz7.algorithm.particleswarm.Particle;

public class RingBest implements IBestChooser {
    private Particle[] population;
    private int neighboorhoodDistance;
    private double[][] localBest;
    private double[] localFitnesses;
    private int dimensionCount;

    public RingBest(Particle[] population, int neighboorhoodDistance, int dimensionCount) {
        this.population = population;
        this.neighboorhoodDistance = neighboorhoodDistance;
        this.dimensionCount = dimensionCount;

        localBest = new double[population.length][dimensionCount];
        localFitnesses = new double[population.length];
        for (int i = 0; i < population.length; i++) {
            localFitnesses[i] = Double.MAX_VALUE;
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < population.length; i++) {
            double[] best = localBest[i];
            double lowestError = localFitnesses[i];
            int d = neighboorhoodDistance * 2 + 1;
            while (d > 0) {
                int index = i - d;
                if (index < 0) {
                    index += population.length;
                } else if (index >= population.length) {
                    index -= population.length;
                }

                if (population[index].getBestFitness() < lowestError) {
                    best = population[index].getPersonalBest();
                    lowestError = population[index].getBestFitness();
                }

                d--;
                index++;
            }

            localFitnesses[i] = lowestError;
            for (int j = 0; j < dimensionCount; j++) {
                localBest[i][j] = best[j];
            }
        }
    }

    @Override
    public double[] getBest(int index) {
        return localBest[index];
    }
}
