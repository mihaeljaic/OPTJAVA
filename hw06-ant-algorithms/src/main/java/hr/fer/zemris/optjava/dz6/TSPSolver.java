package hr.fer.zemris.optjava.dz6;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TSPSolver {

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Expected 4 arguments.");
            return;
        }

        Function function = parseFunction(Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8));
        int k = Integer.parseInt(args[1]);
        int l = Integer.parseInt(args[2]);
        int maxIterations = Integer.parseInt(args[3]);

        MMAS mmas = new MMAS(function, function.getN(), l, k, 1, maxIterations, Math.round(maxIterations / 5), 1, 3, 0, 1, 0.02, 0.05, 1);
        int[] bestSolution = mmas.run();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < function.getN(); i++) {
            sb.append((bestSolution[i]+1) + " ");
        }
        System.out.println("Best solution: " + sb.toString());
        System.out.println("Length: " + function.calculateFitness(bestSolution));

    }

    private static Function parseFunction(List<String> lines) {
        int i = 0;
        while (!lines.get(i).trim().toUpperCase().startsWith("DIMENSION")) i++;
        int n = Integer.parseInt(lines.get(i).split(":")[1].trim());
        i++;
        while (!lines.get(i).trim().toUpperCase().equals("NODE_COORD_SECTION") && !lines.get(i).trim().toUpperCase().equals("EDGE_WEIGHT_SECTION"))
            i++;

        double[][] distances = new double[n][n];
        if (lines.get(i).trim().toUpperCase().equals("EDGE_WEIGHT_SECTION")) {
            i++;
            String line = lines.get(i).trim();
            for (int j = 0; j < n; j++) {
                String[] temp = line.split("\\s+");
                for (int k = 0; k < n; k++) {
                    distances[j][k] = Double.parseDouble(temp[k]);
                }

                i++;
                line = lines.get(i).trim();
            }

        } else {
            i++;
            double[][] positions = new double[n][2];
            int index = 0;
            String line = lines.get(i).trim();
            while (!line.toUpperCase().equals("EOF")) {
                String[] temp = line.split("\\s+");
                positions[index][0] = Double.parseDouble(temp[1]);
                positions[index][1] = Double.parseDouble(temp[2]);
                index++;
                i++;
                line = lines.get(i).trim();
            }

            for (i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double distanceX = positions[i][0] - positions[j][0];
                    double distanceY = positions[i][1] - positions[j][1];
                    distances[i][j] = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                }
            }
        }

        return new Function(distances);
    }

}
