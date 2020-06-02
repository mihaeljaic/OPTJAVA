package hr.fer.zemris.optjava.dz5.function;

import hr.fer.zemris.optjava.dz5.solution.IndexPermutation;

public class FactoryFitnessFunction implements IFitnessFunction<IndexPermutation> {
    private double[][] distanceMatrix;
    private int[][] quantityMatrix;
    private int n;

    public FactoryFitnessFunction(int n, double[][] distanceMatrix, int[][] quantityMatrix) {
        this.n = n;
        this.distanceMatrix = distanceMatrix;
        this.quantityMatrix = quantityMatrix;
    }

    @Override
    public int getVariableCount() {
        return n;
    }

    @Override
    public double calculateFitness(IndexPermutation solution) {
        Integer[] indexes = solution.value;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum += quantityMatrix[indexes[i]][indexes[j]] * distanceMatrix[i][j];
            }
        }

        return -sum;
    }
}
