package hr.fer.zemris.generic.ga.solution;

public class IntegerArraySolution extends GASolution<int[]> implements Comparable<IntegerArraySolution> {
    private int n;

    public IntegerArraySolution(int n) {
        this.n = n;
        this.data = new int[n];
    }

    public IntegerArraySolution(int[] data) {
        this.data = data;
        n = data.length;
    }

    public int getN() {
        return n;
    }

    @Override
    public GASolution<int[]> duplicate() {
        return null;
    }

    @Override
    public int compareTo(IntegerArraySolution o) {
        return -Double.compare(this.fitness, o.fitness);
    }
}
