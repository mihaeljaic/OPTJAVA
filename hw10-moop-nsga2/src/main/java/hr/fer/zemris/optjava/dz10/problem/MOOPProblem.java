package hr.fer.zemris.optjava.dz10.problem;

import java.util.List;
import java.util.function.Function;

public class MOOPProblem {
    private List<Function<double[], Double>> functions;

    public MOOPProblem(List<Function<double[], Double>> functions) {
        this.functions = functions;
    }

    public int getNumberOfObjectives() {
        return functions.size();
    }

    public void evaluateSolution(double[] solution, double[] objectives) {
        for (int i = 0; i < objectives.length; i++) {
            objectives[i] = functions.get(i).apply(solution);
        }
    }
}
