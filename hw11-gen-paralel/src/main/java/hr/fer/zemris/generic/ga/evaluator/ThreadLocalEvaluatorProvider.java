package hr.fer.zemris.generic.ga.evaluator;

import hr.fer.zemris.art.GrayScaleImage;

import java.io.File;
import java.io.IOException;

public class ThreadLocalEvaluatorProvider implements IEvaluatorProvider {
    private ThreadLocal<EvaluatorImplementation> threadLocal = new ThreadLocal<>();
    private static final File templateImage = new File("./11-kuca-200-133.png");

    @Override
    public EvaluatorImplementation getEvaluator() throws IOException {
        EvaluatorImplementation evaluatorImplementation = threadLocal.get();
        if (evaluatorImplementation == null) {
            System.out.printf("Creating new evaluatorImplementation for thread %s.%n", Thread.currentThread().getName());
            evaluatorImplementation = new EvaluatorImplementation();

            threadLocal.set(evaluatorImplementation);
        }

        return evaluatorImplementation;
    }
}
