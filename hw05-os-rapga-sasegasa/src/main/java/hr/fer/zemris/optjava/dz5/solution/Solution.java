package hr.fer.zemris.optjava.dz5.solution;

public abstract class Solution<T> implements Comparable<Solution> {
    public double fitness;
    public T value;

    @Override
    public int compareTo(Solution o) {
        return Double.compare(fitness, o.fitness);
    }

    public abstract Solution duplicate();

}
