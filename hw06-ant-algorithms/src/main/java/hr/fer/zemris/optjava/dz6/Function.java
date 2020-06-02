package hr.fer.zemris.optjava.dz6;


public class Function {
    private int n;
    double[][] distances;

    public Function(double[][] distances) {
        this.distances = distances;
        n = distances.length;
    }

    public int getN() {
        return n;
    }

    public double calculateFitness(int[] route) {
        double distance = 0.0;
        int currentCity = 0;
        for (int i = 1; i < n; i++) {
            distance += distances[currentCity][route[i]];
            currentCity = route[i];
        }
        distance += distances[route[n-1]][0];

        return distance;
    }

}
