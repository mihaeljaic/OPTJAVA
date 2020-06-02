package hr.fer.zemris.optjava.dz5.algorithm;

import hr.fer.zemris.optjava.dz5.crossover.ICrossover;
import hr.fer.zemris.optjava.dz5.function.IFitnessFunction;
import hr.fer.zemris.optjava.dz5.mutation.IMutationOperator;
import hr.fer.zemris.optjava.dz5.selection.ISelectOperator;
import hr.fer.zemris.optjava.dz5.solution.IndexPermutation;

import java.util.ArrayList;
import java.util.List;

public class SASEGASA implements IOptAlgorithm<IndexPermutation> {
    private List<IndexPermutation> population;
    private int numberOfSubpopulations;
    private List<IndexPermutation>[] subpopulations;
    private IndexPermutation best;
    private double maxSelectionPressure;
    private int maxGenerations;
    private double compFactorLowerBound;
    private double compFactorUpperBound;
    private double comparisonFactor;
    private double successRatio;
    private double compFactorIncrease;
    private IFitnessFunction<IndexPermutation> function;
    private ISelectOperator<IndexPermutation> selectOperator;
    private ICrossover<IndexPermutation> crossover;
    private IMutationOperator<IndexPermutation> mutationOperator;

    public SASEGASA(List<IndexPermutation> population, int numberOfSubpopulations, double maxSelectionPressure, int maxGenerations,
                    double compFactorLowerBound, double compFactorUpperBound, double successRatio, double compFactorIncrease,
                    IFitnessFunction<IndexPermutation> function, ISelectOperator<IndexPermutation> selectOperator,
                    ICrossover<IndexPermutation> crossover, IMutationOperator<IndexPermutation> mutationOperator) {
        this.population = population;
        this.numberOfSubpopulations = numberOfSubpopulations;
        this.maxSelectionPressure = maxSelectionPressure;
        this.maxGenerations = maxGenerations;
        this.compFactorUpperBound = compFactorUpperBound;
        this.successRatio = successRatio;
        this.compFactorIncrease = compFactorIncrease;
        this.function = function;
        this.selectOperator = selectOperator;
        this.crossover = crossover;
        this.mutationOperator = mutationOperator;
        this.compFactorLowerBound = compFactorLowerBound;
        comparisonFactor = compFactorLowerBound;
    }

    @Override
    public IndexPermutation run() {
        evaluatePopulation();
        while (numberOfSubpopulations > 0) {
            assignSubpopulations();
            boolean[] stuck = new boolean[numberOfSubpopulations];
            int generation = 0;
            comparisonFactor = compFactorLowerBound;
            OffspringSelection<IndexPermutation>[] offspringSelection = new OffspringSelection[numberOfSubpopulations];
            for (int i = 0; i < numberOfSubpopulations; i++) {
                offspringSelection[i] = new OffspringSelection<>(comparisonFactor, maxSelectionPressure,
                        successRatio, function, selectOperator, crossover, mutationOperator);
            }
            System.out.println("Number of subpopulations: " + numberOfSubpopulations);
            while (generation < maxGenerations) {
                boolean converged = true;
                for (int i = 0; i < numberOfSubpopulations; i++) {
                    if (!stuck[i]) {
                        IndexPermutation temp = offspringSelection[i].processPopulation(subpopulations[i]);
                        if (temp != null && (best == null || temp.fitness > best.fitness)) {
                            System.out.println("Found new best solution: " + temp + "; Fitness: " + temp.fitness);
                            best = temp;
                        }
                        if (offspringSelection[i].getSelectionPressure() >= maxSelectionPressure) {
                            stuck[i] = true;
                        } else {
                            converged = false;
                        }
                    }
                }
                if (converged) {
                    break;
                }
                comparisonFactor += compFactorIncrease;
                if (comparisonFactor >= compFactorUpperBound) {
                    comparisonFactor = compFactorUpperBound;
                }
                for (int i = 0; i < numberOfSubpopulations; i++) {
                    offspringSelection[i].setComparisonFactor(comparisonFactor);
                }
                generation++;
            }
            repopulate();
            numberOfSubpopulations--;
        }

        return best;
    }

    private void repopulate() {
        population.clear();
        for (int i = 0; i < numberOfSubpopulations; i++) {
            for (IndexPermutation solution : subpopulations[i]) {
                population.add(solution);
            }
        }
    }

    private void evaluatePopulation() {
        for (IndexPermutation solution : population) {
            double fitness = function.calculateFitness(solution);
            solution.fitness = fitness;
            if (best == null || fitness > best.fitness) {
                best = solution;
            }
        }
    }

    private void assignSubpopulations() {
        int unitCount = 0;
        int[] subpopulationSizes = new int[numberOfSubpopulations];
        for (int i = 0; i < numberOfSubpopulations; i++) {
            int temp = population.size() / numberOfSubpopulations;
            subpopulationSizes[i] = temp;
            unitCount += temp;
        }
        int subpopulationIndex = 0;
        while (unitCount < population.size()) {
            subpopulationSizes[subpopulationIndex++]++;
            unitCount++;
        }

        subpopulations = new List[numberOfSubpopulations];
        int populationIndex = 0;
        for (int i = 0; i < numberOfSubpopulations; i++) {
            List<IndexPermutation> subPopulation = new ArrayList<>();
            int subpopulationSize = subpopulationSizes[i];
            for (int j = 0; j < subpopulationSize; j++) {
                subPopulation.add(population.get(populationIndex));
                populationIndex++;
            }
            subpopulations[i] = subPopulation;
        }
    }

}
