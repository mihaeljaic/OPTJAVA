package hr.fer.zemris.optjava.dz9.algorithm.solution;

import java.util.Arrays;

public class Solution {
    private double[] decisionSpace;
    private double[] objectiveSpace;
    private double fitness;

    public Solution(double[] decisionSpace, int n) {
        this.decisionSpace = decisionSpace;
        objectiveSpace = new double[n];
    }

    public double[] getDecisionSpace() {
        return decisionSpace;
    }

    public double[] getObjectiveSpace() {
        return objectiveSpace;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solution)) return false;

        Solution solution = (Solution) o;

        if (Double.compare(solution.fitness, fitness) != 0) return false;
        if (!Arrays.equals(decisionSpace, solution.decisionSpace)) return false;
        return Arrays.equals(objectiveSpace, solution.objectiveSpace);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = Arrays.hashCode(decisionSpace);
        result = 31 * result + Arrays.hashCode(objectiveSpace);
        temp = Double.doubleToLongBits(fitness);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String decisionSpacetoString() {
        StringBuilder sb = new StringBuilder();
        for (double d : decisionSpace) {
            sb.append(d + " ");
        }

        return sb.toString();
    }

    public String objectiveSpacetoString() {
        StringBuilder sb = new StringBuilder();
        for (double d : objectiveSpace) {
            sb.append(d + " ");
        }

        return sb.toString().trim();
    }

}
