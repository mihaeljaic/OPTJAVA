package hr.fer.zemris.optjava.algorithm.crossover;

import hr.fer.zemris.optjava.algorithm.solution.AntSolution;
import hr.fer.zemris.optjava.algorithm.solution.Node;

public class ReplaceSubTree implements ICrossover {

    @Override
    public AntSolution[] crossover(AntSolution parent1, AntSolution parent2) {
        Node.Result subtree1 = parent1.getRoot().pickRandomNode();
        Node.Result subtree2 = parent2.getRoot().pickRandomNode();

        if (subtree1.getParent() == null) {
            parent1 = new AntSolution(subtree2.getNode());
        } else {
            subtree1.getParent().getChildren().set(subtree1.getChildIndex(), subtree2.getNode());
        }

        if (subtree2.getParent() == null) {
            parent2 = new AntSolution(subtree1.getNode());
        } else {
            subtree2.getParent().getChildren().set(subtree2.getChildIndex(), subtree1.getNode());
        }

        AntSolution[] offsprings = new AntSolution[2];
        offsprings[0] = parent1;
        offsprings[1] = parent2;

        return offsprings;
    }
}
