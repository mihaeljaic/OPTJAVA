package hr.fer.zemris.optjava.dz7.algorithm.imune;

import hr.fer.zemris.optjava.dz7.ErrorFunction;
import hr.fer.zemris.optjava.dz7.algorithm.IOptimizationAlgorithm;
import hr.fer.zemris.optjava.dz7.algorithm.imune.mutation.IHyperMutation;
import hr.fer.zemris.optjava.dz7.algorithm.imune.selection.IClonalSelection;

import java.util.ArrayList;
import java.util.List;

public class CloningAlgorithm implements IOptimizationAlgorithm {
    private ErrorFunction antigen;
    private int maxIterations;
    private double acceptableError;
    private int dimensionCount;
    private int populationSize;
    private double beta;
    private int n; // Number of antibodies picked for cloning
    private int d; // Number of random antibodies to insert at the end of each generation
    private Antibody[] population;
    private IClonalSelection clonalSelection;
    private IHyperMutation hyperMutation;
    private double[] delta;

    private double highestAffinity;
    private double[] bestSolution;

    public CloningAlgorithm(ErrorFunction antigen, int maxIterations, double acceptableError, int dimensionCount,
                            int populationSize, double beta, int n, int d, Antibody[] population,
                            IClonalSelection clonalSelection, IHyperMutation hyperMutation, double[] delta) {
        this.antigen = antigen;
        this.maxIterations = maxIterations;
        this.acceptableError = acceptableError;
        this.dimensionCount = dimensionCount;
        this.populationSize = populationSize;
        this.beta = beta;
        this.n = n;
        this.d = d;
        this.population = population;
        this.clonalSelection = clonalSelection;
        this.hyperMutation = hyperMutation;
        this.delta = delta;
        bestSolution = new double[dimensionCount];
        highestAffinity = -Double.MAX_VALUE;
    }

    @Override
    public double[] run() {
        int iteration = 0;
        while (iteration < maxIterations && -highestAffinity > acceptableError) {
            evaluate(population);

            Antibody[] forCloning = clonalSelection.select(population, n);
            Antibody[] cloned = clone(forCloning);
            hyperMutate(cloned);
            evaluate(cloned);

            Antibody[] chosen = clonalSelection.select(cloned, populationSize - d);
            for (int i = 0; i < chosen.length; i++) {
                population[i] = chosen[i];
            }
            addRandom();

            iteration++;
        }

        return bestSolution;
    }

    private void addRandom() {
        for (int i = n; i < populationSize; i++) {
            double[] position = new double[dimensionCount];
            for (int d = 0; d < dimensionCount; d++) {
                position[d] = -delta[d] + Math.random() * 2 * delta[d];
            }

            population[i] =  new Antibody(position);
        }
    }

    private void hyperMutate(Antibody[] cloned) {
        for (int i = 0; i < cloned.length; i++) {
            hyperMutation.hyperMutate(cloned[i]);
        }
    }

    private Antibody[] clone(Antibody[] forCloning) {
        List<Antibody> temp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int numberOfClones = (int) Math.round(beta * populationSize / (i + 1));
            for (int j = 0; j < numberOfClones; j++) {
                temp.add(forCloning[i].clone());
            }
        }

        Antibody[] cloned = new Antibody[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            cloned[i] = temp.get(i);
        }

        return cloned;
    }

    private void evaluate(Antibody[] antibodies) {
        for (int i = 0; i < antibodies.length; i++) {
            Antibody antibody = antibodies[i];
            if (antibody.isEvaluated()) {
                continue;
            }

            antibody.setAffinity(-antigen.calculateError(antibody.getPosition()));
            if (antibody.getAffinity() > highestAffinity) {
                highestAffinity = antibody.getAffinity();
                double[] newBest = antibody.getPosition();
                for (int d = 0; d < dimensionCount; d++) {
                    bestSolution[d] = newBest[d];
                }

                System.out.println("Found new best solution. Error: " + (-highestAffinity));
            }
        }
    }

}
