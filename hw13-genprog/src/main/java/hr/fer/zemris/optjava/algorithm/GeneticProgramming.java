package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.optjava.algorithm.creators.IPopulationCreator;
import hr.fer.zemris.optjava.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.algorithm.selection.ISelection;
import hr.fer.zemris.optjava.algorithm.solution.AntSolution;
import hr.fer.zemris.optjava.algorithm.solution.Node;

import java.util.ArrayList;
import java.util.List;

public class GeneticProgramming {
    private FitnessFunction function;
    private int populationSize;
    private IPopulationCreator creator;
    private ICrossover crossover;
    private IMutation mutation;
    private ISelection selection;
    private int maxFitness;
    private int maxGenerations;
    private double mutationProbability;
    private double crossoverProbability;
    private double reproductionProbability;
    private double punishment;
    private int maximumNodes;
    private int maxDepth;

    private AntSolution best;
    private List<AntSolution> population;

    public GeneticProgramming(FitnessFunction function, int populationSize, IPopulationCreator creator,
                              ICrossover crossover, IMutation mutation, ISelection selection, int maxFitness,
                              int maxGenerations, double mutationProbability, double crossoverProbability,
                              double reproductionProbability, double punishment, int maximumNodes, int maxDepth) {
        this.function = function;
        this.populationSize = populationSize;
        this.creator = creator;
        this.crossover = crossover;
        this.mutation = mutation;
        this.selection = selection;
        this.maxFitness = maxFitness;
        this.maxGenerations = maxGenerations;
        this.mutationProbability = mutationProbability;
        this.crossoverProbability = crossoverProbability;
        this.reproductionProbability = reproductionProbability;
        this.punishment = punishment;
        this.maximumNodes = maximumNodes;
        this.maxDepth = maxDepth;
    }

    public AntSolution run() {
        population = creator.create(populationSize);
        for (AntSolution solution : population) {
            int foodEaten = function.evaluateSolution(solution);
            solution.setFoodEaten(foodEaten);
            if (foodEaten == maxFitness) {
                return solution;
            }
            if (best == null || foodEaten > best.getFoodEaten()) {
                best = solution.copy();
            }
        }

        int generation = 0;
        while (generation < maxGenerations) {
            List<AntSolution> nextGeneration = new ArrayList<>();
            nextGeneration.add(best.copy());

            for (int i = 1; i < populationSize; i++) {
                double rand = Math.random();
                if (rand < reproductionProbability) {
                    nextGeneration.add(selection.select(population).copy());

                } else if (rand < reproductionProbability + crossoverProbability && i < populationSize - 1) {
                    AntSolution parent1 = selection.select(population);
                    AntSolution parent2 = selection.select(population);
                    AntSolution[] offsprings = crossover.crossover(parent1.copy(), parent2.copy());

                    int index = 0;
                    for (AntSolution offspring : offsprings) {
                        if (Node.treeSize(offspring.getRoot()) > maximumNodes) {
                            nextGeneration.add(index == 0 ? parent1 : parent2);
                            index++;
                            continue;
                        }
                        int foodEaten = function.evaluateSolution(offspring);
                        offspring.setFoodEaten(foodEaten);
                        if (foodEaten == maxFitness) {
                            return offspring;
                        }
                        double fitness = foodEaten;
                        if (parent1.getFoodEaten() == foodEaten || parent2.getFoodEaten() == foodEaten) {
                            fitness *= punishment;
                        }
                        offspring.setFitness(fitness);
                        if (foodEaten > best.getFoodEaten()) {
                            best = offspring.copy();
                            System.out.println("Found new best solution: " + foodEaten);
                        }

                        nextGeneration.add(offspring);
                        index++;
                    }
                    i++;

                } else {
                    AntSolution mutant = selection.select(population).copy();
                    mutation.mutate(mutant);

                    int foodEaten = function.evaluateSolution(mutant);
                    mutant.setFoodEaten(foodEaten);
                    mutant.setFitness(foodEaten);
                    if (foodEaten == maxFitness) {
                        return mutant;
                    }
                    if (foodEaten > best.getFitness()) {
                        best = mutant.copy();
                        System.out.println("Found new best solution: " + foodEaten);
                    }

                    nextGeneration.add(mutant);
                }
            }

            population = nextGeneration;
            generation++;
        }

        return best;
    }

}
