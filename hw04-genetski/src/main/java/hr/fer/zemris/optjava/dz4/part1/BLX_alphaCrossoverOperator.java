package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.util.ICrossoverOperator;

public class BLX_alphaCrossoverOperator implements ICrossoverOperator<DoubleArraySolution> {
    private double alpha;

    public BLX_alphaCrossoverOperator(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public DoubleArraySolution crossover(DoubleArraySolution parent1, DoubleArraySolution parent2) {
        DoubleArraySolution child = new DoubleArraySolution(parent1.values.length);
        for (int i = 0; i < parent1.values.length; i++) {
            double min = Math.min(parent1.values[i], parent2.values[i]);
            double max = Math.max(parent1.values[i], parent2.values[i]);
            double diff = (max - min) * alpha;
            double from = min - diff ;
            double to = max + diff;
            child.values[i] = from + Math.random() * (to - from);
        }

        return child;
    }

}
