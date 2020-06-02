package hr.fer.zemris.optjava.dz4.part2;

import java.util.Random;

public class BoxSolution implements Comparable<BoxSolution> {
    public int[] indexes;
    public double fitness;

    public BoxSolution(int n) {
        this.indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
    }

    public BoxSolution duplicate() {
        BoxSolution duplicate = new BoxSolution(indexes.length);
        for (int i = 0; i < indexes.length; i++) {
            duplicate.indexes[i] = indexes[i];
        }

        return duplicate;
    }

    public void randomize() {
        Random random = new Random();
        for (int i = indexes.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = indexes[index];
            indexes[index] = indexes[i];
            indexes[i] = temp;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i : indexes) {
            sb.append(i + " ");
        }

        return sb.toString();
    }


    @Override
    public int compareTo(BoxSolution o) {
        return Double.compare(fitness, o.fitness);
    }
}
