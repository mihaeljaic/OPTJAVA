package hr.fer.zemris.optjava.dz8;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Program accepts 2 arguments. First is path to file that contains values in each row. Program then maps values
 * from first file to file that is specified in second argument. Values are mapped into interval [-1, 1].
 *
 * @author Mihael Jaić
 */

public class DatasetGenerator {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Expected 2 arguments.");
            return;
        }

        File outputFile = new File(args[1]);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        Files.write(outputFile.toPath(), mapValues(Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8))
                , StandardCharsets.UTF_8, StandardOpenOption.WRITE);
    }

    private static List<String> mapValues(List<String> lines) {
        List<String> output = new ArrayList<>();
        double min = Double.parseDouble(lines.get(0));
        double max = min;
        for (int i = 1; i < lines.size(); i++) {
            double value = Double.parseDouble(lines.get(i));
            if (value > max) {
                max = value;
            } else if (value < min) {
                min = value;
            }
        }

        double span = max - min;
        for (String line : lines) {
            double mappedValue = 2 * (Double.parseDouble(line) - min) / span - 1;
            output.add(Double.toString(mappedValue));
        }

        return output;
    }

}
