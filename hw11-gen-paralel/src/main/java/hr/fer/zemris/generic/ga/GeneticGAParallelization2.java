package hr.fer.zemris.generic.ga;

import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.evaluator.Evaluator;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GeneticGAParallelization2 {
    private int populationSize;
    private int maxGenerations;
    private double wantedFitness;
    private ISelection<IntegerArraySolution> selection;
    private ICrossover<IntegerArraySolution> crossover;
    private IMutation<IntegerArraySolution> mutation;

    private List<IntegerArraySolution> population;
    private IntegerArraySolution bestSolution;
    private Queue<GeneticTask> forProcessing;
    private Queue<List<IntegerArraySolution>> processed;
    private int numberOfWorkers;

    public GeneticGAParallelization2(int populationSize, int maxGenerations, double wantedFitness,
                                     ISelection<IntegerArraySolution> selection, ICrossover<IntegerArraySolution> crossover,
                                     IMutation<IntegerArraySolution> mutation) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.wantedFitness = wantedFitness;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    public IntegerArraySolution run(List<IntegerArraySolution> startingPopulation, int tasksCount, int childrenToProduce) throws IOException {
        if (tasksCount * childrenToProduce != populationSize) {
            throw new IllegalArgumentException("Invalid tasks and children numbers.");
        }

        population = startingPopulation;
        forProcessing = new ConcurrentLinkedQueue<>();
        processed = new ConcurrentLinkedDeque<>();

        initializeThreadPool();

        for (IntegerArraySolution solution : population) {
            Evaluator.getEvaluator().evaluate(solution);
            if (bestSolution == null || solution.fitness > bestSolution.fitness) {
                System.out.println("Found new best solution: " + solution.fitness);
                bestSolution = solution;
            }
        }

        int generation = 0;
        while (generation < maxGenerations && wantedFitness > bestSolution.fitness) {
            List<IntegerArraySolution> nextGen = new ArrayList<>();

            for (int i = 0; i < tasksCount; i++) {
                forProcessing.add(new GeneticTask(population, childrenToProduce));
            }

            for (int i = 0; i < tasksCount;) {
                List<IntegerArraySolution> solutions = processed.poll();
                if (solutions == null) continue;

                for (IntegerArraySolution solution : solutions) {
                    if (bestSolution == null || bestSolution.fitness < solution.fitness) {
                        System.out.println("Found new best solution: " + solution.fitness);
                        bestSolution = solution;
                    }
                }

                nextGen.addAll(solutions);
                i++;
            }

            population = nextGen;
            generation++;
        }

        poisonWorkers();

        return bestSolution;
    }

    private void poisonWorkers() {
        for (int i = 0; i < numberOfWorkers; i++) {
            forProcessing.add(GenerationWork.POISON);
        }
    }

    private void initializeThreadPool() {
        numberOfWorkers = Runtime.getRuntime().availableProcessors();
        GenerationWork.setForProcessing(forProcessing);
        GenerationWork.setProcessed(processed);

        for (int i = 0; i < numberOfWorkers; i++) {
            GenerationWork work = new GenerationWork(selection, crossover, mutation);
            Thread worker = new Thread(work);
            worker.start();
        }
    }
}
