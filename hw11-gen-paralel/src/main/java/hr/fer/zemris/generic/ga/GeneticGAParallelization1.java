package hr.fer.zemris.generic.ga;

import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.evaluator.EvaluatorWork;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GeneticGAParallelization1 {
    private int populationSize;
    private int maxGenerations;
    private double wantedFitness;
    private ISelection<IntegerArraySolution> selection;
    private ICrossover<IntegerArraySolution> crossover;
    private IMutation<IntegerArraySolution> mutation;

    private List<IntegerArraySolution> population;
    private IntegerArraySolution bestSolution;
    private Queue<IntegerArraySolution> forEvaluation;
    private Queue<IntegerArraySolution> evaluated;
    private int numberOfWorkers;
    private IRNG rng;

    public GeneticGAParallelization1(int populationSize, int maxGenerations, double wantedFitness,
                            ISelection<IntegerArraySolution> selection, ICrossover<IntegerArraySolution> crossover,
                            IMutation<IntegerArraySolution> mutation) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.wantedFitness = wantedFitness;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        rng = RNG.getRNG();
    }

    public IntegerArraySolution run(List<IntegerArraySolution> startingPopulation) {
        population = startingPopulation;
        forEvaluation = new ConcurrentLinkedQueue<>();
        evaluated = new ConcurrentLinkedDeque<>();

        initializeThreadPool();

        evaluate();

        int generation = 0;
        while (generation < maxGenerations && wantedFitness > bestSolution.fitness) {
            List<IntegerArraySolution> nextGen = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                IntegerArraySolution parent1 = selection.select(population, rng);
                IntegerArraySolution parent2 = selection.select(population, rng);
                IntegerArraySolution offspring = crossover.crossover(parent1, parent2, rng);
                mutation.mutate(offspring, rng);
                nextGen.add(offspring);
            }

            population = nextGen;
            evaluate();
            generation++;
        }

        poisonWorkers();

        return bestSolution;
    }

    private void poisonWorkers() {
        for (int i = 0; i < numberOfWorkers; i++) {
            forEvaluation.add(EvaluatorWork.POISON);
        }
    }

    private void initializeThreadPool() {
        numberOfWorkers = Runtime.getRuntime().availableProcessors();

        EvaluatorWork.setForEvaluation(forEvaluation);
        EvaluatorWork.setEvaluated(evaluated);
        for (int i = 0; i < numberOfWorkers; i++) {
            Thread worker = new Thread(new EvaluatorWork());
            worker.start();
        }
    }

    private void evaluate() {
        for (IntegerArraySolution solution : population) {
            forEvaluation.add(solution);
        }

        population.clear();

        for (int i = 0; i < populationSize; ) {
            IntegerArraySolution solution = evaluated.poll();
            if (solution == null) continue;
            if (bestSolution == null || bestSolution.fitness < solution.fitness) {
                bestSolution = solution;
                System.out.println("Found new best solution: " + solution.fitness);
            }

            population.add(solution);
            i++;
        }
    }
}
