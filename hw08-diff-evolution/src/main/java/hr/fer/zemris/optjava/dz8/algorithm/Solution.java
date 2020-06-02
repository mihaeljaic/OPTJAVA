package hr.fer.zemris.optjava.dz8.algorithm;

public class Solution {
    private double[] vector;
    private double fitness;
    private boolean taken;

    public Solution(double[] vector) {
        this.vector = vector;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getVector() {
        return vector;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public boolean isTaken() {
        return taken;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double value : vector) {
            sb.append(Double.toString(value) + " ");
        }

        return sb.toString();
    }
}
