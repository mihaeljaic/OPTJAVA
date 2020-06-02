package hr.fer.zemris.optjava.dz3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz3.decode.BitVectorDecoder;
import hr.fer.zemris.optjava.dz3.decode.IDecoder;
import hr.fer.zemris.optjava.dz3.decode.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decode.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.neighboorhood.BitVectorUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.neighboorhood.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.dz3.tempSchedule.GeometricTempSchedule;

public class RegresijaSustava {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Argumenti...");
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(args[1]), StandardCharsets.UTF_8);
        IFunction function = parseFunction(lines);

        if (args[0].equals("decimal")) {
            DoubleArraySolution x0 = new DoubleArraySolution(function.variableCount());
            IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();

            long simKaljenjaStartTime = System.currentTimeMillis();
            x0.randomize(new Random(), new double[]{-5, -5, -5, -5, -5, -5}, new double[]{5, 5, 5, 5, 5, 5});
            double[] deltas = new double[]{2, 2, 2, 2, 2, 2};
            double initTemp = 10000;
            for (int f = 0; f < 20; f++) {
                for (int i = 0; i < deltas.length; i++) {
                    deltas[i] /= 5;
                }
                initTemp /= 4;
                System.out.println("deltas: " + deltas[0]);
                System.out.println("initTemp: " + initTemp);
                System.out.println("x0: " + x0);
                System.out.println("Greška: " + function.valueAt(decoder.decode(x0)));
                System.out.println();
                for (int m = 0; m < 300; m++) {
                    IOptAlgorithm<DoubleArraySolution> algSimKaljenja = new SimulatedAnnealing<>(decoder,
                            new DoubleArrayUnifNeighborhood(deltas, 0.15), x0
                            , function, true, new GeometricTempSchedule(0.99, initTemp, 100, 1000));
                    DoubleArraySolution newResult = algSimKaljenja.run();
                    if (function.valueAt(decoder.decode(newResult)) < function.valueAt(decoder.decode(x0))) {
                        x0 = newResult;
                    }
                }
                if (function.valueAt(decoder.decode(x0)) > 1) {
                    x0.randomize(new Random(), new double[]{-5, -5, -5, -5, -5, -5}, new double[]{5, 5, 5, 5, 5, 5});
                }
            }

            System.out.println("Pronađeno rješenje: " + x0);
            System.out.println("Greška: " + function.valueAt(decoder.decode(x0)));
            System.out.println("Vrijeme izvođenja: " + ((double) System.currentTimeMillis() - simKaljenjaStartTime) / 1000 + "s");
        } else if (args[0].startsWith("binary")) {
            int bitNumber = 0;
            try {
                bitNumber = Integer.parseInt(args[0].split(":")[1]);
            } catch (NumberFormatException ex) {
                System.out.println("Očekujem broj bitova.");
            }

            IDecoder<BitVectorSolution> decoder = new NaturalBinaryDecoder(-10, 10, function.variableCount(), bitNumber);

            BitVectorSolution result = null;
            long simKaljenjaStartTime = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                BitVectorSolution x0 = new BitVectorSolution(function.variableCount(), bitNumber);
                x0.randomize(new Random());
                IOptAlgorithm<BitVectorSolution> alg = new SimulatedAnnealing<>(decoder,
                        new BitVectorUnifNeighborhood(((BitVectorDecoder)decoder).getTotalBits()), x0, function, true,
                        new GeometricTempSchedule(0.99, 1000, 100, 1000));
                BitVectorSolution newResult = alg.run();

                if (result == null || function.valueAt(decoder.decode(newResult)) < function.valueAt(decoder.decode(result))) {
                    result = newResult;
                }
            }

            System.out.println("Pronađeno rješenje: " + result);
            System.out.println("Greška: " + function.valueAt(decoder.decode(result)));
            System.out.println("Vrijeme izvođenja: " + ((double) System.currentTimeMillis() - simKaljenjaStartTime) / 1000 + "s");
        } else {
            System.out.println("Nepoznat argument " + args[0]);
        }

    }

    private static IFunction parseFunction(List<String> lines) {
        List<String[]> temp = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }

            temp.add(line.substring(1, line.length() - 1).split(",\\s*"));
        }

        int rows = temp.size();
        int columns = temp.get(0).length;
        double[][] values = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            int j = 0;
            for (String s : temp.get(i)) {
                values[i][j++] = Double.parseDouble(s);
            }
        }

        return new Function(values);
    }

}
