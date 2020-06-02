package hr.fer.zemris.optjava.dz8.algorithm.DE;

import hr.fer.zemris.optjava.dz8.ErrorFunction;
import hr.fer.zemris.optjava.dz8.algorithm.DE.base_selection.IBaseSelect;
import hr.fer.zemris.optjava.dz8.algorithm.DE.crossover.ICrossover;
import hr.fer.zemris.optjava.dz8.algorithm.IOptalgorithm;
import hr.fer.zemris.optjava.dz8.algorithm.Solution;

import java.util.Random;
import java.util.function.BiFunction;

public class DifferentialEvolutionAlgorithm implements IOptalgorithm {
    private int populationSize;
    private int maxIterations;
    private int dimensionCount;
    private double targetError;
    private double[] vectorMin;
    private double[] vectorMax;
    private ErrorFunction errorFunction;
    private ICrossover crossoverOperator;
    private IBaseSelect baseSelectOperator;
    private int linearCombinations;
    private double f;
    private BiFunction<Solution, Solution, Solution> selectOperator;

    private Random random = new Random();
    private Solution[] population;
    private Solution bestSolution;

    public DifferentialEvolutionAlgorithm(int populationSize, int maxIterations, int dimensionCount, double targetError,
                                          double[] vectorMin, double[] vectorMax, ErrorFunction errorFunction,
                                          ICrossover crossoverOperator, IBaseSelect baseSelectOperator, int linearCombinations,
                                          double f, BiFunction<Solution, Solution, Solution> selectOperator) {
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.dimensionCount = dimensionCount;
        this.targetError = targetError;
        this.vectorMin = vectorMin;
        this.vectorMax = vectorMax;
        this.errorFunction = errorFunction;
        this.crossoverOperator = crossoverOperator;
        this.baseSelectOperator = baseSelectOperator;
        this.linearCombinations = linearCombinations;
        this.f = f;
        this.selectOperator = selectOperator;
    }

    @Override
    public Solution run() {
        createStartingPopulation();
        evaluate(population);
        int generation = 0;
        while (generation < maxIterations && bestSolution.getFitness() >= targetError) {
            Solution[] probeVectors = new Solution[populationSize];
            for (int i = 0; i < populationSize; i++) {
                Solution targetVector = population[i];
                targetVector.setTaken(true);

                Solution baseVector = baseSelectOperator.select(population);
                baseVector.setTaken(true);

                Solution mutatedVector = calculateMutationVector(baseVector);
                Solution probeVector = crossoverOperator.crossover(targetVector, mutatedVector);
                probeVectors[i] = probeVector;

                targetVector.setTaken(false);
            }

            evaluate(probeVectors);
            for (int i = 0; i < populationSize; i++) {
                population[i] = selectOperator.apply(population[i], probeVectors[i]);
            }

            generation++;
        }

        return bestSolution;
    }

    private void createStartingPopulation() {
        bestSolution = new Solution(new double[dimensionCount]);
        bestSolution.setFitness(Double.MAX_VALUE);
        population = new Solution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            double[] vector = new double[dimensionCount];
            for (int j = 0; j < dimensionCount; j++) {
                vector[j] = vectorMin[j] + Math.random() * (vectorMax[j] - vectorMin[j]);
            }
            population[i] = new Solution(vector);
        }
    }

    private void evaluate(Solution[] solutions) {
        for (Solution solution : solutions) {
            solution.setFitness(errorFunction.calculateError(solution.getVector()));
            if (solution.getFitness() < bestSolution.getFitness()) {
                System.out.println("Found new best solution: " + solution.getFitness());
                bestSolution = solution;
            }
        }
    }

    private Solution calculateMutationVector(Solution baseVector) {
        Solution[] chosenVectors = new Solution[linearCombinations * 2];
        int chosen = 0;
        while (chosen < linearCombinations * 2) {
            int index = random.nextInt(populationSize);
            while (population[index].isTaken()) {
                index = random.nextInt(populationSize);
            }

            chosenVectors[chosen] = population[index];
            population[index].setTaken(true);
            chosen++;
        }

        double[] mutatedVector = new double[dimensionCount];
        for (int i = 0; i < dimensionCount; i++) {
            double value = baseVector.getVector()[i];
            for (int j = 0; j < linearCombinations; j++) {
                value += f * (chosenVectors[2 * j].getVector()[i] - chosenVectors[2 * j + 1].getVector()[i]);
            }
            mutatedVector[i] = value;
        }

        baseVector.setTaken(false);
        for (Solution solution : chosenVectors) {
            solution.setTaken(false);
        }

        return new Solution(mutatedVector);
    }

}
