package hr.fer.zemris.optjava.dz6;

import java.util.Arrays;

public class MMAS {
    private Function function;
    private int n;
    private int l;
    private int k;
    private double e;
    private int maxIterations;
    private int maxNoImprovementIterations;
    private double alpha;
    private double beta;
    private double pMin;
    private double pMax;
    private double ro;
    private double a;
    private double cbsProbability;

    private double tauMin;
    private double tauMax;
    private double cbs;
    private int[][] candidates;
    private int[] best;
    private double[][] probabilities;
    private double[][] pheromones;
    private double[][] eta;

    public MMAS(Function function, int n, int l, int k, double e, int maxIterations, int maxNoImprovementIterations,
                double alpha, double beta, double pMin, double pMax, double ro, double p, double cbsProbability) {
        this.function = function;
        this.n = n;
        this.l = l;
        this.k = k;
        this.e = e;
        this.maxIterations = maxIterations;
        this.maxNoImprovementIterations = maxNoImprovementIterations;
        this.alpha = alpha;
        this.beta = beta;
        this.pMin = pMin;
        this.pMax = pMax;
        this.ro = ro;
        this.cbsProbability = cbsProbability;
        probabilities = new double[n][n];
        pheromones = new double[n][n];
        eta = new double[n][n];

        double mi = (double) (n - 1) / (n * (Math.pow(p, -1.0 / n) - 1));
        a = mi * n;
    }

    public int[] run() {
        initializeEta();
        initializeCandidates();
        nearestNeighbour();
        System.out.println("Nearest neighbour distance: " + cbs);
        tauMax = 1 / (ro * cbs);
        tauMin = tauMax / a;
        setInitialPheromones();

        int iteration = 0;
        int noImprovementCount = 0;
        while (iteration < maxIterations) {
            int[] bestIterationRoute = null;
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < l; i++) {
                int[] route = createRoute();
                double distance = function.calculateFitness(route);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestIterationRoute = route;
                }
            }
            System.out.println("Iteration's shortest distance: " + minDistance);
            if (minDistance < cbs) {
                System.out.println("Found new best solution: " + minDistance);
                noImprovementCount = 0;
                cbs = minDistance;
                best = bestIterationRoute;
                tauMax = 1 / (ro * cbs);
                tauMin = tauMax / a;
            }

            evaporatePheromones();
            if (Math.random() < cbsProbability) {
                updatePheromones(best);
            } else {
                updatePheromones(bestIterationRoute);
            }

            cbsProbability += 1.0 / maxNoImprovementIterations;
            noImprovementCount++;
            if (noImprovementCount == maxNoImprovementIterations) {
                System.out.println("Stuck in local optimum. Reseting pheromones.");
                setInitialPheromones();
                noImprovementCount = 0;
                cbsProbability = 0;
            }
            iteration++;
        }

        return best;
    }

    private int[] createRoute() {
        int[] route = new int[n];
        route[0] = 0;
        boolean[] visited = new boolean[n];
        visited[0] = true;
        int currentCity = 0;
        for (int i = 1; i < n; i++) {
            double probabilitySum = 0.0;
            boolean found = false;
            for (int candidate : candidates[currentCity]) {
                if (visited[candidate]) continue;
                found = true;
                probabilitySum += probabilities[currentCity][candidate];
            }
            if (!found) {
                for (int j = 0; j < n; j++) {
                    if (visited[j]) continue;
                    probabilitySum += probabilities[currentCity][j];
                }
            }

            double rWheel = 0.0;
            double random = Math.random();
            int index = 1;
            if (found) {
                int[] possibleCandidates = candidates[currentCity];
                int pos = 0;
                while (rWheel < random) {
                    if (pos == k) break;
                    while (pos < k && visited[possibleCandidates[pos]]) {
                        pos++;
                    }
                    if (pos == k) break;
                    rWheel += probabilities[currentCity][possibleCandidates[pos]] / probabilitySum;
                    pos++;
                }
                pos--;
                route[i] = possibleCandidates[pos];
                visited[possibleCandidates[pos]] = true;
                currentCity = possibleCandidates[pos];
            } else {
                while (rWheel < random) {
                    if (index == n) break;
                    while (visited[index]) index++;
                    rWheel += probabilities[currentCity][index] / probabilitySum;
                    index++;
                }
                index--;
                route[i] = index;
                visited[index] = true;
                currentCity = index;
            }
        }

        return route;
    }

    private void updatePheromones(int[] route) {
        int currentCity = route[0];
        for (int i = 1; i < n; i++) {
            pheromones[currentCity][route[i]] = Math.min(tauMax, pheromones[currentCity][route[i]] + e / cbs);
            probabilities[currentCity][route[i]] = Math.pow(pheromones[currentCity][route[i]], alpha) * eta[currentCity][route[i]];
            currentCity = route[i];
        }

        pheromones[route[n - 1]][0] = Math.min(tauMax, pheromones[route[n - 1]][0] + e / cbs);
        probabilities[route[n - 1]][0] = Math.pow(pheromones[route[n - 1]][0], alpha) * eta[route[n - 1]][route[0]];
    }

    private void initializeCandidates() {
        candidates = new int[n][k];
        for (int i = 0; i < n; i++) {
            IndexDistance[] temp = new IndexDistance[n];
            int index = 0;
            for (double d : function.distances[i]) {
                temp[index] = new IndexDistance(index, d);
                index++;
            }
            Arrays.sort(temp);
            for (int j = 0; j + 1 < k; j++) {
                candidates[i][j] = temp[j + 1].index;
            }
        }
    }

    private void nearestNeighbour() {
        best = new int[n];
        cbs = 0.0;
        best[0] = 0;
        int currentCity = 0;
        boolean[] visited = new boolean[n];
        visited[0] = true;
        for (int i = 1; i < n; i++) {
            double minDistance = Double.MAX_VALUE;
            int index = 0;
            for (int j = 0; j < n; j++) {
                if (visited[j]) continue;
                if (function.distances[currentCity][j] < minDistance) {
                    minDistance = function.distances[currentCity][j];
                    index = j;
                }
            }

            best[i] = index;
            cbs += minDistance;
            visited[index] = true;
            currentCity = index;
        }

        cbs += function.distances[best[n - 1]][0];
    }

    private void evaporatePheromones() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = Math.max(tauMin, pheromones[i][j] *= (1 - ro));
            }
        }
    }

    private void setInitialPheromones() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = tauMax;
                probabilities[i][j] = tauMax * eta[i][j];
            }
        }
    }

    private void initializeEta() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                eta[i][j] = Math.pow(1 / function.distances[i][j], beta);
            }
        }
    }

    private static class IndexDistance implements Comparable<IndexDistance> {
        private int index;
        private double distance;

        public IndexDistance(int index, double distance) {
            this.index = index;
            this.distance = distance;
        }

        @Override
        public int compareTo(IndexDistance o) {
            return Double.compare(distance, o.distance);
        }
    }

}
