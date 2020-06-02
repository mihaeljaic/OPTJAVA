package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

public class DoubleArraySolution implements Comparable<DoubleArraySolution>{
    public double[] values;
    public double fitness;

    public DoubleArraySolution(int n) {
        values = new double[n];
    }

    public DoubleArraySolution duplicate() {
        DoubleArraySolution duplicate = new DoubleArraySolution(values.length);
        for (int i = 0; i < values.length; i++) {
            duplicate.values[i] = values[i];
        }

        return duplicate;
    }

    public void randomize(Random rand, double min, double max) {
        for (int i = 0; i < values.length; i++) {
            values[i] = min + rand.nextDouble() * (max - min);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (int i = 0; i < values.length; i++) {
            sb.append(" " + values[i]);
            if (i != values.length-1) {
                sb.append(",");
            }
        }

        return sb.append("}").toString();
    }

    @Override
    public int compareTo(DoubleArraySolution o) {
        if (fitness < o.fitness) return -1;
        if (fitness > o.fitness) return  1;
        return 0;
    }
}
