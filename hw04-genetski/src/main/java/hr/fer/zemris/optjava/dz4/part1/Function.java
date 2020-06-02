package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.util.IFitnessFunction;

public class Function implements IFitnessFunction<DoubleArraySolution, Double> {
    private int variableCount;
    private double[][] values;

    public Function(double[][] values) {
        super();
        this.values = values;
        variableCount = values[0].length;
    }

    @Override
    public Double calculateFitness(DoubleArraySolution solution) {
        double result = 0;
        double point[] = solution.values;
        double a = point[0];
        double b = point[1];
        double c = point[2];
        double d = point[3];
        double e = point[4];
        double f = point[5];

        for (int i = 0; i < values.length; i++) {
            double x1 = values[i][0];
            double x2 = values[i][1];
            double x3 = values[i][2];
            double x4 = values[i][3];
            double x5 = values[i][4];
            double y = values[i][5];
            result -= Math.pow(a * x1 + b * x1 * x1 * x1 * x2 + c * Math.exp(d * x3) * (1 + Math.cos(e * x4)) + f * x4 * x5 * x5 - y, 2);
        }

        return result;
    }

    @Override
    public int variableCount() {
        return variableCount;
    }
}
