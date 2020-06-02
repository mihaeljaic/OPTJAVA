package hr.fer.zemris.optjava.dz7.algorithm.particleswarm;

import hr.fer.zemris.optjava.dz7.ErrorFunction;
import hr.fer.zemris.optjava.dz7.algorithm.IOptimizationAlgorithm;
import hr.fer.zemris.optjava.dz7.algorithm.particleswarm.choosing.IBestChooser;

import java.util.function.Function;

public class PSO implements IOptimizationAlgorithm {
    private ErrorFunction errorFunction;
    private int dimensionCount;
    private IBestChooser bestChooser;
    private double individualityFactor;
    private double socialComponent;
    private double[] positionMin;
    private double[] positionMax;
    private double[] velocityMin;
    private double[] velocityMax;
    private int maxIterations;
    private double acceptableError;
    private Function<Integer, Double> inertion;

    private Particle[] population;
    private double[] bestSolution;
    private double lowestError = Double.MAX_VALUE;

    public PSO(ErrorFunction errorFunction, int dimensionCount, IBestChooser bestChooser, double individualityFactor,
               double socialComponent, double[] positionMin, double[] positionMax, double[] velocityMin,
               double[] velocityMax, int maxIterations, double acceptableError, Particle[] population, Function<Integer, Double> inertion) {
        this.errorFunction = errorFunction;
        this.dimensionCount = dimensionCount;
        this.bestChooser = bestChooser;
        this.individualityFactor = individualityFactor;
        this.socialComponent = socialComponent;
        this.positionMin = positionMin;
        this.positionMax = positionMax;
        this.velocityMin = velocityMin;
        this.velocityMax = velocityMax;
        this.maxIterations = maxIterations;
        this.acceptableError = acceptableError;
        this.population = population;
        this.inertion = inertion;
        bestSolution = new double[dimensionCount];
    }

    @Override
    public double[] run() {
        initializePopulation();
        int iteration = 0;
        while (iteration < maxIterations && lowestError > acceptableError) {
            for (int i = 0; i < population.length; i++) {
                population[i].setFitness(errorFunction.calculateError(population[i].getPosition()));
                if (population[i].getFitness() < lowestError) {
                    lowestError = population[i].getFitness();
                    double[] solution = population[i].getPosition();
                    for (int d = 0; d < dimensionCount; d++) {
                        bestSolution[d] = solution[d];
                    }
                    System.out.println("Found new best solution, error: " + lowestError);
                }
            }

            bestChooser.update();
            double velocityFactor = inertion.apply(iteration);
            for (int i = 0; i < population.length; i++) {
                double[] position = population[i].getPosition();
                double[] velocity = population[i].getVelocity();
                double[] personalBest = population[i].getPersonalBest();
                double[] best = bestChooser.getBest(i);
                for (int d = 0; d < dimensionCount; d++) {
                    velocity[d] = velocityFactor * velocity[d] + individualityFactor * Math.random() * (personalBest[d] - position[d])
                            + socialComponent * Math.random() * (best[d] - position[d]);
                }

                fixRange(velocity, velocityMin, velocityMax);
                population[i].updatePosition();
            }

            iteration++;
        }

        return bestSolution;
    }

    private void fixRange(double[] value, double[] minValue, double[] maxValue) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] < minValue[i]) {
                value[i] = minValue[i];
            } else if (value[i] > maxValue[i]) {
                value[i] = maxValue[i];
            }
        }
    }

    private void initializePopulation() {
        for (int i = 0; i < population.length; i++) {
            double[] randomPosition = new double[dimensionCount];
            double[] randomVelocity = new double[dimensionCount];
            for (int d = 0; d < dimensionCount; d++) {
                randomPosition[d] = positionMin[d] + (positionMax[d] - positionMin[d]) * Math.random();
                randomVelocity[d] = velocityMin[d] + (velocityMax[d] - velocityMin[d]) * Math.random();
            }

            population[i] = new Particle(randomPosition, randomVelocity);
        }
    }
}
