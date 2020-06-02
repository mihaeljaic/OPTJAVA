package hr.fer.zemris.generic.ga;

import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;

import java.util.List;

public class GeneticTask {
    private List<IntegerArraySolution> population;
    private int childrenToProduce;

    public GeneticTask(List<IntegerArraySolution> population, int childrenToProduce) {
        this.population = population;
        this.childrenToProduce = childrenToProduce;
    }

    public int getChildrenToProduce() {
        return childrenToProduce;
    }

    public List<IntegerArraySolution> getPopulation() {
        return population;
    }
}
