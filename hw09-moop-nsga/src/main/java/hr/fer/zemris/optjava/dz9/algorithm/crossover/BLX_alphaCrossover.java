package hr.fer.zemris.optjava.dz9.algorithm.crossover;

import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

public class BLX_alphaCrossover implements ICrossover {
    private double alpha;

    public BLX_alphaCrossover(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public Solution crossover(Solution parent1, Solution parent2) {
        double[] decisionSpace = new double[parent1.getDecisionSpace().length];
        for (int i = 0; i < decisionSpace.length; i++) {
            double min = Math.min(parent1.getDecisionSpace()[i], parent2.getDecisionSpace()[i]);
            double max = Math.max(parent1.getDecisionSpace()[i], parent2.getDecisionSpace()[i]);
            double diff = (max - min) * alpha;
            double from = min - diff ;
            double to = max + diff;
            decisionSpace[i] = from + Math.random() * (to - from);
        }

        return new Solution(decisionSpace, parent1.getObjectiveSpace().length);
    }

}
