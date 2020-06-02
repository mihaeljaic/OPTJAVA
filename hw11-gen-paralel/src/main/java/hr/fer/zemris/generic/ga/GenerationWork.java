package hr.fer.zemris.generic.ga;

import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.evaluator.Evaluator;
import hr.fer.zemris.generic.ga.evaluator.EvaluatorImplementation;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.generic.ga.solution.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GenerationWork implements Runnable {
    public static GeneticTask POISON = new GeneticTask(null, -1);
    private static Queue<GeneticTask> forProcessing;
    private static Queue<List<IntegerArraySolution>> processed;

    private ISelection<IntegerArraySolution> selection;
    private ICrossover<IntegerArraySolution> crossover;
    private IMutation<IntegerArraySolution> mutation;

    public GenerationWork(ISelection<IntegerArraySolution> selection, ICrossover<IntegerArraySolution> crossover,
                          IMutation<IntegerArraySolution> mutation) {
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public void run() {
        IRNG rng = RNG.getRNG();
        EvaluatorImplementation evaluator;
        try {
            evaluator = Evaluator.getEvaluator();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            GeneticTask task = forProcessing.poll();
            if (task == null) continue;
            if (task == POISON) {
                System.out.println("Something's wrong with the food.");
                return; // RIP
            }

            List<IntegerArraySolution> children = new ArrayList<>();
            int childrenToAdd = task.getChildrenToProduce();
            List<IntegerArraySolution> population = task.getPopulation();
            for (int i = 0; i < childrenToAdd; i++) {
                IntegerArraySolution parent1 = selection.select(population, rng);
                IntegerArraySolution parent2 = selection.select(population, rng);
                IntegerArraySolution offspring = crossover.crossover(parent1, parent2, rng);
                mutation.mutate(offspring, rng);

                evaluator.evaluate(offspring);
                children.add(offspring);
            }

            processed.add(children);
        }
    }

    public static void setForProcessing(Queue<GeneticTask> forProcessing) {
        GenerationWork.forProcessing = forProcessing;
    }

    public static void setProcessed(Queue<List<IntegerArraySolution>> processed) {
        GenerationWork.processed = processed;
    }
}
