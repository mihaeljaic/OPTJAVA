package hr.fer.zemris.generic.ga.evaluator;

import java.io.IOException;

public interface IEvaluatorProvider {

    EvaluatorImplementation getEvaluator() throws IOException;
}
