package hr.fer.zemris.generic.ga.evaluator;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.solution.GASolution;

public class EvaluatorImplementation implements IGAEvaluator<int[]> {
    private static GrayScaleImage template;
    private GrayScaleImage image;

    public static void setTemplate(GrayScaleImage template) {
        EvaluatorImplementation.template = template;
    }

    public GrayScaleImage getTemplate() {
        return template;
    }

    public GrayScaleImage getImage() {
        return image;
    }

    public GrayScaleImage draw(GASolution<int[]> solution, GrayScaleImage image) {
        if (image == null) {
            image = new GrayScaleImage(template.getWidth(), template.getHeight());
        }

        int[] solutionData = solution.getData();
        byte bgcol = (byte) solutionData[0];
        image.clear(bgcol);

        int n = (solutionData.length - 1) / 5;
        int index = 1;
        for (int i = 0; i < n; i++) {
            image.rectangle(solutionData[index], solutionData[index + 1], solutionData[index + 2],
                    solutionData[index + 3], (byte) solutionData[index + 4]);
            index += 5;
        }

        return image;
    }

    @Override
    public void evaluate(GASolution<int[]> solution) {
        if (image == null) {
            image = new GrayScaleImage(template.getWidth(), template.getHeight());
        }

        draw(solution, image);

        byte[] data = image.getData();
        byte[] tdata = template.getData();
        int w = image.getWidth();
        int h = image.getHeight();

        double error = 0.0;
        int index2 = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                error += Math.abs(((int) data[index2] & 0xFF) - ((int) tdata[index2] & 0xFF));
                index2++;
            }
        }

        solution.fitness = -error;
    }
}
