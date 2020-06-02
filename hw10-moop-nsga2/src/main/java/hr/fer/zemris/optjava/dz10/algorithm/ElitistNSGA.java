package hr.fer.zemris.optjava.dz10.algorithm;

import hr.fer.zemris.optjava.dz10.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.dz10.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.dz10.algorithm.selection.ISelection;
import hr.fer.zemris.optjava.dz10.algorithm.solution.Solution;
import hr.fer.zemris.optjava.dz10.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz10.util.IndexComparator;

import java.util.*;

public class ElitistNSGA {
    private final int populationSize;
    private final int maxIterations;
    private final MOOPProblem problem;
    private final int decisionSpaceDim;
    private double[] minValues;
    private double[] maxValues;
    private ISelection selection;
    private ICrossover crossover;
    private IMutation mutation;

    private List<List<Solution>> paretoFronts;

    public ElitistNSGA(int populationSize, int maxIterations, MOOPProblem problem, int decisionSpaceDim,
                       double[] minValues, double[] maxValues, ISelection selection, ICrossover crossover, IMutation mutation) {
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.problem = problem;
        this.decisionSpaceDim = decisionSpaceDim;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    public List<List<Solution>> run(List<Solution> startingPopulation) {
        List<Solution> parents = startingPopulation;
        List<Solution> children = new ArrayList<>();
        int iteration = 0;
        while (iteration < maxIterations) {
            List<Solution> population = new ArrayList<>(parents.size() + children.size());
            population.addAll(children);
            population.addAll(parents);

            evaluate(population);
            List<List<Solution>> paretoFronts = undominatedSort(population);
            this.paretoFronts = paretoFronts;

            paretoFronts.forEach(paretoFront -> calculateCrowdingDistances(paretoFront));

            List<Solution> nextParents = new ArrayList<>(populationSize);
            for (List<Solution> paretoFront : paretoFronts) {
                if (nextParents.size() + paretoFront.size() > populationSize) {
                    addMissing(nextParents, paretoFront, populationSize - nextParents.size());
                    break;
                }

                nextParents.addAll(paretoFront);
            }

            List<Solution> nextChildren = new ArrayList<>(populationSize);
            for (int i = 0; i < populationSize; i++) {
                Solution parent1 = selection.select(nextParents);
                Solution parent2 = selection.select(nextParents);

                Solution child = crossover.crossover(parent1, parent2);

                mutation.mutate(child);
                clipDecisionSpace(child);

                nextChildren.add(child);
            }

            parents = nextParents;
            children = nextChildren;

            iteration++;
        }

        return undominatedSort(parents);
    }

    private void calculateCrowdingDistances(List<Solution> front) {
        List<List<Integer>> indexVectors = getIndexVectors(front);

        List<Double> minFunctionValues = new ArrayList<>(problem.getNumberOfObjectives());
        List<Double> maxFunctionValues = new ArrayList<>(problem.getNumberOfObjectives());
        getMinMaxFunctionValues(front, minFunctionValues, maxFunctionValues);

        for (Solution s : front) {
            s.setCrowdingDistance(0.0);
        }

        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            List<Integer> indexVector = indexVectors.get(i);
            final double fMin = minFunctionValues.get(i);
            final double fMax = maxFunctionValues.get(i);

            front.get(indexVector.get(0)).setCrowdingDistance(Double.POSITIVE_INFINITY);
            front.get(indexVector.get(indexVector.size() - 1)).setCrowdingDistance(Double.POSITIVE_INFINITY);
            for (int j = 1; j < indexVector.size() - 1; j++) {
                final int index = indexVector.get(j);
                Solution left = front.get(indexVector.get(j - 1));
                Solution right = front.get(indexVector.get(j + 1));

                final double newDistance = front.get(index).getCrowdingDistance() +
                        (right.getObjectiveSpace()[i] - left.getObjectiveSpace()[i]) / (fMax - fMin);
                front.get(index).setCrowdingDistance(newDistance);
            }
        }
    }

    private void addMissing(List<Solution> nextParents, List<Solution> paretoFront, int amount) {
        paretoFront.sort((s1, s2) -> -Double.compare(s1.getCrowdingDistance(), s2.getCrowdingDistance()));
        nextParents.addAll(paretoFront.subList(0, amount));
    }

    private List<List<Integer>> getIndexVectors(List<Solution> front) {
        List<List<Integer>> indexVectors = new ArrayList<>(problem.getNumberOfObjectives());
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            final int function = i;
            IndexComparator<Solution> comparator = new IndexComparator<>(front,
                    (s1, s2) -> Double.compare(s1.getObjectiveSpace()[function], s2.getObjectiveSpace()[function]));
            List<Integer> indexVector = comparator.createIndexedList();
            indexVector.sort(comparator);

            indexVectors.add(indexVector);
        }

        return indexVectors;
    }

    private void getMinMaxFunctionValues(List<Solution> front, List<Double> minFunctionValues, List<Double> maxFunctionValues) {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            final int function = i;
            Comparator<Solution> comparator =
                    (s1, s2) -> Double.compare(s1.getObjectiveSpace()[function], s2.getObjectiveSpace()[function]);
            minFunctionValues.add(Collections.min(front, comparator).getObjectiveSpace()[function]);
            maxFunctionValues.add(Collections.max(front, comparator).getObjectiveSpace()[function]);
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
                    population.get(i).setFrontIndex(paretoFronts.size());
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

    private void evaluate(List<Solution> solutions) {
        for (Solution solution : solutions) {
            problem.evaluateSolution(solution.getDecisionSpace(), solution.getObjectiveSpace());
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

    private void clipDecisionSpace(Solution solution) {
        double[] decisionSpace = solution.getDecisionSpace();
        for (int i = 0; i < decisionSpaceDim; i++) {
            decisionSpace[i] = Math.max(minValues[i], decisionSpace[i]);
            decisionSpace[i] = Math.min(maxValues[i], decisionSpace[i]);
        }
    }

}
