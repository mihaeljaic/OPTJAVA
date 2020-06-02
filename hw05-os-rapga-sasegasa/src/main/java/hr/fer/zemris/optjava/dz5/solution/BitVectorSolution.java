package hr.fer.zemris.optjava.dz5.solution;

import java.util.Random;

public class BitVectorSolution extends Solution<Boolean[]> {
    private int n;
    private Random random = new Random();

    public BitVectorSolution(int n) {
        this.n = n;
        value = new Boolean[n];
    }

    public BitVectorSolution duplicate() {
        BitVectorSolution solution = new BitVectorSolution(n);
        for (int i = 0; i < n; i++) {
            solution.value[i] = value[i];
        }

        return solution;
    }

    public void randomize() {
        for (int i = 0; i < n; i++) {
            value[i] = random.nextBoolean();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BitVectorSolution)) {
            return false;
        }
        BitVectorSolution other = (BitVectorSolution) obj;
        if (Math.abs(fitness - other.fitness) > 1e-9) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            if (value[i] != other.value[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Boolean b : value) {
            sb.append(b ? "1" : "0");
        }

        return sb.toString();
    }
}
