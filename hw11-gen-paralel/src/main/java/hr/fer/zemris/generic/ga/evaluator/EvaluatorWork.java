package hr.fer.zemris.generic.ga.evaluator;

import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;

import java.io.IOException;
import java.util.Queue;

public class EvaluatorWork implements Runnable {
    public static volatile IntegerArraySolution POISON = new IntegerArraySolution(0);
    private static Queue<IntegerArraySolution> forEvaluation;
    private static Queue<IntegerArraySolution> evaluated;
    private EvaluatorImplementation evaluator;

    @Override
    public void run() {
        try {
            evaluator = Evaluator.getEvaluator();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            IntegerArraySolution solution = forEvaluation.poll();
            if (solution == POISON) {
                System.out.println("Something's wrong with the food");
                return; // RIP
            }
            if (solution == null) continue;

            evaluator.evaluate(solution);

            evaluated.add(solution);
        }
    }

    public static void setForEvaluation(Queue<IntegerArraySolution> forEvaluation) {
        EvaluatorWork.forEvaluation = forEvaluation;
    }

    public static void setEvaluated(Queue<IntegerArraySolution> evaluated) {
        EvaluatorWork.evaluated = evaluated;
    }

}
