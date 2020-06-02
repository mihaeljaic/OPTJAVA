package hr.fer.zemris.optjava.dz9.algorithm;

import hr.fer.zemris.optjava.dz9.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.dz9.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.dz9.algorithm.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.algorithm.selection.ISelect;
import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NSGA {
    private MOOPProblem function;
    private int maxIterations;
    private int decisionSpaceDim;
    private SharingFunction sharingFunction;
    private ISelect selectionOperator;
    private ICrossover crossoverOperator;
    private IMutation mutationOperator;
    private double[] minValues;
    private double[] maxValues;
    private double initialFitness;
    private double stepFitness;

    private List<Solution> population;
    private List<Solution> bestSolutions = new ArrayList<>();
    private List<List<Solution>> paretoFronts = new ArrayList<>();

    public NSGA(MOOPProblem function, int maxIterations, int decisionSpaceDim,
                SharingFunction sharingFunction, ISelect selectionOperator, ICrossover crossoverOperator,
                IMutation mutationOperator, double[] minValues, double[] maxValues, double initialFitness,
                double stepFitness) {
        this.function = function;
        this.maxIterations = maxIterations;
        this.decisionSpaceDim = decisionSpaceDim;
        this.sharingFunction = sharingFunction;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.initialFitness = initialFitness;
        this.stepFitness = stepFitness;
    }

    public List<List<Solution>> run(List<Solution> population) {
        this.population = population;

        evaluate();
        paretoFronts = undominatedSort(population);
        calculateFitness();
        for (int i = 0; i < maxIterations; i++) {
            List<Solution> nextGeneration = new ArrayList<>(population.size());
            for (int j = 0; j < population.size(); j++) {
                Solution parent1 = selectionOperator.select(population, -1);
                Solution parent2 = selectionOperator.select(population, population.indexOf(parent1));

                Solution child = crossoverOperator.crossover(parent1, parent2);
                mutationOperator.mutate(child);
                clipDecisionSpace(child);

                nextGeneration.add(child);
            }

            this.population = nextGeneration;

            evaluate();
            paretoFronts = undominatedSort(population);
            calculateFitness();

            //updateBest();
        }

        return paretoFronts;
    }

    private void evaluate() {
        for (int i = 0; i < population.size(); i++) {
            function.evaluateSolution(population.get(i).getDecisionSpace(), population.get(i).getObjectiveSpace());
        }
    }

    private List<List<Solution>> undominatedSort(List<Solution> population) {
        List<Set<Integer>> dominatesSets = new ArrayList<>(population.size());
        List<Integer> dominatedBy = new ArrayList<>(population.size());
        for (int i = 0; i < population.size(); i++) {
            dominatesSets.add(new HashSet<>());
            dominatedBy.add(0);
        }

        for (int i = 0; i < population.size(); i++) {
            double[] objectiveSpace = population.get(i).getObjectiveSpace();
            for (int j = 0; j < population.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (dominates(objectiveSpace, population.get(j).getObjectiveSpace())) {
                    dominatesSets.get(j).add(i);
                    dominatedBy.set(i, dominatedBy.get(i) + 1);
                }
            }
        }

        List<List<Solution>> paretoFronts = new ArrayList<>();
        boolean[] taken = new boolean[population.size()];
        while (true) {
            List<Solution> paretoFront = new ArrayList<>();
            List<Integer> tempDominatedBy = new ArrayList<>(population.size());
            for (int i = 0; i < population.size(); i++) {
                tempDominatedBy.add(dominatedBy.get(i));
            }
            for (int i = 0; i < population.size(); i++) {
                if (!taken[i] && tempDominatedBy.get(i) == 0) {
                    paretoFront.add(population.get(i));
                    taken[i] = true;
                    for (int dominates : dominatesSets.get(i)) {
                        dominatedBy.set(dominates, dominatedBy.get(dominates) - 1);
                    }
                }
            }

            if (paretoFront.isEmpty()) {
                break;
            }
            paretoFronts.add(paretoFront);
        }

        return paretoFronts;
    }

    private void calculateFitness() {
        double currentFitness = initialFitness;
        for (int i = 0; i < paretoFronts.size(); i++) {
            List<Solution> solutions = paretoFronts.get(i);
            double minFitness = currentFitness;
            for (Solution solution : solutions) {
                double nicheDensity = sharingFunction.calculateNicheDensity(solutions, solution);
                solution.setFitness(currentFitness / nicheDensity);
                if (currentFitness / nicheDensity < minFitness) {
                    minFitness = currentFitness / nicheDensity;
                }
            }

            currentFitness = minFitness - stepFitness;
        }
    }

    private void clipDecisionSpace(Solution solution) {
        double[] decisionSpace = solution.getDecisionSpace();
        for (int i = 0; i < decisionSpaceDim; i++) {
            decisionSpace[i] = Math.max(minValues[i], decisionSpace[i]);
            decisionSpace[i] = Math.min(maxValues[i], decisionSpace[i]);
        }
    }

    private void updateBest() {
        if (bestSolutions.isEmpty()) {
            for (Solution solution : paretoFronts.get(0)) {
                bestSolutions.add(solution);
            }
            return;
        }

        List<Solution> zeroFront = paretoFronts.get(0);
        int contendersCount = zeroFront.size() + bestSolutions.size();
        boolean[] dominated = new boolean[contendersCount];

        for (int i = 0; i < zeroFront.size(); i++) {
            updateDominated(zeroFront.get(i).getDecisionSpace(), zeroFront, bestSolutions, dominated);
        }

        for (int i = 0; i < bestSolutions.size(); i++) {
            updateDominated(bestSolutions.get(i).getDecisionSpace(), zeroFront, bestSolutions, dominated);
        }

        List<Solution> newBest = new ArrayList<>();
        for (int i = 0; i < contendersCount; i++) {
            if (!dominated[i]) {
                if (i < zeroFront.size()) {
                    newBest.add(zeroFront.get(i));
                } else {
                    newBest.add(bestSolutions.get(i - zeroFront.size()));
                }
            }
        }

        bestSolutions = newBest;
    }

    private void updateDominated(double[] decisionSpace, List<Solution> zeroFront, List<Solution> bestSolutions, boolean[] dominated) {
        for (int j = 0; j < zeroFront.size(); j++) {
            if (dominated[j]) {
                continue;
            }
            if (dominates(decisionSpace, zeroFront.get(j).getDecisionSpace())) {
                dominated[j] = true;
            }
        }
        for (int j = 0; j < bestSolutions.size(); j++) {
            if (dominated[zeroFront.size() + j]) {
                continue;
            }
            if (dominates(decisionSpace, zeroFront.get(j).getDecisionSpace())) {
                dominated[zeroFront.size() + j] = true;
            }
        }
    }

    private boolean dominates(double[] decisionSpace1, double[] decisionSpace2) {
        boolean dominates = false;
        for (int k = 0; k < decisionSpaceDim; k++) {
            if (decisionSpace1[k] < decisionSpace2[k]) {
                return false;
            } else if (decisionSpace1[k] > decisionSpace2[k]) {
                dominates = true;
            }
        }

        return dominates;
    }

}
