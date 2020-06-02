package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.evaluator.Evaluator;
import hr.fer.zemris.generic.ga.evaluator.EvaluatorImplementation;
import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static void loadTemplateImage(String path, int[] minValues, int[] maxValues) throws IOException {
        GrayScaleImage templateImage = GrayScaleImage.load(new File(path));
        EvaluatorImplementation.setTemplate(templateImage);

        int width = templateImage.getWidth();
        int height = templateImage.getHeight();

        System.out.println("Image width: " + width);
        System.out.println("Image height: " + height);

        minValues[0] = 0;
        maxValues[0] = 255;
        minValues[1] = minValues[2] = 0;
        maxValues[1] = width - 1;
        maxValues[2] = height - 1;
        minValues[3] = minValues[4] = 1;
        maxValues[3] = width;
        maxValues[4] = height;
    }

    public static List<IntegerArraySolution> createStartingPopulation(int[] minValues, int[] maxValues, int populationSize, int rectangleCount) {
        List<IntegerArraySolution> startingPopulation = new ArrayList<>(populationSize);
        IRNG rng = RNG.getRNG();
        int dimension = 5 * rectangleCount + 1;
        for (int i = 0; i < populationSize; i++) {
            int[] data = new int[dimension];
            for (int j = 0; j < dimension; j++) {
                data[j] = rng.nextInt(minValues[j % 5], maxValues[j % 5]);
            }

            startingPopulation.add(new IntegerArraySolution(data));
        }

        return startingPopulation;
    }

    public static void saveParameters(String path, int rectangleCount, int populationSize, int maxGenerations, double minError,
                                       int contendersCount, double probability, int sigma) throws IOException {
        List<String> parameters = new ArrayList<>();

        parameters.add("rectangleCount = " + rectangleCount);
        parameters.add("populationSize = " + populationSize);
        parameters.add("maxGenerations = " + maxGenerations);
        parameters.add("minError = " + minError);
        parameters.add("contendersCount = " + contendersCount);
        parameters.add("probability = " + probability);
        parameters.add("sigma = " + sigma);

        Files.write(Paths.get(path), parameters, StandardCharsets.UTF_8);
    }

    public static void exportImage(IntegerArraySolution best, String path) throws IOException {
        EvaluatorImplementation evaluator = Evaluator.getEvaluator();
        evaluator.evaluate(best);
        System.out.println(best.fitness);
        evaluator.getImage().save(new File(path));
    }
}
