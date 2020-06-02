package hr.fer.zemris.optjava.dz9.algorithm;

import hr.fer.zemris.optjava.dz9.algorithm.solution.Solution;

import java.util.List;

public class SharingFunction {
    private double sigma;
    private double alpha;
    private boolean decisionSpaceDistance;

    public SharingFunction(double sigma, double alpha, boolean decisionSpaceDistance) {
        this.sigma = sigma;
        this.alpha = alpha;
        this.decisionSpaceDistance = decisionSpaceDistance;
    }

    public double calculateNicheDensity(List<Solution> population, Solution solution) {
        double nicheDensity = 0.0;
        int iterations = decisionSpaceDistance ? solution.getDecisionSpace().length : solution.getObjectiveSpace().length;
        double[] minValues = new double[iterations];
        double[] maxValues = new double[iterations];
        for (int i = 0; i < iterations; i++) {
            minValues[i] = Double.MAX_VALUE;
            maxValues[i] = -Double.MAX_VALUE;
        }

        for (int i = 0; i < population.size(); i++) {
            Solution sol = population.get(i);
            for (int j = 0; j < iterations; j++) {
                double value = decisionSpaceDistance ? sol.getDecisionSpace()[j] : sol.getObjectiveSpace()[j];
                if (value < minValues[j]) {
                    minValues[j] = value;
                }
                if (value > maxValues[j]) {
                    maxValues[j] = value;
                }
            }
        }

        for (int i = 0; i < population.size(); i++) {
            double distance = calculateDistance(solution, population.get(i), minValues, maxValues);
            if (distance < sigma) {
                nicheDensity += 1 - Math.pow(distance / sigma, alpha);
            }
        }

        return nicheDensity;
    }

    private double calculateDistance(Solution solution1, Solution solution2, double[] minValues, double[] maxValues) {
        double distance = 0.0;
        int iterations = decisionSpaceDistance ? solution1.getDecisionSpace().length : solution1.getObjectiveSpace().length;
        double[] values1 = decisionSpaceDistance ? solution1.getDecisionSpace() : solution1.getObjectiveSpace();
        double[] values2 = decisionSpaceDistance ? solution2.getDecisionSpace() : solution2.getObjectiveSpace();
        for (int i = 0; i < iterations; i++) {
            distance += Math.pow((values1[i] - values2[i]) / (maxValues[i] - minValues[i]), 2);
        }

        return Math.sqrt(distance);
    }

}
